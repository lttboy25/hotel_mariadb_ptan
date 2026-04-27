package iuh.dao;

import iuh.dto.ChiTietHoaDonDTO;
import iuh.entity.ChiTietHoaDon;
import iuh.entity.Phong;
import iuh.mapper.Mapper;

public class ChiTietHoaDonDao extends AbstractGenericDaoImpl<ChiTietHoaDon, String> {
    public ChiTietHoaDonDao() {super(ChiTietHoaDon.class);}
    private Mapper mapper;

    public ChiTietHoaDon save(ChiTietHoaDonDTO chiTietHoaDonDTO) {

        return create(mapper.map(chiTietHoaDonDTO));
    }

}
