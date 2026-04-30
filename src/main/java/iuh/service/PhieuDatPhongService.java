/*
 * @ (#) PhieuDatPhongService.java     1.0    4/29/2026
 *
 * Copyright (c) 2026 IUH. All rights reserved.
 */
package iuh.service;

import iuh.entity.PhieuDatPhong;
import iuh.entity.TrangThaiPhieuDatPhong;

import java.util.List;

/*
 * @description
 * @author:NguyenTruong
 * @date:  4/29/2026
 * @version:    1.0
 */
public interface PhieuDatPhongService {
    List<PhieuDatPhong> getAll();

    List<PhieuDatPhong> getByTrangThai(TrangThaiPhieuDatPhong trangThai);

    PhieuDatPhong getByMaPhieu(String maPhieu);

    List<PhieuDatPhong> getPhieuDatPhongByToPayment(TrangThaiPhieuDatPhong status, String cccd);

    boolean updateTrangThai(String maPhieu, TrangThaiPhieuDatPhong trangThai);
}
