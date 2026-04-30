/*
 * @ (#) HoaDonDao.java     1.0    4/29/2026
 *
 * Copyright (c) 2026 IUH. All rights reserved.
 */
package iuh.dao;

import iuh.dto.HoaDonDTO;
import iuh.entity.HoaDon;

/*
 * @description
 * @author:NguyenTruong
 * @date:  4/29/2026
 * @version:    1.0
 */
public interface HoaDonDao {
    String generateMaHoaDon();

    HoaDon save(HoaDonDTO hoaDonDTO);
    double calculateRevenue(String maNhanVien, java.time.LocalDateTime start, java.time.LocalDateTime end);
}
