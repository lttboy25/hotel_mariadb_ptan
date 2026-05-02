package iuh.view;

import iuh.dto.NhanVienDTO;

/**
 * Lớp quản lý session người dùng hiện tại
 * Lưu trữ thông tin đăng nhập trong suốt phiên làm việc
 */
public class CurrentUser {
    private static CurrentUser instance;
    private NhanVienDTO nhanVien;

    private CurrentUser() {
    }

    public static CurrentUser getInstance() {
        if (instance == null) {
            instance = new CurrentUser();
        }
        return instance;
    }

    public void setNhanVien(NhanVienDTO nhanVien) {
        this.nhanVien = nhanVien;
    }

    public NhanVienDTO getNhanVien() {
        return nhanVien;
    }

    public String getHoTen() {
        return nhanVien != null ? nhanVien.getTenNhanVien() : "";
    }

    public String getEmail() {
        return nhanVien != null ? nhanVien.getEmail() : "";
    }

    public String getMaNhanVien() {
        return nhanVien != null ? nhanVien.getMaNhanVien() : "";
    }

    public String getTaiKhoan() {
        return nhanVien != null ? nhanVien.getTaiKhoan().getMaNhanVien() : "";
    }

    public void logout() {
        nhanVien = null;
    }

    public boolean isLoggedIn() {
        return nhanVien != null;
    }
}
