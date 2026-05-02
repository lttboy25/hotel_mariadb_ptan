/*
 * @ (#) ThongKeServiceImpl.java     1.0    4/28/2026
 *
 * Copyright (c) 2026 IUH. All rights reserved.
 */
package iuh.service.impl;

import iuh.dao.impl.ThongKeDaoImpl;
import iuh.dto.ThongKeDTO;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

public class ThongKeServiceImpl implements iuh.service.ThongKeService , Serializable {
    private final ThongKeDaoImpl thongKeDaoImpl = new ThongKeDaoImpl();

    @Override
    public ThongKeDTO layThongKe(LocalDate tuNgay, LocalDate denNgay) {
        if (tuNgay == null || denNgay == null) {
            throw new IllegalArgumentException("Khoảng ngày thống kê không hợp lệ.");
        }
        if (tuNgay.isAfter(denNgay)) {
            throw new IllegalArgumentException("Từ ngày phải nhỏ hơn hoặc bằng đến ngày.");
        }

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime thoiDiemThongKe = denNgay.atTime(LocalTime.MAX);
        if (thoiDiemThongKe.isAfter(now)) {
            thoiDiemThongKe = now;
        }
        long tongSoPhong = thongKeDaoImpl.demTongSoPhong();
        long soPhongDangSuDung = thongKeDaoImpl.demPhongDangSuDung(thoiDiemThongKe);
        long soKhachDangLuuTru = thongKeDaoImpl.demKhachDangLuuTru(now);
        long soLuotDatPhong = thongKeDaoImpl.demDatPhongTrongKhoang(tuNgay, denNgay);
        long soLuotHuyPhong = thongKeDaoImpl.demHuyPhongTrongKhoang(tuNgay, denNgay);

        long soNgay = ChronoUnit.DAYS.between(tuNgay, denNgay) + 1;
        LocalDate kyTruocDenNgay = tuNgay.minusDays(1);
        LocalDate kyTruocTuNgay = kyTruocDenNgay.minusDays(soNgay - 1);
        long soLuotDatPhongKyTruoc = thongKeDaoImpl.demDatPhongTrongKhoang(kyTruocTuNgay, kyTruocDenNgay);

        double tongDoanhThu = thongKeDaoImpl.tinhDoanhThuTrongKhoang(tuNgay.atStartOfDay(), denNgay.plusDays(1).atStartOfDay());
        double tyLeLapDay = tongSoPhong == 0 ? 0d : (soPhongDangSuDung * 100.0) / tongSoPhong;
        double tyLeHuyPhong = soLuotDatPhong == 0 ? 0d : (soLuotHuyPhong * 100.0) / soLuotDatPhong;
        double tyLeTangTruongDatPhong = soLuotDatPhongKyTruoc == 0
                ? (soLuotDatPhong > 0 ? 100d : 0d)
                : ((soLuotDatPhong - soLuotDatPhongKyTruoc) * 100.0) / soLuotDatPhongKyTruoc;

        return ThongKeDTO.builder()
                .tuNgay(tuNgay)
                .denNgay(denNgay)
                .soKhachDangLuuTru(soKhachDangLuuTru)
                .soPhongDangSuDung(soPhongDangSuDung)
                .tongSoPhong(tongSoPhong)
                .tyLeLapDay(tyLeLapDay)
                .soLuotDatPhong(soLuotDatPhong)
                .soLuotDatPhongKyTruoc(soLuotDatPhongKyTruoc)
                .tyLeTangTruongDatPhong(tyLeTangTruongDatPhong)
                .soLuotHuyPhong(soLuotHuyPhong)
                .tyLeHuyPhong(tyLeHuyPhong)
                .tongDoanhThu(tongDoanhThu)
                .dsDatPhongTheoThang(thongKeDaoImpl.thongKeDatPhongTheo12ThangGanNhat(denNgay))
                .dsDoanhThuTheoNgay(thongKeDaoImpl.thongKeDoanhThuTheoNgay(tuNgay, denNgay))
                .dsTrangThaiDatPhong(thongKeDaoImpl.thongKeTrangThaiDatPhong(tuNgay, denNgay))
                .dsLoaiPhong(thongKeDaoImpl.thongKeLoaiPhong(tuNgay, denNgay))
                .build();
    }
}
