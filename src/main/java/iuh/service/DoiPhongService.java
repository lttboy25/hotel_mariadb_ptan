/*
 * @ (#) DoiPhongService.java     1.0    4/29/2026
 *
 * Copyright (c) 2026 IUH. All rights reserved.
 */
package iuh.service;

import iuh.dto.PhongDTO;
import iuh.entity.Phong;

import java.util.List;

/*
 * @description
 * @author:NguyenTruong
 * @date:  4/29/2026
 * @version:    1.0
 */
public interface DoiPhongService {
    //lay phong da dat
    List<PhongDTO> getBookedRooms(String maPDP);

    //lay phong trong
    List<PhongDTO> getAvailableRooms();

    // doi phong
    void doiPhong(String maPDP, String maPhongCu, String maPhongMoi);

    List<PhongDTO> getAllBookedRooms();

    double tinhPhiChenhLech(String maPhongCu, String maPhongMoi);

    // DoiPhongServiceImpl.java
    String getMaPDPByPhong(String maPhong);
}
