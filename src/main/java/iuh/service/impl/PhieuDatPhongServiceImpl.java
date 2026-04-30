package iuh.service.impl;

import iuh.dao.impl.PhieuDatPhongDaoImpl;
import iuh.entity.KhachHang;
import iuh.entity.PhieuDatPhong;
import iuh.entity.TrangThaiPhieuDatPhong;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PhieuDatPhongServiceImpl implements iuh.service.PhieuDatPhongService {
    PhieuDatPhongDaoImpl phieuDatPhongDao = new PhieuDatPhongDaoImpl();

    @Override
    public List<PhieuDatPhong> getAll() {
        List<PhieuDatPhong> list = new ArrayList<>();

        KhachHang kh = new KhachHang(); // tạm demo
        kh.setMaKhachHang("KH001");

        list.add(new PhieuDatPhong(
                "PDP001",
                LocalDate.parse("2026-02-01"),
                TrangThaiPhieuDatPhong.DA_DAT,
                200000,
                kh,
                new ArrayList<>()));

        list.add(new PhieuDatPhong(
                "PDP002",
                LocalDate.parse("2026-02-02"),
                TrangThaiPhieuDatPhong.NHAN_PHONG,
                300000,
                kh,
                new ArrayList<>()));

        return list;
    }

    @Override
    public List<PhieuDatPhong> getByTrangThai(TrangThaiPhieuDatPhong trangThai) {
        List<PhieuDatPhong> list = new ArrayList<>();
        if (trangThai == null) {
            return list;
        }
        return phieuDatPhongDao.getPhieuDatPhongByStatus(trangThai);
    }

    @Override
    public PhieuDatPhong getByMaPhieu(String maPhieu) {
        if (maPhieu == null) {
            return null;
        }
        return phieuDatPhongDao.getPhieuDatPhongByCode(maPhieu);
    }

    @Override
    public List<PhieuDatPhong> getPhieuDatPhongByToPayment(TrangThaiPhieuDatPhong status, String cccd) {
        if (status == null || cccd == null) {
            return null;
        }
        return phieuDatPhongDao.getPhieuDatPhongByToPayment(status, cccd);
    }

    @Override
    public boolean updateTrangThai(String maPhieu, TrangThaiPhieuDatPhong trangThai) {
        if (trangThai == null || maPhieu == null || maPhieu.isEmpty()) {
            return false;
        }
        return phieuDatPhongDao.updateStatusBookingTicket(maPhieu, trangThai);
    }

}