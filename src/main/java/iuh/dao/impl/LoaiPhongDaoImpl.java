package iuh.dao.impl;

import java.util.List;

import iuh.entity.LoaiPhong;

public class LoaiPhongDaoImpl extends AbstractGenericDaoImpl<LoaiPhong, String> implements iuh.dao.LoaiPhongDao {

    public LoaiPhongDaoImpl() {
        super(LoaiPhong.class);
    }

    @Override
    public List<LoaiPhong> findAll() {
        return loadAll();
    }
}
