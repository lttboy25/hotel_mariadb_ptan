/*
 * @ (#) KhuyenMaiDao.java     1.0    4/29/2026
 *
 * Copyright (c) 2026 IUH. All rights reserved.
 */
package iuh.dao;

import iuh.entity.KhuyenMai;
import iuh.enums.TrangThai;

import java.util.List;
import java.util.Optional;

/*
 * @description
 * @author:NguyenTruong
 * @date:  4/29/2026
 * @version:    1.0
 */
public interface KhuyenMaiDao {
    List<KhuyenMai> findAll();

    Optional<KhuyenMai> findById(String maKhuyenMai);

    boolean existsById(String maKhuyenMai);

    String generateNextMaKhuyenMai();

    KhuyenMai save(KhuyenMai khuyenMai);

    List<KhuyenMai> findByKeyword(String keyword);

    List<KhuyenMai> findByTrangThai(TrangThai trangThai);

    default int extractNumber(String code) {
        if (code == null) return 0;
        try {
            return Integer.parseInt(code.replaceAll("\\D+", ""));
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    default String formatCode(int number) {
        return String.format("KM%03d", number);
    }
}
