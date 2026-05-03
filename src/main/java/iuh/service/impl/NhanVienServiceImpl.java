package iuh.service.impl;

import iuh.dao.impl.NhanVienDaoImpl;
import iuh.dto.NhanVienDTO;
import iuh.dto.TaiKhoanDTO;
import iuh.entity.NhanVien;
import iuh.entity.TaiKhoan;
import iuh.enums.TrangThaiNhanVien;

import java.util.List;
import java.util.Optional;

public class NhanVienServiceImpl implements iuh.service.NhanVienService {

    private final NhanVienDaoImpl nhanVienDao = new NhanVienDaoImpl();
    private static final String VAI_TRO_MAC_DINH = "Nhân viên";

    // ==================== HELPER ====================

    private NhanVienDTO toDTO(NhanVien e) {
        if (e == null) return null;

        TaiKhoanDTO taiKhoanDTO = null;
        if (e.getTaiKhoan() != null) {
            taiKhoanDTO = TaiKhoanDTO.builder()
                    .maNhanVien(e.getTaiKhoan().getMaNhanVien())
                    .matKhau(e.getTaiKhoan().getMatKhau())
                    .vaiTro(e.getTaiKhoan().getVaiTro())
                    .build();
        }

        return NhanVienDTO.builder()
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
                .taiKhoan(taiKhoanDTO)
                .build();
    }

    private NhanVien toEntity(NhanVienDTO dto) {
        if (dto == null) return null;
        return NhanVien.builder()
                .maNhanVien(dto.getMaNhanVien())
                .tenNhanVien(dto.getTenNhanVien())
                .CCCD(dto.getCCCD())
                .gioiTinh(dto.isGioiTinh())
                .ngaySinh(dto.getNgaySinh())
                .email(dto.getEmail())
                .soDienThoai(dto.getSoDienThoai())
                .ngayBatDau(dto.getNgayBatDau())
                .diaChi(dto.getDiaChi())
                .trangThai(dto.getTrangThai())
                .build();
    }

    // ==================== SERVICE ====================

    @Override
    public List<NhanVienDTO> getAllNhanVien() {
        return nhanVienDao.findAll().stream()
                .map(this::toDTO)
                .toList();
    }

    @Override
    public Optional<NhanVienDTO> getNhanVienById(String maNhanVien) {
        return nhanVienDao.findById(maNhanVien).map(this::toDTO);
    }

    @Override
    public List<NhanVienDTO> searchNhanVienByName(String name) {
        return nhanVienDao.findByName(name).stream()
                .map(this::toDTO)
                .toList();
    }

    @Override
    public NhanVienDTO createNhanVien(NhanVienDTO nhanVien) {
        if (nhanVien == null)
            throw new IllegalArgumentException("NhanVien khong duoc null");
        return toDTO(nhanVienDao.save(toEntity(nhanVien)));
    }

    @Override
    public NhanVienDTO updateNhanVien(NhanVienDTO nhanVien) {
        if (nhanVien == null || nhanVien.getMaNhanVien() == null)
            throw new IllegalArgumentException("Du lieu khong hop le");
        return toDTO(nhanVienDao.update(toEntity(nhanVien)));
    }

    @Override
    public boolean deleteNhanVien(String maNhanVien) {
        if (maNhanVien == null || maNhanVien.isEmpty()) return false;
        return nhanVienDao.delete(maNhanVien);
    }

    @Override
    public List<NhanVienDTO> searchNhanVien(String keyword) {
        return nhanVienDao.search(keyword).stream()
                .map(this::toDTO)
                .toList();
    }

    @Override
    public String generateNextMaNhanVien() {
        return nhanVienDao.generateMaNV();
    }

    @Override
    public NhanVienDTO xacThucDangNhap(String maNhanVien, String matKhau) {
        if (maNhanVien == null || matKhau == null ||
                maNhanVien.isEmpty() || matKhau.isEmpty()) return null;
        return toDTO(nhanVienDao.login(maNhanVien, matKhau));
    }

    @Override
    public NhanVienDTO getNhanVienDTOById(String maNhanVien) {
        if (maNhanVien == null || maNhanVien.isBlank()) return null;
        return nhanVienDao.findById(maNhanVien).map(this::toDTO).orElse(null);
    }

    @Override
    public NhanVienDTO addNhanVienAutoCode(NhanVienDTO nhanVien) {
        validateNhanVien(nhanVien, false);
        validateDuplicate(nhanVien, false);

        for (int i = 0; i < 10; i++) {
            String maNhanVien = nhanVienDao.generateMaNV();
            if (nhanVienDao.existsById(maNhanVien)) continue;

            nhanVien.setMaNhanVien(maNhanVien);
            NhanVien entity = toEntity(nhanVien);

            String matKhauMacDinh = taoMatKhauMacDinh(maNhanVien);
            TaiKhoan taiKhoan = TaiKhoan.builder()
                    .maNhanVien(maNhanVien)
                    .matKhau(matKhauMacDinh)
                    .vaiTro(VAI_TRO_MAC_DINH)
                    .nhanVien(entity)
                    .build();
            entity.setTaiKhoan(taiKhoan);

            return toDTO(nhanVienDao.save(entity));
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