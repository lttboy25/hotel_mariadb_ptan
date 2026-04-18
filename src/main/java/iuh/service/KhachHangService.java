package iuh.service;

import iuh.dao.KhachHangDao;
import iuh.entity.KhachHang;
import java.util.List;

public class KhachHangService {
    private final KhachHangDao khachHangDao = new KhachHangDao();

    public List<KhachHang> loadAll() {
        return khachHangDao.loadAll();
    }

    public boolean themKhachHang(KhachHang kh) {
        return khachHangDao.create(kh) != null;
    }

    public boolean capNhatKhachHang(KhachHang kh) {
        return khachHangDao.update(kh) != null;
    }

    public boolean xoaKhachHang(String maKH) {
        return khachHangDao.delete(maKH);
    }

    public List<KhachHang> timKiem(String kw) {
        return khachHangDao.findByKeyword(kw);
    }

    public String phatSinhMaMoi() {
        return khachHangDao.generateNextMaKH();
    }
}