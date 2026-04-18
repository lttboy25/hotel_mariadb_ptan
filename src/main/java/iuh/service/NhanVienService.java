package iuh.service;

import iuh.dao.NhanVienDao;
import iuh.entity.NhanVien;

import java.util.List;
import java.util.Optional;

public class NhanVienService {

    private final NhanVienDao nhanVienDao = new NhanVienDao();

    public List<NhanVien> getAllNhanVien() {
        return nhanVienDao.findAll();
    }

    public Optional<NhanVien> getNhanVienById(String maNhanVien) {
        return nhanVienDao.findById(maNhanVien);
    }

    public List<NhanVien> searchNhanVienByName(String name) {
        return nhanVienDao.findByName(name);
    }

    public NhanVien createNhanVien(NhanVien nhanVien) {
        if (nhanVien == null) {
            throw new IllegalArgumentException("NhanVien không được null");
        }
        return nhanVienDao.save(nhanVien);
    }

    public NhanVien updateNhanVien(NhanVien nhanVien) {
        if (nhanVien == null || nhanVien.getMaNhanVien() == null) {
            throw new IllegalArgumentException("Dữ liệu không hợp lệ");
        }
        return nhanVienDao.update(nhanVien);
    }

    public boolean deleteNhanVien(String maNhanVien) {
        if (maNhanVien == null || maNhanVien.isEmpty()) {
            return false;
        }
        return nhanVienDao.delete(maNhanVien);
    }

    public List<NhanVien> searchNhanVien(String keyword) {
        return nhanVienDao.search(keyword);
    }

    public String generateNextMaNhanVien() {
        return nhanVienDao.generateMaNV();
    }

    public NhanVien addNhanVienAutoCode(NhanVien nhanVien) {
        for (int i = 0; i < 10; i++) {
            String maNhanVien = nhanVienDao.generateMaNV();

            if (nhanVienDao.existsById(maNhanVien)) {
                continue; // nếu trùng thì thử lại
            }

            nhanVien.setMaNhanVien(maNhanVien);
            return nhanVienDao.save(nhanVien);
        }

        throw new IllegalStateException("Không thể tạo mã nhân viên duy nhất. Vui lòng thử lại.");
    }

}