

INSERT INTO TaiKhoan (tenDangNhap, matKhau, vaiTro) VALUES
('admin', 'admin123', 'admin'),
('nhanvien1', 'pass123', 'employee'),
('nhanvien2', 'pass456', 'employee')
ON DUPLICATE KEY UPDATE
  matKhau = VALUES(matKhau),
  vaiTro = VALUES(vaiTro);

INSERT INTO NhanVien (maNhanVien, CCCD, tenNhanVien, gioiTinh, ngaySinh, email, soDienThoai, ngayBatDau, tenDangNhap, trangThai, diaChi) VALUES
('NV0011', '123456789012', 'Nguyễn Quản Trị', 1, '1990-05-15', 'admin@victorya.com', '0901234567', '2025-01-01', 'admin', 'Đang làm việc', 'Hồ Chí Minh'),
('NV0012', '987654321098', 'Trần Văn A', 1, '1995-08-20', 'tvan.a@victorya.com', '0987654321', '2025-02-01', 'nhanvien1', 'Đang làm việc', 'Hồ Chí Minh'),
('NV0013', '111222333444', 'Lê Thị B', 0, '1998-03-10', 'lethib@victorya.com', '0901111111', '2025-02-15', 'nhanvien2', 'Đang làm việc', 'Bình Dương')

