package iuh.service;

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
}
