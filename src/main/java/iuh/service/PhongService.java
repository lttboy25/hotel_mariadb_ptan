package iuh.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import iuh.dao.PhongDao;
import iuh.entity.Phong;

public class PhongService {
    private PhongDao phongDao = new PhongDao();

    public List<Phong> getAllRoom() {
        return phongDao.findAll();
    }

    public Optional<Phong> getRoomById(String maPhong) {
        return phongDao.findById(maPhong);
    }

    public List<Phong> getRoomByKeyword(String keyword) {
        return phongDao.findByKeyword(keyword);
    }

    public Phong createPhong(Phong phong) {
        return phongDao.save(phong);
    }

    public Phong updatePhong(Phong phong) {
        return phongDao.updateRoom(phong);
    }

    public boolean deletePhong(String maPhong) {
        return phongDao.deleteRoom(maPhong);
    }

    public boolean checkNull(Phong phong) {
        if (phong.getTang() == 0 || phong.getLoaiPhong() == null || phong.getTinhTrang() == null
                || phong.getTrangThai() == null) {
            return false;
        }
        return true;
    }

    public List<Integer> getAllTang() {
        return getAllRoom()
                .stream()
                .map(p -> p.getTang())
                .distinct()
                .toList();
    }

    public List<String> getAllTinhTrang() {
        return getAllRoom()
                .stream()
                .map(p -> p.getTinhTrang())
                .distinct()
                .toList();
    }

    public List<String> getAllTrangThai() {
        return getAllRoom()
                .stream()
                .map(p -> p.getTrangThai())
                .distinct()
                .toList();
    }
}
