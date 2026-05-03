/*
 * @ (#) NhanVienService.java     1.0    4/29/2026
 *
 * Copyright (c) 2026 IUH. All rights reserved.
 */
package iuh.service;

import iuh.dto.NhanVienDTO;
import iuh.dto.TaiKhoanDTO;
import iuh.mapper.Mapper;

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
    Mapper mapper = new Mapper();

    default void validateNhanVien(NhanVienDTO nv, boolean isUpdate) {
        if (nv == null) {
            throw new IllegalArgumentException("Dữ liệu nhân viên không hợp lệ");
        }

        if (isBlank(nv.getTenNhanVien())) {
            throw new IllegalArgumentException("Tên nhân viên không được để trống");
        }

        if (isBlank(nv.getCCCD())) {
            throw new IllegalArgumentException("CCCD không được để trống");
        }

        if (isBlank(nv.getSoDienThoai())) {
            throw new IllegalArgumentException("Số điện thoại không được để trống");
        }

        if (isBlank(nv.getEmail())) {
            throw new IllegalArgumentException("Email không được để trống");
        }
    }

    default boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    List<NhanVienDTO> getAllNhanVien();

    Optional<NhanVienDTO> getNhanVienById(String maNhanVien);

    List<NhanVienDTO> searchNhanVienByName(String name);

    NhanVienDTO createNhanVien(NhanVienDTO nhanVien);

    NhanVienDTO updateNhanVien(NhanVienDTO nhanVien);

    boolean deleteNhanVien(String maNhanVien);

    List<NhanVienDTO> searchNhanVien(String keyword);

    String generateNextMaNhanVien();

    NhanVienDTO xacThucDangNhap(String maNhanVien, String matKhau);

    NhanVienDTO getNhanVienDTOById(String maNhanVien);

    default NhanVienDTO mapToDTO(NhanVienDTO nv) {
        TaiKhoanDTO taiKhoanDTO = new TaiKhoanDTO();
        if (nv != null) {
            taiKhoanDTO = TaiKhoanDTO.builder()
                    .maNhanVien(nv.getTaiKhoan().getMaNhanVien())
                    .matKhau(nv.getTaiKhoan().getMatKhau())
                    .vaiTro(nv.getTaiKhoan().getVaiTro())
                    .build();
        }

        return NhanVienDTO.builder()
                .maNhanVien(nv.getMaNhanVien())
                .CCCD(nv.getCCCD())
                .tenNhanVien(nv.getTenNhanVien())
                .taiKhoan(taiKhoanDTO)
                .gioiTinh(nv.isGioiTinh())
                .ngaySinh(nv.getNgaySinh())
                .email(nv.getEmail())
                .soDienThoai(nv.getSoDienThoai())
                .ngayBatDau(nv.getNgayBatDau())
                .trangThai(nv.getTrangThai())
                .diaChi(nv.getDiaChi())
                .build();
    }

    NhanVienDTO addNhanVienAutoCode(NhanVienDTO nhanVien);

    boolean doiMatKhau(String maNhanVien, String matKhauCu, String matKhauMoi);

    String layMatKhauHienTai(String maNhanVien);

    String taoMatKhauMacDinh(String maNhanVien);
}
