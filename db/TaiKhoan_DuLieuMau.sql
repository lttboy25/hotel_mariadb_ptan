-- =============================================
-- DỮ LIỆU MẪU ĐĂNG NHẬP VICTORYA
-- =============================================

-- Xóa dữ liệu cũ (nếu cần)
 DELETE FROM NhanVien WHERE tenDangNhap IN ('null', 'nhanvien1', 'nhanvien2');
-- DELETE FROM TaiKhoan WHERE tenDangNhap IN ('admin', 'nhanvien1', 'nhanvien2');

-- ===========================
-- INSERT TÀI KHOẢN
-- ===========================

INSERT INTO TaiKhoan (tenDangNhap, matKhau, vaiTro) VALUES
('admin', 'admin123', 'admin'),          -- Tài khoản admin (mật khẩu: admin123)
('nhanvien1', 'pass123', 'employee'),    -- Nhân viên 1 (mật khẩu: pass123)
('nhanvien2', 'pass456', 'employee');    -- Nhân viên 2 (mật khẩu: pass456)

-- ===========================
-- INSERT NHÂN VIÊN
-- ===========================

INSERT INTO NhanVien (maNhanVien, CCCD, tenNhanVien, gioiTinh, ngaySinh, email, soDienThoai, ngayBatDau, tenDangNhap, trangThai, diaChi) VALUES
-- Admin
('NV011', '123456789012', 'Nguyễn Quản Trị', 1, '1990-05-15', 'admin@victorya.com', '0901234567', '2025-01-01', 'admin', 'Đang làm việc', 'Hồ Chí Minh'),

-- Nhân viên 1
('NV012', '987654321098', 'Trần Văn A', 1, '1995-08-20', 'tvan.a@victorya.com', '0987654321', '2025-02-01', 'nhanvien1', 'Đang làm việc', 'Hồ Chí Minh'),

-- Nhân viên 2
('NV013', '111222333444', 'Lê Thị B', 0, '1998-03-10', 'lethib@victorya.com', '0901111111', '2025-02-15', 'nhanvien2', 'Đang làm việc', 'Bình Dương');

-- ===========================
-- GHI CHÚ
-- ===========================
/*
THÔNG TIN ĐĂNG NHẬP:

1. Admin:
   - Tài khoản: admin
   - Mật khẩu: admin123
   - Mã NV: NV001
   - Tên: Nguyễn Quản Trị

2. Nhân viên 1:
   - Tài khoản: nhanvien1
   - Mật khẩu: pass123
   - Mã NV: NV002
   - Tên: Trần Văn A

3. Nhân viên 2:
   - Tài khoản: nhanvien2
   - Mật khẩu: pass456
   - Mã NV: NV003
   - Tên: Lê Thị B
*/

