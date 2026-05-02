/*
 * @ (#) ThanhToanService.java     1.0    4/29/2026
 *
 * Copyright (c) 2026 IUH. All rights reserved.
 */
package iuh.service;

import iuh.dto.ChiTietPhieuDatPhongDTO;
import iuh.dto.HoaDonDTO;
import iuh.dto.KhuyenMaiDTO;
import iuh.dto.PhongDTO;
import iuh.entity.ChiTietPhieuDatPhong;
import iuh.entity.HoaDon;
import iuh.entity.Phong;
import iuh.enums.TinhTrangPhong;

import java.util.List;

/*
 * @description
 * @author:NguyenTruong
 * @date:  4/29/2026
 * @version:    1.0
 */
public interface ThanhToanService {

    List<ChiTietPhieuDatPhongDTO> getDanhSachPhieuDatPhongDeThanhToan(String cccd);

    boolean coTheThanhToan(double tienKhachDua, double tongTien);

    HoaDonDTO thanhToan(List<ChiTietPhieuDatPhongDTO> listThanhToan, double tienKhachDua, double tienThua);

    List<KhuyenMaiDTO> getDsKhuyenMai();

    double tienSauKhiApGiamGia(double tongTien, KhuyenMaiDTO KhuyenMai);


}
