/*
 * @ (#) PhieuHuyPhongDao.java     1.0    4/29/2026
 *
 * Copyright (c) 2026 IUH. All rights reserved.
 */
package iuh.dao;

import iuh.entity.PhieuHuyPhong;

import java.util.List;

/*
 * @description
 * @author:NguyenTruong
 * @date:  4/29/2026
 * @version:    1.0
 */
public interface PhieuHuyPhongDao extends GenericDao<PhieuHuyPhong, Long> {
    boolean huyNhieuPhongNghiepVu(List<PhieuHuyPhong> listPhieuHuy, double tienHoan);
}
