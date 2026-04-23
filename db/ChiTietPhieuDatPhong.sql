-- Active: 1776938476085@@127.0.0.1@3306@hotel

-- LoaiPhong → Phong → KhachHang → PhieuDatPhong → ChiTietPhieuDatPhong → PhieuHuyPhong
INSERT INTO `chitietphieudatphong`(`maPhieuDatPhong`, `maPhong`, `trangThai`,`soGioLuuTru`, `thoiGianNhanPhong`, `thoiGianTraPhong`, `soNguoi`) VALUES
('PDP001', 'P101', 'Đang ở',    2, '2026-04-20 14:00:00', '2026-04-22 12:00:00', 2),
('PDP002', 'P201', 'Đang ở',    2, '2026-04-21 14:00:00', '2026-04-24 12:00:00', 2),
('PDP003', 'P301', 'Chờ nhận',  2, '2026-04-25 14:00:00', '2026-04-28 12:00:00', 4),
('PDP004', 'P202', 'Đang ở',    2, '2026-04-19 14:00:00', '2026-04-21 12:00:00', 1);

-- --------------------------------------------------------
-- 6. BẢNG PhieuHuyPhong (FK → ChiTietPhieuDatPhong)
-- Chỉ insert cho chi tiết có trangThai = 'Đã hủy' (id = 5)
-- Các id còn lại lấy theo AUTO_INCREMENT từ bước trên
-- --------------------------------------------------------