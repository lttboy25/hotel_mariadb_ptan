/*
 * @ (#) HoaDonDao.java     1.0    4/29/2026
 *
 * Copyright (c) 2026 IUH. All rights reserved.
 */
package iuh.dao;

import iuh.dto.HoaDonDTO;
import iuh.entity.HoaDon;
import iuh.enums.TrangThaiHoaDon;

import java.time.LocalDateTime;
import java.util.List;

/*
 * @description
 * @author:NguyenTruong
 * @date:  4/29/2026
 * @version:    1.0
 */
public interface HoaDonDao {
    List<HoaDon> getAllHoaDon();

    HoaDon getById(String maHoaDon);

    List<HoaDon> searchByKeyword(String keyword);

    List<HoaDon> getByNgayTao(LocalDateTime tuNgay, LocalDateTime denNgay);

    List<HoaDon> getByTrangThai(TrangThaiHoaDon trangThai);

    List<HoaDon> search(String keyword, LocalDateTime tuNgay, LocalDateTime denNgay, TrangThaiHoaDon trangThai);

    String generateMaHoaDon();

    HoaDon save(HoaDonDTO hoaDonDTO);

    double calculateRevenue(String maNhanVien, java.time.LocalDateTime start, java.time.LocalDateTime end);
}
