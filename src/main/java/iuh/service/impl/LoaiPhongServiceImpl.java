package iuh.service.impl;

import java.util.List;

import iuh.dao.impl.LoaiPhongDaoImpl;
import iuh.dto.LoaiPhongDTO;
import iuh.entity.LoaiPhong;
import iuh.mapper.Mapper;

public class LoaiPhongServiceImpl implements iuh.service.i.LoaiPhongService, iuh.service.LoaiPhongService {
    private LoaiPhongDaoImpl loaiPhongDao = new LoaiPhongDaoImpl();

    @Override
    public List<LoaiPhongDTO> getAll() {
        return loaiPhongDao.findAll().stream().map(
                e->
                    LoaiPhongDTO.
                            builder()
                            .maLoaiPhong(e.getMaLoaiPhong())
                            .tenLoaiPhong(e.getTenLoaiPhong())
                            .gia(e.getGia())
                            .soNguoiLonToiDa(e.getSoNguoiLonToiDa())
                            .soTreEmToiDa(e.getSoTreEmToiDa())
                            .ngayTao(e.getNgayTao())
                            .build()
        ).toList();
    }
}
