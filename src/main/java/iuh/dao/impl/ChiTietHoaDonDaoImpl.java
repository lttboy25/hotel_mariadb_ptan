package iuh.dao.impl;

import iuh.dto.ChiTietHoaDonDTO;
import iuh.entity.ChiTietHoaDon;
import iuh.mapper.Mapper;

public class ChiTietHoaDonDaoImpl extends AbstractGenericDaoImpl<ChiTietHoaDon, String> implements iuh.dao.ChiTietHoaDonDao {
    public ChiTietHoaDonDaoImpl() {super(ChiTietHoaDon.class);}

    @Override
    public ChiTietHoaDon save(ChiTietHoaDonDTO chiTietHoaDonDTO) {

        return create(Mapper.map(chiTietHoaDonDTO));
    }

}
