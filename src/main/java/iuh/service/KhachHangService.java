package iuh.service;

import iuh.dao.KhachHangDao;
import iuh.dto.KhachHangDTO;
import iuh.entity.KhachHang;
import iuh.mapper.Mapper;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class KhachHangService {
    private final KhachHangDao khachHangDao = new KhachHangDao();

    public List<KhachHangDTO> loadAll() {
        return khachHangDao.loadAll()
                .stream()
                .map(Mapper::map)
                .collect(Collectors.toList());
    }

    public boolean themKhachHang(KhachHangDTO kh) {
        if (kh == null) return false;
        if (kh.getNgayTao() == null) kh.setNgayTao(LocalDate.now());
        KhachHang entity = Mapper.map(kh);
        return khachHangDao.create(entity) != null;
    }

    public boolean capNhatKhachHang(KhachHangDTO kh) {
        if (kh == null) return false;
        KhachHang entity = Mapper.map(kh);
        return khachHangDao.update(entity) != null;
    }

    public boolean xoaKhachHang(String maKH) {
        return khachHangDao.delete(maKH);
    }

    public List<KhachHangDTO> timKiem(String kw) {
        return khachHangDao.findByKeyword(kw)
                .stream()
                .map(Mapper::map)
                .collect(Collectors.toList());
    }

    public String phatSinhMaMoi() {
        return khachHangDao.generateNextMaKH();
    }

    public KhachHangDTO timTheoCCCD(String cccd) {
        return Mapper.map(khachHangDao.findByCCCD(cccd));
    }

    public KhachHangDTO findByCCCD(String cccd) { return timTheoCCCD(cccd); }
    public boolean create(KhachHangDTO dto) { return themKhachHang(dto); }
    public boolean update(KhachHangDTO dto) { return capNhatKhachHang(dto); }
    public String generateNextMaKH() { return phatSinhMaMoi(); }
}
