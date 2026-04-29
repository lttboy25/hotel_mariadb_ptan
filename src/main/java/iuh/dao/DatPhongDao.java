    /*
     * @ (#) DatPhongDao.java     1.0    4/20/2026
     *
     * Copyright (c) 2026 IUH. All rights reserved.
     */
    package iuh.dao;


    /*
     * @description
     * @author:NguyenTruong
     * @date:  4/20/2026
     * @version:    1.0
     */

    import iuh.db.JPAUtil;
    import iuh.dto.DatPhongRequestDTO;
    import iuh.entity.ChiTietPhieuDatPhong;
    import iuh.entity.KhachHang;
    import iuh.entity.PhieuDatPhong;
    import iuh.entity.Phong;
    import jakarta.persistence.EntityManager;
    import jakarta.persistence.EntityTransaction;

    import java.time.LocalDate;
    import java.time.LocalDateTime;
    import java.time.temporal.ChronoUnit;
    import java.util.List;

    public class DatPhongDao {

//        public List<Phong>getDsPhongTrong(){
//            PhongDao phongDao = new PhongDao();
//            return phongDao.findPhongTrong();
//        }

        public PhieuDatPhong saveDatPhong(DatPhongRequestDTO request) {
            EntityManager em = JPAUtil.getEntityManager();
            EntityTransaction tx = em.getTransaction();
            try {
                tx.begin();

                String maPdp = generateNextMaPhieuDatPhong(em);
                KhachHang khachHangRef = em.getReference(KhachHang.class, request.getMaKhachHang());

                PhieuDatPhong phieuDatPhong = PhieuDatPhong.builder()
                        .maPhieuDatPhong(maPdp)
                        .ngayTao(LocalDate.now())
                        .trangThai(request.getTrangThai() == null || request.getTrangThai().isBlank() ? "Đã đặt" : request.getTrangThai())
                        .tienDatCoc(0)
                        .khachHang(khachHangRef)
                        .build();
                em.persist(phieuDatPhong);

                int soGioLuuTru = calculateHours(request.getCheckIn(), request.getCheckOut());
                double tongTienPhong = 0.0;

                int tongSucChuaNguoiLon = 0;
                int tongSucChuaTreEm = 0;
                List<Phong> phongDaChon = new java.util.ArrayList<>();

                for (String maPhong : request.getMaPhongs()) {
                    if (!isRoomAvailable(em, maPhong, request.getCheckIn(), request.getCheckOut())) {
                        throw new IllegalStateException("Phòng " + maPhong + " đã có lịch trong khoảng thời gian này.");
                    }

                    Phong phongRef = em.find(Phong.class, maPhong);
                    if (phongRef == null) {
                        throw new IllegalStateException("Không tìm thấy phòng " + maPhong + ".");
                    }

                    if (phongRef.getLoaiPhong() != null) {
                        tongSucChuaNguoiLon += phongRef.getLoaiPhong().getSoNguoiLonToiDa();
                        tongSucChuaTreEm += phongRef.getLoaiPhong().getSoTreEmToiDa();
                    }

                    phongDaChon.add(phongRef);
                }

                if (request.getSoNguoiLon() > tongSucChuaNguoiLon || request.getSoTreEm() > tongSucChuaTreEm) {
                    throw new IllegalStateException("Số người vượt quá sức chứa của các phòng đã chọn.");
                }

                List<Integer> soNguoiTheoPhong = allocatePeoplePerRoom(phongDaChon, request.getSoNguoiLon(), request.getSoTreEm());

                for (int i = 0; i < phongDaChon.size(); i++) {
                    Phong phongRef = phongDaChon.get(i);
                    int soNguoiPhong = soNguoiTheoPhong.get(i);

                    ChiTietPhieuDatPhong chiTiet = ChiTietPhieuDatPhong.builder()
                            .phieuDatPhong(phieuDatPhong)
                            .phong(phongRef)
                            .trangThai("Chưa thanh toán")
                            .thoiGianNhanPhong(request.getCheckIn())
                            .thoiGianTraPhong(request.getCheckOut())
                            .soNguoi(soNguoiPhong)
                            .soGioLuuTru(soGioLuuTru)
                            .build();
                    em.persist(chiTiet);
                    phieuDatPhong.getDsachPhieuDatPhong().add(chiTiet);

                    if (phongRef.getLoaiPhong() != null) {
                        tongTienPhong += soGioLuuTru * phongRef.getLoaiPhong().getGia();
                    }

                    // Không cập nhật tinhTrang theo booking; tính sẵn sàng dựa trên thời gian đặt.
                }

                long tienDatCoc = Math.round(tongTienPhong * 0.3);
                phieuDatPhong.setTienDatCoc(tienDatCoc);

                tx.commit();
                return phieuDatPhong;
            } catch (Exception e) {
                if (tx.isActive()) {
                    tx.rollback();
                }
                throw new RuntimeException("Không thể lưu phiếu đặt phòng", e);
            } finally {
                em.close();
            }
        }

        private List<Integer> allocatePeoplePerRoom(List<Phong> rooms, int adults, int children) {
            List<Integer> allocation = new java.util.ArrayList<>();
            int remainingAdults = adults;
            int remainingChildren = children;
            for (Phong room : rooms) {
                int maxAdults = room.getLoaiPhong() != null ? room.getLoaiPhong().getSoNguoiLonToiDa() : 0;
                int maxChildren = room.getLoaiPhong() != null ? room.getLoaiPhong().getSoTreEmToiDa() : 0;

                int adultsInRoom = Math.min(remainingAdults, maxAdults);
                int childrenInRoom = Math.min(remainingChildren, maxChildren);
                allocation.add(adultsInRoom + childrenInRoom);

                remainingAdults -= adultsInRoom;
                remainingChildren -= childrenInRoom;
            }

            if (remainingAdults > 0 || remainingChildren > 0) {
                throw new IllegalStateException("Không đủ sức chứa để phân bổ số người theo phòng.");
            }
            return allocation;
        }

        private String generateNextMaPhieuDatPhong(EntityManager em) {
            Number maxNumber = (Number) em.createNativeQuery(
                            "SELECT MAX(CAST(SUBSTRING(maPhieuDatPhong, 4) AS UNSIGNED)) FROM PhieuDatPhong FOR UPDATE")
                    .getSingleResult();
            int nextNumber = (maxNumber == null) ? 1 : maxNumber.intValue() + 1;
            return String.format("PDP%03d", nextNumber);
        }

        private boolean isRoomAvailable(EntityManager em, String maPhong, LocalDateTime checkIn, LocalDateTime checkOut) {
            Long count = em.createQuery("""
                    SELECT COUNT(ct) FROM ChiTietPhieuDatPhong ct
                    WHERE ct.phong.maPhong = :maPhong
                    AND ct.thoiGianNhanPhong < :checkOut
                    AND ct.thoiGianTraPhong > :checkIn
                    AND ct.trangThai != 'Đã hủy'
                    """, Long.class)
                    .setParameter("maPhong", maPhong)
                    .setParameter("checkIn", checkIn)
                    .setParameter("checkOut", checkOut)
                    .getSingleResult();
            return count == 0;
        }

        private int calculateHours(LocalDateTime checkIn, LocalDateTime checkOut) {
            long hours = ChronoUnit.HOURS.between(checkIn, checkOut);
            return (int) Math.max(1, hours);
        }
        //public List<Phong> getDsPhongByTrangThai(){}

        //public

    }
