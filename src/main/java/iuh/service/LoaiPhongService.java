/*
 * @ (#) LoaiPhongService.java     1.0    4/29/2026
 *
 * Copyright (c) 2026 IUH. All rights reserved.
 */
package iuh.service;

import iuh.dto.LoaiPhongDTO;
import iuh.entity.LoaiPhong;

import java.util.List;

/*
 * @description
 * @author:NguyenTruong
 * @date:  4/29/2026
 * @version:    1.0
 */
public interface LoaiPhongService {
    List<LoaiPhongDTO> getAll();
}
