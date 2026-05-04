/*
 * @ (#) NhanPhongService.java     1.0    4/29/2026
 *
 * Copyright (c) 2026 IUH. All rights reserved.
 */
package iuh.service;

import iuh.dto.ChiTietPhieuDatPhongDTO;
import iuh.entity.ChiTietPhieuDatPhong;

import java.util.List;

/*
 * @description
 * @author:NguyenTruong
 * @date:  4/29/2026
 * @version:    1.0
 */
public interface NhanPhongService {
    List<ChiTietPhieuDatPhongDTO> getDanhSachPhongDeNhanByCCCD(String cccd);
    
    boolean nhanPhong(List<ChiTietPhieuDatPhongDTO> listNhanPhong);
}
