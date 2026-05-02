package iuh.service.impl;

import iuh.dao.impl.KhachHangDaoImpl;
import iuh.dto.KhachHangDTO;
import iuh.entity.KhachHang;
import iuh.mapper.Mapper;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class KhachHangServiceImpl implements iuh.service.KhachHangService {
    private final KhachHangDaoImpl khachHangDao = new KhachHangDaoImpl();

    @Override
    public List<KhachHangDTO> loadAll() {
        List<KhachHang> entities = khachHangDao.loadAll();
        if (entities == null)
            return java.util.Collections.emptyList();
        return entities
                .stream()
                .map(Mapper::map)
                .collect(Collectors.toList());
    }

    @Override
    public boolean themKhachHang(KhachHangDTO kh) {
        if (kh == null)
            return false;
        if (kh.getNgayTao() == null)
            kh.setNgayTao(LocalDate.now());
        KhachHang entity = Mapper.map(kh);
        return khachHangDao.create(entity) != null;
    }

    @Override
    public boolean capNhatKhachHang(KhachHangDTO kh) {
        if (kh == null)
            return false;
        KhachHang entity = Mapper.map(kh);
        return khachHangDao.update(entity) != null;
    }

    @Override
    public boolean xoaKhachHang(String maKH) {
        return khachHangDao.delete(maKH);
    }

    @Override
    public List<KhachHangDTO> timKiem(String kw) {
        List<KhachHang> entities = khachHangDao.findByKeyword(kw);
        if (entities == null)
            return java.util.Collections.emptyList();
        return entities
                .stream()
                .map(Mapper::map)
                .collect(Collectors.toList());
    }

    @Override
    public String phatSinhMaMoi() {
        return khachHangDao.generateNextMaKH();
    }

    @Override
    public KhachHangDTO timTheoCCCD(String cccd) {
        return Mapper.map(khachHangDao.findByCCCD(cccd));
    }

    @Override
    public KhachHangDTO findByCCCD(String cccd) {
        return timTheoCCCD(cccd);
    }

    @Override
    public boolean create(KhachHangDTO dto) {
        return themKhachHang(dto);
    }

    @Override
    public boolean update(KhachHangDTO dto) {
        return capNhatKhachHang(dto);
    }

    @Override
    public String generateNextMaKH() {
        return phatSinhMaMoi();
    }
}
