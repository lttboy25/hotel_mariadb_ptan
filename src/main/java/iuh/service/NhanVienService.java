/*
 * @ (#) NhanVienService.java     1.0    4/29/2026
 *
 * Copyright (c) 2026 IUH. All rights reserved.
 */
package iuh.service;

import iuh.dto.NhanVienDTO;
import iuh.entity.NhanVien;

import java.util.List;
import java.util.Optional;

/*
 * @description
 * @author:NguyenTruong
 * @date:  4/29/2026
 * @version:    1.0
 */
public interface NhanVienService {
    String VAI_TRO_MAC_DINH = "employee";

    List<NhanVien> getAllNhanVien();

    Optional<NhanVien> getNhanVienById(String maNhanVien);

    List<NhanVien> searchNhanVienByName(String name);

    NhanVien createNhanVien(NhanVien nhanVien);

    NhanVien updateNhanVien(NhanVien nhanVien);

    boolean deleteNhanVien(String maNhanVien);

    List<NhanVien> searchNhanVien(String keyword);

    String generateNextMaNhanVien();

    NhanVienDTO xacThucDangNhap(String maNhanVien, String matKhau);

    NhanVienDTO getNhanVienDTOById(String maNhanVien);

    default NhanVienDTO mapToDTO(NhanVien nv) {
        return NhanVienDTO.builder()
                .maNhanVien(nv.getMaNhanVien())
                .CCCD(nv.getCCCD())
                .tenNhanVien(nv.getTenNhanVien())
                .taiKhoan(nv.getTaiKhoan() != null ? nv.getTaiKhoan().getMaNhanVien() : "")
                .gioiTinh(nv.isGioiTinh())
                .ngaySinh(nv.getNgaySinh())
                .email(nv.getEmail())
                .soDienThoai(nv.getSoDienThoai())
                .ngayBatDau(nv.getNgayBatDau())
                .trangThai(nv.getTrangThai() != null ? nv.getTrangThai().getDisplay() : "")
                .diaChi(nv.getDiaChi())
                .build();
    }

    NhanVien addNhanVienAutoCode(NhanVien nhanVien);

    boolean doiMatKhau(String maNhanVien, String matKhauCu, String matKhauMoi);

    String layMatKhauHienTai(String maNhanVien);

    String taoMatKhauMacDinh(String maNhanVien);
}
