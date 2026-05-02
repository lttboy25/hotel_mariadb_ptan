package iuh.service.impl;

import iuh.dao.impl.NhanVienDaoImpl;
import iuh.dto.NhanVienDTO;
import iuh.dto.TaiKhoanDTO;
import iuh.entity.NhanVien;
import iuh.entity.TaiKhoan;

import java.util.List;
import java.util.Optional;

public class NhanVienServiceImpl implements iuh.service.NhanVienService {

    private final NhanVienDaoImpl nhanVienDao = new NhanVienDaoImpl();

    @Override
    public List<NhanVienDTO> getAllNhanVien() {
        return nhanVienDao.findAll()
                .stream()
                .map(e -> NhanVienDTO.builder()
                        .maNhanVien(e.getMaNhanVien())
                        .tenNhanVien(e.getTenNhanVien())
                        .CCCD(e.getCCCD())
                        .gioiTinh(e.isGioiTinh())
                        .ngaySinh(e.getNgaySinh())
                        .email(e.getEmail())
                        .soDienThoai(e.getSoDienThoai())
                        .ngayBatDau(e.getNgayBatDau())
                        .diaChi(e.getDiaChi())
                        .trangThai(e.getTrangThai())
                        .taiKhoan(String.valueOf(e.getTaiKhoan()))
                        .build())
                .toList();
    }

    @Override
    public Optional<NhanVienDTO> getNhanVienById(String maNhanVien) {
        return nhanVienDao.findById(maNhanVien)
                .map(e -> NhanVienDTO.builder()
                        .maNhanVien(e.getMaNhanVien())
                        .tenNhanVien(e.getTenNhanVien())
                        .CCCD(e.getCCCD())
                        .gioiTinh(e.isGioiTinh())
                        .ngaySinh(e.getNgaySinh())
                        .email(e.getEmail())
                        .soDienThoai(e.getSoDienThoai())
                        .ngayBatDau(e.getNgayBatDau())
                        .diaChi(e.getDiaChi())
                        .trangThai(e.getTrangThai())
                        .taiKhoan(String.valueOf(e.getTaiKhoan()))
                        .build());
    }

    @Override
    public List<NhanVienDTO> searchNhanVienByName(String name) {
        return nhanVienDao.findByName(name)
                .stream()
                .map(e -> NhanVienDTO.builder()
                        .maNhanVien(e.getMaNhanVien())
                        .tenNhanVien(e.getTenNhanVien())
                        .CCCD(e.getCCCD())
                        .gioiTinh(e.isGioiTinh())
                        .ngaySinh(e.getNgaySinh())
                        .email(e.getEmail())
                        .soDienThoai(e.getSoDienThoai())
                        .ngayBatDau(e.getNgayBatDau())
                        .diaChi(e.getDiaChi())
                        .trangThai(e.getTrangThai())
                        .taiKhoan(String.valueOf(e.getTaiKhoan()))
                        .build())
                .toList();
    }

    @Override
    public NhanVienDTO createNhanVien(NhanVienDTO nhanVien) {
        if (nhanVien == null)
            throw new IllegalArgumentException("NhanVien khong duoc null");
        TaiKhoan tk = TaiKhoan.builder()
                .maNhanVien(nhanVien.getMaNhanVien())
                .matKhau(nhanVien.getTaiKhoan())
                .vaiTro(nhanVien.getTaiKhoan().)
                .build();
        NhanVienDTO nv = NhanVien.builder()
                .maNhanVien(nhanVien.getMaNhanVien())
                .tenNhanVien(nhanVien.getTenNhanVien())
                .CCCD(nhanVien.getCCCD())
                .gioiTinh(nhanVien.isGioiTinh())
                .ngaySinh(nhanVien.getNgaySinh())
                .email(nhanVien.getEmail())
                .soDienThoai(nhanVien.getSoDienThoai())
                .ngayBatDau(nhanVien.getNgayBatDau())
                .diaChi(nhanVien.getDiaChi())
                .trangThai(nhanVien.getTrangThai())
                .taiKhoan(nhanVien.getTaiKhoan())
                .build();
        return nhanVienDao.save(nv);
    }

    @Override
    public NhanVienDTO updateNhanVien(NhanVienDTO nhanVien) {
        if (nhanVien == null || nhanVien.getMaNhanVien() == null)
            throw new IllegalArgumentException("Du lieu khong hop le");
        return nhanVienDao.update(nhanVien);
    }

    @Override
    public boolean deleteNhanVien(String maNhanVien) {
        if (maNhanVien == null || maNhanVien.isEmpty()) return false;
        return nhanVienDao.delete(maNhanVien);
    }

    @Override
    public List<NhanVienDTO> searchNhanVien(String keyword) {
        return nhanVienDao.search(keyword);
    }

    @Override
    public String generateNextMaNhanVien() {
        return nhanVienDao.generateMaNV();
    }

    @Override
    public NhanVienDTO xacThucDangNhap(String maNhanVien, String matKhau) {
        if (maNhanVien == null || matKhau == null ||
                maNhanVien.isEmpty() || matKhau.isEmpty()) return null;
        return nhanVienDao.login(maNhanVien, matKhau);
    }

    @Override
    public NhanVienDTO getNhanVienDTOById(String maNhanVien) {
        if (maNhanVien == null || maNhanVien.isBlank()) return null;
        return nhanVienDao.findById(maNhanVien).orElse(null);
    }

    @Override
    public NhanVienDTO addNhanVienAutoCode(NhanVienDTO nhanVien) {
        validateNhanVien(nhanVien);
        validateDuplicate(nhanVien, false);

        for (int i = 0; i < 10; i++) {
            String maNhanVien = nhanVienDao.generateMaNV();

            if (nhanVienDao.existsById(maNhanVien)) {
                continue;
            }

            nhanVien.setMaNhanVien(maNhanVien);
            // ✅ Không tạo TaiKhoan entity ở đây — để server/dao xử lý
            return nhanVienDao.save(nhanVien);
        }

        throw new IllegalStateException("Khong the tao ma nhan vien duy nhat.");
    }

    @Override
    public boolean doiMatKhau(String maNhanVien, String matKhauCu, String matKhauMoi) {
        if (maNhanVien == null || maNhanVien.isBlank())
            throw new IllegalArgumentException("Ma nhan vien khong hop le.");
        if (matKhauCu == null || matKhauCu.isBlank())
            throw new IllegalArgumentException("Mat khau hien tai khong duoc de trong.");
        if (matKhauMoi == null || matKhauMoi.isBlank())
            throw new IllegalArgumentException("Mat khau moi khong duoc de trong.");
        if (matKhauMoi.length() < 6)
            throw new IllegalArgumentException("Mat khau moi phai co it nhat 6 ky tu.");
        if (matKhauMoi.equals(matKhauCu))
            throw new IllegalArgumentException("Mat khau moi phai khac mat khau hien tai.");
        return nhanVienDao.doiMatKhau(maNhanVien, matKhauCu, matKhauMoi);
    }

    @Override
    public String layMatKhauHienTai(String maNhanVien) {
        if (maNhanVien == null || maNhanVien.isBlank()) return null;
        return nhanVienDao.layMatKhauTheoMaNhanVien(maNhanVien);
    }

    @Override
    public String taoMatKhauMacDinh(String maNhanVien) {
        return maNhanVien;
    }

    private void validateNhanVien(NhanVienDTO nv) {
        if (nv == null)
            throw new IllegalArgumentException("Du lieu nhan vien khong duoc null.");
        if (nv.getTenNhanVien() == null || nv.getTenNhanVien().isBlank())
            throw new IllegalArgumentException("Ten nhan vien khong duoc de trong.");
        if (nv.getCCCD() == null || nv.getCCCD().isBlank())
            throw new IllegalArgumentException("CCCD khong duoc de trong.");
        if (nv.getSoDienThoai() == null || nv.getSoDienThoai().isBlank())
            throw new IllegalArgumentException("So dien thoai khong duoc de trong.");
        if (nv.getEmail() == null || nv.getEmail().isBlank())
            throw new IllegalArgumentException("Email khong duoc de trong.");
    }

    public void validateDuplicate(NhanVienDTO nv, boolean isUpdate) {
        boolean isCCCDExist = getAllNhanVien().stream()
                .anyMatch(x -> x.getCCCD().equals(nv.getCCCD())
                        && (!isUpdate || !x.getMaNhanVien().equals(nv.getMaNhanVien())));
        if (isCCCDExist)
            throw new IllegalArgumentException("CCCD đã tồn tại");

        boolean isEmailExist = getAllNhanVien().stream()
                .anyMatch(x -> x.getEmail().equals(nv.getEmail())
                        && (!isUpdate || !x.getMaNhanVien().equals(nv.getMaNhanVien())));
        if (isEmailExist)
            throw new IllegalArgumentException("Email đã tồn tại");
    }
}