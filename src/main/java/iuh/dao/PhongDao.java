/*
 * @ (#) PhongDao.java     1.0    4/29/2026
 *
 * Copyright (c) 2026 IUH. All rights reserved.
 */
package iuh.dao;

import iuh.entity.Phong;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/*
 * @description
 * @author:NguyenTruong
 * @date:  4/29/2026
 * @version:    1.0
 */
public interface PhongDao {
    List<Phong> findAll();

    Optional<Phong> findById(String maPhong);

    List<Phong> findByKeyword(String keyword);

    String generateMaPhong(int tang);

    Phong save(Phong phong);

    String generateSoPhong(String maPhong);

    Phong updateRoom(Phong Phong);

    boolean deleteRoom(String maPhong);

    // lây phòng troongs
    List<Phong> getAvailableRooms();

    List<Phong> findPhongByDate(LocalDateTime ngayNhanPhong, LocalDateTime ngayTraPhong);

    List<Phong> getRoomsByStatus(String status);

    boolean updateStatusRoom(String maPhong, String trangThai, String tinhTrang);
}
