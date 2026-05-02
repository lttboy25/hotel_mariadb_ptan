/*
 * @ (#) ChiTietHoaDonDao.java     1.0    4/29/2026
 *
 * Copyright (c) 2026 IUH. All rights reserved.
 */
package iuh.dao;

import iuh.dto.ChiTietHoaDonDTO;
import iuh.entity.ChiTietHoaDon;

/*
 * @description
 * @author:NguyenTruong
 * @date:  4/29/2026
 * @version:    1.0
 */
public interface ChiTietHoaDonDao {
    ChiTietHoaDon save(ChiTietHoaDonDTO chiTietHoaDonDTO);
}
