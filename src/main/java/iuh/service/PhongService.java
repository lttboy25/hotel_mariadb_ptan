/*
 * @ (#) PhongService.java     1.0    4/29/2026
 *
 * Copyright (c) 2026 IUH. All rights reserved.
 */
package iuh.service;

import iuh.dto.PhongDTO;
import iuh.enums.TinhTrangPhong;
import iuh.enums.TrangThaiPhong;
import iuh.entity.Phong;

import java.time.LocalDateTime;
import java.util.List;

/*
 * @description
 * @author:NguyenTruong
 * @date:  4/29/2026
 * @version:    1.0
 */
public interface PhongService {
    List<PhongDTO> getAllRoom();

    PhongDTO getRoomById(String maPhong);

    List<PhongDTO> getRoomByKeyword(String keyword);

    PhongDTO createPhong(PhongDTO phong);

    PhongDTO updatePhong(PhongDTO phong);

    boolean deletePhong(String maPhong);

    boolean checkNull(PhongDTO phong);

    List<Integer> getAllTang();

    List<TinhTrangPhong> getAllTinhTrang();

    List<TrangThaiPhong> getAllTrangThai();

    List<Phong> getPhongByDate(LocalDateTime ngayNhan, LocalDateTime ngayTra);

    List<Phong> getRoomsByStatus(TinhTrangPhong status);

    boolean updateStatusRoom(String maPhong, TrangThaiPhong trangThai, TinhTrangPhong tinhTrang);
}
