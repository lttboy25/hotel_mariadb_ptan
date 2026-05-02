/*
 * @ (#) DatPhongDaoImpl.java     1.0    4/20/2026
 *
 * Copyright (c) 2026 IUH. All rights reserved.
 */
package iuh.dao.impl;

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
import java.util.List;

public class DatPhongDaoImpl implements iuh.dao.DatPhongDao {

    // public List<Phong>getDsPhongTrong(){
    // PhongDaoImpl phongDao = new PhongDaoImpl();
    // return phongDao.findPhongTrong();
    // }

    @Override
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
                    .trangThai(request.getTrangThai() == null
                            ? TrangThaiPhieuDatPhong.DA_DAT
                            : request.getTrangThai())
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

            List<Integer> soNguoiTheoPhong = allocatePeoplePerRoom(phongDaChon, request.getSoNguoiLon(),
                    request.getSoTreEm());

            for (int i = 0; i < phongDaChon.size(); i++) {
                Phong phongRef = phongDaChon.get(i);
                int soNguoiPhong = soNguoiTheoPhong.get(i);

                ChiTietPhieuDatPhong chiTiet = ChiTietPhieuDatPhong.builder()
                        .phieuDatPhong(phieuDatPhong)
                        .phong(phongRef)
                        .trangThai(TrangThaiChiTietPhieuDatPhong.CHUA_THANH_TOAN)
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

    // public List<Phong> getDsPhongByTrangThai(){}

    // public

}
