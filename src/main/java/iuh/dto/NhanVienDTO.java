package iuh.dto;

import iuh.entity.TaiKhoan;
import iuh.entity.TrangThaiNhanVien;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * DTO cho NhanVien
 * Sử dụng để truyền dữ liệu giữa các tầng
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NhanVienDTO implements Serializable {

    private String maNhanVien;

    private String CCCD;

    private String tenNhanVien;

    private TaiKhoanDTO taiKhoan;  // Lấy từ TaiKhoan.tenDangNhap

    private boolean gioiTinh;

    private LocalDate ngaySinh;

    private String email;

    private String soDienThoai;

    private LocalDate ngayBatDau;

    private TrangThaiNhanVien trangThai;  // "Hoạt động", "Đã nghỉ"

    private String diaChi;

    // Getter hỗ trợ cho header
    public String getHoTen() {
        return tenNhanVien;
    }

    public String getGioiTinhText() {
        return gioiTinh ? "Nam" : "Nữ";
    }
}

