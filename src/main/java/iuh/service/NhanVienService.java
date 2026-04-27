package iuh.service;

import iuh.dao.NhanVienDao;
import iuh.dto.NhanVienDTO;
import iuh.entity.NhanVien;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    /**
     * Xác thực đăng nhập
     * @param tenDangNhap tên đăng nhập
     * @param matKhau mật khẩu
     * @return NhanVienDTO nếu xác thực thành công, null nếu thất bại
     */
    public NhanVienDTO xacThucDangNhap(String tenDangNhap, String matKhau) {
        if (tenDangNhap == null || tenDangNhap.isEmpty() ||
            matKhau == null || matKhau.isEmpty()) {
            return null;
        }

        try {
            List<NhanVien> allNhanVien = getAllNhanVien();
            NhanVien nhanVien = allNhanVien.stream()
                    .filter(nv -> nv.getTaiKhoan() != null &&
                                  nv.getTaiKhoan().getTenDangNhap().equals(tenDangNhap) &&
                                  nv.getTaiKhoan().getMatKhau().equals(matKhau))
                    .findFirst()
                    .orElse(null);

            if (nhanVien != null) {
                return mapToDTO(nhanVien);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Chuyển đổi NhanVien entity thành DTO
     */
    private NhanVienDTO mapToDTO(NhanVien nhanVien) {
        if (nhanVien == null) {
            return null;
        }

        return NhanVienDTO.builder()
                .maNhanVien(nhanVien.getMaNhanVien())
                .CCCD(nhanVien.getCCCD())
                .tenNhanVien(nhanVien.getTenNhanVien())
                .taiKhoan(nhanVien.getTaiKhoan() != null ? nhanVien.getTaiKhoan().getTenDangNhap() : "")
                .gioiTinh(nhanVien.isGioiTinh())
                .ngaySinh(nhanVien.getNgaySinh())
                .email(nhanVien.getEmail())
                .soDienThoai(nhanVien.getSoDienThoai())
                .ngayBatDau(nhanVien.getNgayBatDau())
                .trangThai(nhanVien.getTrangThai() != null ? nhanVien.getTrangThai().toString() : "")
                .diaChi(nhanVien.getDiaChi())
                .build();
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