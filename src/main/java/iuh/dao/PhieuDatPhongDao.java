/*
 * @ (#) PhieuDatPhongDao.java     1.0    4/29/2026
 *
 * Copyright (c) 2026 IUH. All rights reserved.
 */
package iuh.dao;

import iuh.entity.PhieuDatPhong;
import iuh.entity.TrangThaiPhieuDatPhong;

import java.util.List;

/*
 * @description
 * @author:NguyenTruong
 * @date:  4/29/2026
 * @version:    1.0
 */
public interface PhieuDatPhongDao {
    List<PhieuDatPhong> getAll();

    List<PhieuDatPhong> getPhieuDatPhongByStatus(TrangThaiPhieuDatPhong status);

    PhieuDatPhong getPhieuDatPhongByCode(String maPhieu);

    boolean updateStatusBookingTicket(String maPhieu, TrangThaiPhieuDatPhong trangThai);

    List<PhieuDatPhong> getPhieuDatPhongByToPayment(TrangThaiPhieuDatPhong status, String cccd);
}
