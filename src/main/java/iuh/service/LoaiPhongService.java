package iuh.service;

import java.util.List;

import iuh.dao.LoaiPhongDao;
import iuh.entity.LoaiPhong;

public class LoaiPhongService {
    private LoaiPhongDao loaiPhongDao = new LoaiPhongDao();

    public List<LoaiPhong> getAll() {
        return loaiPhongDao.findAll();
    }
}
