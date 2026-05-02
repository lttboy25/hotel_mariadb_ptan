/*
 * @ (#) NhanVienDao.java     1.0    4/29/2026
 *
 * Copyright (c) 2026 IUH. All rights reserved.
 */
package iuh.dao;

import iuh.entity.NhanVien;

import java.util.List;
import java.util.Optional;

/*
 * @description
 * @author:NguyenTruong
 * @date:  4/29/2026
 * @version:    1.0
 */
public interface NhanVienDao {
    List<NhanVien> findAll();

    Optional<NhanVien> findById(String maNhanVien);

    List<NhanVien> findByName(String name);

    NhanVien save(NhanVien nhanVien);

    NhanVien update(NhanVien nhanVien);

    boolean delete(String maNhanVien);

    List<NhanVien> search(String keyword);

    String generateMaNV();

    default int extractNumber(String code) {
        if (code == null) return 0;

        try {
            return Integer.parseInt(code.replaceAll("\\D+", ""));
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    default String formatMaNV(int number) {
        return String.format("NV%03d", number);
    }

    boolean existsById(String maNhanVien);

    NhanVien login(String maNV, String matKhau);

    boolean doiMatKhau(String maNV, String matKhauCu, String matKhauMoi);

    String layMatKhauTheoMaNhanVien(String maNV);
}
