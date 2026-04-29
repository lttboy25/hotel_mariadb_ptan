package iuh.service;

import iuh.dao.impl.PhieuDatPhongDaoImpl;
import iuh.entity.KhachHang;
import iuh.entity.PhieuDatPhong;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PhieuDatPhongService {
    PhieuDatPhongDaoImpl phieuDatPhongDao = new PhieuDatPhongDaoImpl();

    public List<PhieuDatPhong> getAll() {
        List<PhieuDatPhong> list = new ArrayList<>();

        KhachHang kh = new KhachHang(); // tạm demo
        kh.setMaKhachHang("KH001");

        list.add(new PhieuDatPhong(
                "PDP001",
                LocalDate.parse("2026-02-01"),
                "Đã đặt",
                200000,
                kh,
                new ArrayList<>()
        ));

        list.add(new PhieuDatPhong(
                "PDP002",
                LocalDate.parse("2026-02-02"),
                "Đã nhận phòng",
                300000,
                kh,
                new ArrayList<>()
        ));

        return list;
    }

    public List<PhieuDatPhong> getByTrangThai(String trangThai) {
        List<PhieuDatPhong> list = new ArrayList<>();
        if (trangThai == null) {
            return list;
        }
        return phieuDatPhongDao.getPhieuDatPhongByStatus(trangThai);
    }

    public PhieuDatPhong getByMaPhieu(String maPhieu) {
        if (maPhieu == null) {
            return null;
        }
        return phieuDatPhongDao.getPhieuDatPhongByCode(maPhieu);
    }
    public List<PhieuDatPhong> getPhieuDatPhongByToPayment(String status, String cccd) {
        if (status == null ||  cccd == null) {
            return null;
        }
        return phieuDatPhongDao.getPhieuDatPhongByToPayment(status, cccd);
    }

    public boolean updateTrangThai(String maPhieu, String trangThai) {
        if (trangThai == null || trangThai.isEmpty() || maPhieu == null || maPhieu.isEmpty()) {return false;}
        return phieuDatPhongDao.updateStatusBookingTicket(maPhieu, trangThai);
    }


}