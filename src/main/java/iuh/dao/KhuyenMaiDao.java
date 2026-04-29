/*
 * @ (#) KhuyenMaiDao.java     1.0    4/29/2026
 *
 * Copyright (c) 2026 IUH. All rights reserved.
 */
package iuh.dao;

import iuh.entity.KhuyenMai;
import iuh.entity.TrangThai;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

/*
 * @description
 * @author:NguyenTruong
 * @date:  4/29/2026
 * @version:    1.0
 */
public interface KhuyenMaiDao {
    Pattern MA_KHUYEN_MAI_PATTERN = Pattern.compile("^KM(\\d+)$");

    List<KhuyenMai> findAll();

    Optional<KhuyenMai> findById(String maKhuyenMai);

    boolean existsById(String maKhuyenMai);

    String generateNextMaKhuyenMai();

    KhuyenMai save(KhuyenMai khuyenMai);

    List<KhuyenMai> findByKeyword(String keyword);

    List<KhuyenMai> findByTrangThai(TrangThai trangThai);
}
