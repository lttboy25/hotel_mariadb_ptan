/*
 * @ (#) KhachHangDao.java     1.0    4/29/2026
 *
 * Copyright (c) 2026 IUH. All rights reserved.
 */
package iuh.dao;

import iuh.entity.KhachHang;

import java.util.List;

/*
 * @description
 * @author:NguyenTruong
 * @date:  4/29/2026
 * @version:    1.0
 */
public interface KhachHangDao {
    // Tự động phát sinh mã khách hàng mới theo định dạng KHxxx [cite: 245]
    String generateNextMaKH();

    // Tìm kiếm khách hàng theo từ khóa (tên hoặc số điện thoại)
    List<KhachHang> findByKeyword(String keyword);

    KhachHang findByCCCD(String cccd);
}
