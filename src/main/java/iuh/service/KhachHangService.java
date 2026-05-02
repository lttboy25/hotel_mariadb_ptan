/*
 * @ (#) KhachHangService.java     1.0    4/29/2026
 *
 * Copyright (c) 2026 IUH. All rights reserved.
 */
package iuh.service;

import iuh.dto.KhachHangDTO;

import java.util.List;

/*
 * @description
 * @author:NguyenTruong
 * @date:  4/29/2026
 * @version:    1.0
 */
public interface KhachHangService {
    List<KhachHangDTO> loadAll();

    boolean themKhachHang(KhachHangDTO kh);

    boolean capNhatKhachHang(KhachHangDTO kh);

    boolean xoaKhachHang(String maKH);

    List<KhachHangDTO> timKiem(String kw);

    String phatSinhMaMoi();

    KhachHangDTO timTheoCCCD(String cccd);

    KhachHangDTO findByCCCD(String cccd);

    boolean create(KhachHangDTO dto);

    boolean update(KhachHangDTO dto);

    String generateNextMaKH();
}
