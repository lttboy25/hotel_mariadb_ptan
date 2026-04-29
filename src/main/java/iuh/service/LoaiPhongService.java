package iuh.service;

import java.util.List;

import iuh.dao.impl.LoaiPhongDaoImpl;
import iuh.entity.LoaiPhong;

public class LoaiPhongService {
    private LoaiPhongDaoImpl loaiPhongDao = new LoaiPhongDaoImpl();

    public List<LoaiPhong> getAll() {
        return loaiPhongDao.findAll();
    }
}
