package iuh.service.impl;

import java.util.List;

import iuh.dao.impl.LoaiPhongDaoImpl;
import iuh.entity.LoaiPhong;

public class LoaiPhongServiceImpl implements iuh.service.i.LoaiPhongService, iuh.service.LoaiPhongService {
    private LoaiPhongDaoImpl loaiPhongDao = new LoaiPhongDaoImpl();

    @Override
    public List<LoaiPhong> getAll() {
        return loaiPhongDao.findAll();
    }
}
