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

                for (String maPhong : request.getMaPhongs()) {
                    if (!isRoomAvailable(em, maPhong, request.getCheckIn(), request.getCheckOut())) {
                        throw new IllegalStateException("Phòng " + maPhong + " đã có lịch trong khoảng thời gian này.");
                    }

                    Phong phongRef = em.find(Phong.class, maPhong);
                    if (phongRef == null) {
                        throw new IllegalStateException("Không tìm thấy phòng " + maPhong + ".");
                    }

                    ChiTietPhieuDatPhong chiTiet = ChiTietPhieuDatPhong.builder()
                            .phieuDatPhong(phieuDatPhong)
                            .phong(phongRef)
                            .trangThai("Đã đặt")
                            .thoiGianNhanPhong(request.getCheckIn())
                            .thoiGianTraPhong(request.getCheckOut())
                            .soNguoi(request.getSoNguoi())
                            .soGioLuuTru(soGioLuuTru)
                            .build();
                    em.persist(chiTiet);

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

        private boolean isRoomAvailable(EntityManager em, String maPhong, LocalDateTime checkIn, LocalDateTime checkOut) {
            Long count = em.createQuery("""
                    SELECT COUNT(ct) FROM ChiTietPhieuDatPhong ct
                    WHERE ct.phong.maPhong = :maPhong
                    AND ct.thoiGianNhanPhong < :checkOut
                    AND ct.thoiGianTraPhong > :checkIn
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

        private String generateNextMaPhieuDatPhong(EntityManager em) {
            List<String> allCodes = em.createQuery(
                            "select p.maPhieuDatPhong from PhieuDatPhong p", String.class)
                    .getResultList();
            int nextNumber = allCodes.stream()
                    .filter(code -> code != null && code.matches(".*\\d+"))
                    .map(code -> Integer.parseInt(code.replaceAll("\\D+", "")))
                    .max(Integer::compareTo)
                    .orElse(0) + 1;
            return String.format("PDP%03d", nextNumber);
        }
        //public List<Phong> getDsPhongByTrangThai(){}

        //public

    }
