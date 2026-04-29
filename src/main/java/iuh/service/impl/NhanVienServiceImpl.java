package iuh.service.impl;

import iuh.dao.impl.NhanVienDaoImpl;
import iuh.dto.NhanVienDTO;
import iuh.entity.NhanVien;
import iuh.entity.TaiKhoan;

import java.util.List;
import java.util.Optional;

public class NhanVienServiceImpl implements iuh.service.NhanVienService {

    private final NhanVienDaoImpl nhanVienDao = new NhanVienDaoImpl();

    @Override
    public List<NhanVien> getAllNhanVien() {
        return nhanVienDao.findAll();
    }

    @Override
    public Optional<NhanVien> getNhanVienById(String maNhanVien) {
        return nhanVienDao.findById(maNhanVien);
    }

    @Override
    public List<NhanVien> searchNhanVienByName(String name) {
        return nhanVienDao.findByName(name);
    }

    @Override
    public NhanVien createNhanVien(NhanVien nhanVien) {
        if (nhanVien == null) {
            throw new IllegalArgumentException("NhanVien khong duoc null");
        }
        return nhanVienDao.save(nhanVien);
    }

    @Override
    public NhanVien updateNhanVien(NhanVien nhanVien) {
        if (nhanVien == null || nhanVien.getMaNhanVien() == null) {
            throw new IllegalArgumentException("Du lieu khong hop le");
        }
        return nhanVienDao.update(nhanVien);
    }

    @Override
    public boolean deleteNhanVien(String maNhanVien) {
        if (maNhanVien == null || maNhanVien.isEmpty()) {
            return false;
        }
        return nhanVienDao.delete(maNhanVien);
    }

    @Override
    public List<NhanVien> searchNhanVien(String keyword) {
        return nhanVienDao.search(keyword);
    }

    @Override
    public String generateNextMaNhanVien() {
        return nhanVienDao.generateMaNV();
    }

    @Override
    public NhanVienDTO xacThucDangNhap(String maNhanVien, String matKhau) {
        if (maNhanVien == null || matKhau == null ||
                maNhanVien.isEmpty() || matKhau.isEmpty()) {
            return null;
        }

        NhanVien nv = nhanVienDao.login(maNhanVien, matKhau);
        return nv != null ? mapToDTO(nv) : null;
    }

    @Override
    public NhanVienDTO getNhanVienDTOById(String maNhanVien) {
        if (maNhanVien == null || maNhanVien.isBlank()) {
            return null;
        }

        return nhanVienDao.findById(maNhanVien)
                .map(this::mapToDTO)
                .orElse(null);
    }

    @Override
    public NhanVien addNhanVienAutoCode(NhanVien nhanVien) {
        for (int i = 0; i < 10; i++) {
            String maNhanVien = nhanVienDao.generateMaNV();

            if (nhanVienDao.existsById(maNhanVien)) {
                continue;
            }

            nhanVien.setMaNhanVien(maNhanVien);
            String matKhauMacDinh = taoMatKhauMacDinh(maNhanVien);
            TaiKhoan taiKhoan = TaiKhoan.builder()
                    .maNhanVien(maNhanVien)
                    .matKhau(matKhauMacDinh)
                    .vaiTro(VAI_TRO_MAC_DINH)
                    .nhanVien(nhanVien)
                    .build();
            nhanVien.setTaiKhoan(taiKhoan);
            return nhanVienDao.save(nhanVien);
        }

        throw new IllegalStateException("Khong the tao ma nhan vien duy nhat. Vui long thu lai.");
    }

    @Override
    public boolean doiMatKhau(String maNhanVien, String matKhauCu, String matKhauMoi) {
        if (maNhanVien == null || maNhanVien.isBlank()) {
            throw new IllegalArgumentException("Ma nhan vien khong hop le.");
        }
        if (matKhauCu == null || matKhauCu.isBlank()) {
            throw new IllegalArgumentException("Mat khau hien tai khong duoc de trong.");
        }
        if (matKhauMoi == null || matKhauMoi.isBlank()) {
            throw new IllegalArgumentException("Mat khau moi khong duoc de trong.");
        }
        if (matKhauMoi.length() < 6) {
            throw new IllegalArgumentException("Mat khau moi phai co it nhat 6 ky tu.");
        }
        if (matKhauMoi.equals(matKhauCu)) {
            throw new IllegalArgumentException("Mat khau moi phai khac mat khau hien tai.");
        }
        return nhanVienDao.doiMatKhau(maNhanVien, matKhauCu, matKhauMoi);
    }

    @Override
    public String layMatKhauHienTai(String maNhanVien) {
        if (maNhanVien == null || maNhanVien.isBlank()) {
            return null;
        }
        return nhanVienDao.layMatKhauTheoMaNhanVien(maNhanVien);
    }

    @Override
    public String taoMatKhauMacDinh(String maNhanVien) {
        return maNhanVien;
    }
}
