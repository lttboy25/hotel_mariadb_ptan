/*
 * @ (#) KhuyenMaiService.java     1.0    4/29/2026
 *
 * Copyright (c) 2026 IUH. All rights reserved.
 */
package iuh.service;

import iuh.dto.KhuyenMaiDTO;

import java.util.List;
import java.util.Optional;

/*
 * @description
 * @author:NguyenTruong
 * @date:  4/29/2026
 * @version:    1.0
 */
public interface KhuyenMaiService {
    List<KhuyenMaiDTO> getAllKhuyenMai();

    List<KhuyenMaiDTO> searchKhuyenMai(String keyword);

    Optional<KhuyenMaiDTO> getKhuyenMaiById(String maKhuyenMai);

    List<KhuyenMaiDTO> getKhuyenMaiByTrangThai(TrangThai trangThai);

    String generateNextMaKhuyenMai();

    KhuyenMaiDTO addKhuyenMai(KhuyenMaiDTO dto);

    KhuyenMaiDTO addKhuyenMaiAutoCode(KhuyenMaiDTO dto);

    KhuyenMaiDTO updateKhuyenMai(KhuyenMaiDTO dto);

    boolean deleteKhuyenMai(String maKhuyenMai);
}
