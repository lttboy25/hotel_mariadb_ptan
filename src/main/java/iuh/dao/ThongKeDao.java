/*
 * @ (#) ThongKeDao.java     1.0    4/29/2026
 *
 * Copyright (c) 2026 IUH. All rights reserved.
 */
package iuh.dao;

import iuh.dto.ThongKeDTO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

/*
 * @description
 * @author:NguyenTruong
 * @date:  4/29/2026
 * @version:    1.0
 */
public interface ThongKeDao {
    DateTimeFormatter MONTH_LABEL_FORMAT =
            DateTimeFormatter.ofPattern("MM/yyyy", Locale.forLanguageTag("vi-VN"));

    long demTongSoPhong();

    long demPhongDangSuDung(LocalDateTime thoiDiem);

    long demKhachDangLuuTru(LocalDateTime thoiDiem);

    long demDatPhongTrongKhoang(LocalDate tuNgay, LocalDate denNgay);

    long demHuyPhongTrongKhoang(LocalDate tuNgay, LocalDate denNgay);

    double tinhDoanhThuTrongKhoang(LocalDateTime tuNgay, LocalDateTime denNgay);

    List<ThongKeDTO.DuLieuCotDTO> thongKeDatPhongTheo12ThangGanNhat(LocalDate denNgay);

    List<ThongKeDTO.DuLieuNgayDTO> thongKeDoanhThuTheoNgay(LocalDate tuNgay, LocalDate denNgay);

    List<ThongKeDTO.DuLieuTyTrongDTO> thongKeTrangThaiDatPhong(LocalDate tuNgay, LocalDate denNgay);

    List<ThongKeDTO.DuLieuTyTrongDTO> thongKeLoaiPhong(LocalDate tuNgay, LocalDate denNgay);
}
