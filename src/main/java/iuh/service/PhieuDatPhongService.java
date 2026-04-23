package iuh.service;

import iuh.entity.KhachHang;
import iuh.entity.PhieuDatPhong;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PhieuDatPhongService {

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
}