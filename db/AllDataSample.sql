-- Active: 1776938476085@@127.0.0.1@3306@hotel

-- NhanVien -- KhuyenMai --LoaiPhong → Phong → KhachHang → PhieuDatPhong → ChiTietPhieuDatPhong → PhieuHuyPhong


INSERT INTO NhanVien (maNhanVien, CCCD,tenNhanVien,gioiTinh,ngaySinh,email,soDienThoai,ngayBatDau,trangThai,diaChi) VALUES

-- Nhân viên đang làm việc
('NV001', '079123456789', 'Nguyễn Văn An', 1, '1998-05-12', 'an@gmail.com', '0909123456', '2024-01-10', 'DANG_LAM', 'TP.HCM'),
('NV002', '079987654321', 'Trần Thị Bình', 0, '1999-08-22', 'binh@gmail.com', '0911223344', '2023-11-01', 'DANG_LAM', 'Hà Nội'),
('NV003', '079456789123', 'Lê Văn Cường', 1, '1995-02-15', 'cuong@gmail.com', '0988776655', '2022-06-20', 'DANG_LAM', 'Đà Nẵng'),

-- Nhân viên mới
('NV004', '079333222111', 'Phạm Thị Dung', 0, '2000-12-01', 'dung@gmail.com', '0977554433', '2026-03-01', 'MOI_VAO', 'Cần Thơ'),
('NV005', '079111222333', 'Hoàng Văn Em', 1, '2001-07-19', 'em@gmail.com', '0966443322', '2026-04-10', 'MOI_VAO', 'Bình Dương'),

-- Nhân viên đã nghỉ
('NV006', '079999888777', 'Võ Thị Phương', 0, '1993-03-10', 'phuong@gmail.com', '0955332211', '2020-05-15', 'DA_NGHI', 'Huế'),
('NV007', '079222333444', 'Đỗ Văn Giang', 1, '1990-09-09', 'giang@gmail.com', '0944221100', '2019-09-01', 'DA_NGHI', 'Quảng Ninh'),

-- Nhân viên lâu năm
('NV008', '079555666777', 'Nguyễn Thị Hạnh', 0, '1992-11-25', 'hanh@gmail.com', '0933112233', '2018-02-14', 'DANG_LAM', 'TP.HCM'),
('NV009', '079777888999', 'Bùi Văn Khoa', 1, '1994-04-30', 'khoa@gmail.com', '0922001122', '2017-07-07', 'DANG_LAM', 'Hà Nội'),

-- Nhân viên thử việc
('NV010', '079444555666', 'Trịnh Thị Lan', 0, '2002-06-18', 'lan@gmail.com', '0911998877', '2026-04-01', 'THU_VIEC', 'Đồng Nai');

INSERT INTO KhuyenMai (maKhuyenMai,tenKhuyenMai,ngayBatDau,ngayKetThuc,trangThai,heSo,tongTienToiThieu,tongKhuyenMaiToiDa) VALUES
-- Khuyến mãi đang áp dụng (Tháng 4/2026)
('KM004', 'Ưu đãi Giải phóng 30/4', '2026-04-15 00:00:00', '2026-05-05 23:59:59', 'DANG_AP_DUNG', 0.20, 1500000, 500000),
('KM005', 'Giảm giá cuối tuần T4', '2026-04-01 00:00:00', '2026-04-30 23:59:59', 'DANG_AP_DUNG', 0.10, 500000, 100000),
('KM006', 'Khách hàng VIP tháng 4', '2026-04-01 00:00:00', '2026-04-30 23:59:59', 'DANG_AP_DUNG', 0.15, 2000000, 1000000),

-- Khuyến mãi sắp tới (Tương lai)
('KM007', 'Mùa hè rực rỡ 2026', '2026-06-01 00:00:00', '2026-08-31 23:59:59', 'CHUA_AP_DUNG', 0.25, 3000000, 800000),
('KM008', 'Quốc tế Thiếu nhi', '2026-05-25 00:00:00', '2026-06-05 23:59:59', 'CHUA_AP_DUNG', 0.30, 1000000, 200000),
('KM009', 'Lễ hội âm nhạc tháng 7', '2026-07-10 00:00:00', '2026-07-20 23:59:59', 'CHUA_AP_DUNG', 0.12, 1200000, 300000),

-- Khuyến mãi đã kết thúc (Quá khứ)
('KM010', 'Ưu đãi Tết Nguyên Đán', '2026-01-20 00:00:00', '2026-02-10 23:59:59', 'KET_THUC', 0.40, 5000000, 2000000),
('KM011', 'Valentine ngọt ngào', '2026-02-12 00:00:00', '2026-02-16 23:59:59', 'KET_THUC', 0.14, 800000, 150000),
('KM012', 'Chào mừng năm mới 2026', '2026-01-01 00:00:00', '2026-01-05 23:59:59', 'KET_THUC', 0.50, 2000000, 1000000),

-- Khuyến mãi dài hạn
('KM013', 'Ưu đãi đặt phòng sớm', '2026-01-01 00:00:00', '2026-12-31 23:59:59', 'DANG_AP_DUNG', 0.05, 0, 50000);

-- Active: 1776938476085@@127.0.0.1@3306@hotel
INSERT INTO `loaiphong` (`maLoaiPhong`, `gia`, `ngayTao`, `soNguoiLonToiDa`, `soTreEmToiDa`, `tenLoaiPhong`) VALUES
('LP001', 500000, '2026-04-17', 2, 1, 'Phòng Đơn Standard'),
('LP002', 850000, '2026-04-17', 2, 2, 'Phòng Đôi Deluxe'),
('LP003', 1500000, '2026-04-17', 4, 2, 'Phòng Gia Đình Suite');


-- Active: 1776938476085@@127.0.0.1@3306@hotel
INSERT INTO `phong` (`maPhong`, `moTa`, `soPhong`, `tang`, `tinhTrang`, `trangThai`, `maLoaiPhong`) VALUES
('P101', 'View sân vườn', '101', 1, 'Trống', 'Sẵn sàng', 'LP001'),
('P102', 'Gần thang máy', '102', 1, 'Đang thuê', 'Sẵn sàng', 'LP001'),
('P103', 'View phố', '103', 1, 'Trống', 'Đang sửa chữa', 'LP001'),
('P201', 'Ban công rộng', '201', 2, 'Trống', 'Sẵn sàng', 'LP002'),
('P202', 'Cửa sổ lớn', '202', 2, 'Đang thuê', 'Sẵn sàng', 'LP002'),
('P203', 'Yên tĩnh', '203', 2, 'Trống', 'Sẵn sàng', 'LP002'),
('P301', 'Phòng VIP tầng cao', '301', 3, 'Trống', 'Sẵn sàng', 'LP003'),
('P302', 'Nội thất gỗ', '302', 3, 'Đang thuê', 'Sẵn sàng', 'LP003'),
('P303', 'Cận sân thượng', '303', 3, 'Trống', 'Sẵn sàng', 'LP003'),
('P304', 'View biển', '304', 3, 'Trống', 'Sẵn sàng', 'LP003');

-- Active: 1776938476085@@127.0.0.1@3306@hotel
INSERT INTO `khachhang` (`maKhachHang`, `CCCD`, `hoTen`, `soDienThoai`, `email`, `ngayTao`) VALUES
('KH001', '001234567890', 'Nguyễn Văn An',   '0901234567', 'an.nguyen@gmail.com',   '2026-01-10'),
('KH002', '002345678901', 'Trần Thị Bình',   '0912345678', 'binh.tran@gmail.com',   '2026-01-15'),
('KH003', '003456789012', 'Lê Hoàng Cường',  '0923456789', 'cuong.le@gmail.com',    '2026-02-01'),
('KH004', '004567890123', 'Phạm Thị Dung',   '0934567890', 'dung.pham@gmail.com',   '2026-02-20'),
('KH005', '005678901234', 'Hoàng Minh Đức',  '0945678901', 'duc.hoang@gmail.com',   '2026-03-05');

-- Active: 1776938476085@@127.0.0.1@3306@hotel
INSERT INTO `phieudatphong` (`maPhieuDatPhong`, `ngayTao`, `trangThai`, `tienDatCoc`, `maKhachHang`) VALUES
('PDP001', '2026-04-01', 'Đã nhận phòng',  500000,  'KH001'),
('PDP002', '2026-04-05', 'Đã nhận phòng',  850000,  'KH002'),
('PDP003', '2026-04-10', 'Đã đặt', 1500000, 'KH003'),
('PDP004', '2026-04-15', 'Đã nhận phòng',  500000,  'KH004'),
('PDP005', '2026-04-18', 'Đã hủy',       0,       'KH005');



INSERT INTO `chitietphieudatphong`(`maPhieuDatPhong`, `maPhong`, `trangThai`,`soGioLuuTru`, `thoiGianNhanPhong`, `thoiGianTraPhong`, `soNguoi`) VALUES
('PDP001', 'P101', 'Đang ở',    2, '2026-04-20 14:00:00', '2026-04-22 12:00:00', 2),
('PDP002', 'P201', 'Đang ở',    2, '2026-04-21 14:00:00', '2026-04-24 12:00:00', 2),
('PDP003', 'P301', 'Chờ nhận',  2, '2026-04-25 14:00:00', '2026-04-28 12:00:00', 4),
('PDP004', 'P202', 'Đang ở',    2, '2026-04-19 14:00:00', '2026-04-21 12:00:00', 1);


INSERT INTO `phieuhuyphong` (`maHuyPhong`, `chiTietId`, `lyDo`, `ngayHuy`) VALUES
(1, 5, 'Khách thay đổi kế hoạch đột xuất',        '2026-04-19 09:00:00'),
(2, 4, 'Khách bị bệnh không thể di chuyển',        '2026-04-18 15:30:00'),
(3, 3, 'Công việc bận đột xuất',                   '2026-04-20 10:00:00'),
(4, 2, 'Đặt nhầm ngày',                            '2026-04-17 08:00:00'),
(5, 1, 'Chuyển sang phòng khác trong cùng khách sạn', '2026-04-16 11:00:00')