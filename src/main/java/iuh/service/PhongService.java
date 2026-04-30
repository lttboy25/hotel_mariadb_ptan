/*
 * @ (#) PhongService.java     1.0    4/29/2026
 *
 * Copyright (c) 2026 IUH. All rights reserved.
 */
package iuh.service;

import iuh.entity.Phong;
import iuh.entity.TinhTrangPhong;
import iuh.entity.TrangThaiPhong;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/*
 * @description
 * @author:NguyenTruong
 * @date:  4/29/2026
 * @version:    1.0
 */
public interface PhongService {
    List<Phong> getAllRoom();

    Optional<Phong> getRoomById(String maPhong);

    List<Phong> getRoomByKeyword(String keyword);

    Phong createPhong(Phong phong);

    Phong updatePhong(Phong phong);

    boolean deletePhong(String maPhong);

    boolean checkNull(Phong phong);

    List<Integer> getAllTang();

    List<TinhTrangPhong> getAllTinhTrang();

    List<TrangThaiPhong> getAllTrangThai();

    List<Phong> getPhongByDate(LocalDateTime ngayNhan, LocalDateTime ngayTra);

    List<Phong> getRoomsByStatus(TinhTrangPhong status);

    boolean updateStatusRoom(String maPhong, TrangThaiPhong trangThai, TinhTrangPhong tinhTrang);
}
