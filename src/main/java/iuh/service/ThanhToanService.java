/*
 * @ (#) ThanhToanService.java     1.0    4/29/2026
 *
 * Copyright (c) 2026 IUH. All rights reserved.
 */
package iuh.service;

import iuh.entity.ChiTietPhieuDatPhong;
import iuh.entity.HoaDon;
import iuh.entity.Phong;

import java.util.List;

/*
 * @description
 * @author:NguyenTruong
 * @date:  4/29/2026
 * @version:    1.0
 */
public interface ThanhToanService {
    List<Phong> getRoomsByStatus(String status);

    List<ChiTietPhieuDatPhong> getDanhSachPhieuDatPhongDeThanhToan(String cccd);

    boolean coTheThanhToan(double tienKhachDua, double tongTien);

    HoaDon thanhToan(List<ChiTietPhieuDatPhong> listThanhToan, double tienKhachDua, double tienThua);
}
