package iuh.dao;

import java.util.List;

import iuh.entity.LoaiPhong;

public class LoaiPhongDao extends AbstractGenericDaoImpl<LoaiPhong, String> {

    public LoaiPhongDao() {
        super(LoaiPhong.class);
    }

    public List<LoaiPhong> findAll() {
        return loadAll();
    }
}
