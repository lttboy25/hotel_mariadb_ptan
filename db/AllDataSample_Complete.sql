-- Bo du lieu mau hoan chinh cho he thong quan ly khach san Victorya
-- Phu hop voi schema/entity hien tai trong source code
-- Mat khau mac dinh cua moi nhan vien = maNhanVien


-- =========================
-- NHAN VIEN
-- =========================
INSERT INTO NhanVien
    (maNhanVien, CCCD, tenNhanVien, gioiTinh, ngaySinh, email, soDienThoai, ngayBatDau, trangThai, diaChi)
VALUES
    ('NV001', '079123456789', 'Nguyen Van An', 1, '1998-05-12', 'an@gmail.com', '0909123456', '2024-01-10', 'DANG_LAM', 'TP.HCM'),
    ('NV002', '079987654321', 'Tran Thi Binh', 0, '1999-08-22', 'binh@gmail.com', '0911223344', '2023-11-01', 'DANG_LAM', 'Ha Noi'),
    ('NV003', '079456789123', 'Le Van Cuong', 1, '1995-02-15', 'cuong@gmail.com', '0988776655', '2022-06-20', 'DANG_LAM', 'Da Nang'),
    ('NV004', '079333222111', 'Pham Thi Dung', 0, '2000-12-01', 'dung@gmail.com', '0977554433', '2026-03-01', 'MOI_VAO', 'Can Tho'),
    ('NV005', '079111222333', 'Hoang Van Em', 1, '2001-07-19', 'em@gmail.com', '0966443322', '2026-04-10', 'MOI_VAO', 'Binh Duong'),
    ('NV006', '079999888777', 'Vo Thi Phuong', 0, '1993-03-10', 'phuong@gmail.com', '0955332211', '2020-05-15', 'DA_NGHI', 'Hue'),
    ('NV007', '079222333444', 'Do Van Giang', 1, '1990-09-09', 'giang@gmail.com', '0944221100', '2019-09-01', 'DA_NGHI', 'Quang Ninh'),
    ('NV008', '079555666777', 'Nguyen Thi Hanh', 0, '1992-11-25', 'hanh@gmail.com', '0933112233', '2018-02-14', 'DANG_LAM', 'TP.HCM'),
    ('NV009', '079777888999', 'Bui Van Khoa', 1, '1994-04-30', 'khoa@gmail.com', '0922001122', '2017-07-07', 'DANG_LAM', 'Ha Noi'),
    ('NV010', '079444555666', 'Trinh Thi Lan', 0, '2002-06-18', 'lan@gmail.com', '0911998877', '2026-04-01', 'THU_VIEC', 'Dong Nai');

-- =========================
-- TAI KHOAN
-- =========================
INSERT INTO TaiKhoan
    (maNhanVien, matKhau, vaiTro)
VALUES
    ('NV001', 'NV001', 'admin'),
    ('NV002', 'NV002', 'employee'),
    ('NV003', 'NV003', 'employee'),
    ('NV004', 'NV004', 'employee'),
    ('NV005', 'NV005', 'employee'),
    ('NV006', 'NV006', 'employee'),
    ('NV007', 'NV007', 'employee'),
    ('NV008', 'NV008', 'employee'),
    ('NV009', 'NV009', 'employee'),
    ('NV010', 'NV010', 'employee');

-- =========================
-- KHUYEN MAI
-- =========================
INSERT INTO KhuyenMai
    (maKhuyenMai, tenKhuyenMai, ngayBatDau, ngayKetThuc, trangThai, heSo, tongTienToiThieu, tongKhuyenMaiToiDa)
VALUES
    ('KM004', 'Uu dai Giai phong 30/4', '2026-04-15 00:00:00', '2026-05-05 23:59:59', 'DANG_AP_DUNG', 0.20, 1500000, 500000),
    ('KM005', 'Giam gia cuoi tuan T4', '2026-04-01 00:00:00', '2026-04-30 23:59:59', 'DANG_AP_DUNG', 0.10, 500000, 100000),
    ('KM006', 'Khach hang VIP thang 4', '2026-04-01 00:00:00', '2026-04-30 23:59:59', 'DANG_AP_DUNG', 0.15, 2000000, 1000000),
    ('KM007', 'Mua he ruc ro 2026', '2026-06-01 00:00:00', '2026-08-31 23:59:59', 'CHUA_AP_DUNG', 0.25, 3000000, 800000),
    ('KM008', 'Quoc te Thieu nhi', '2026-05-25 00:00:00', '2026-06-05 23:59:59', 'CHUA_AP_DUNG', 0.30, 1000000, 200000),
    ('KM009', 'Le hoi am nhac thang 7', '2026-07-10 00:00:00', '2026-07-20 23:59:59', 'CHUA_AP_DUNG', 0.12, 1200000, 300000),
    ('KM010', 'Uu dai Tet Nguyen Dan', '2026-01-20 00:00:00', '2026-02-10 23:59:59', 'KET_THUC', 0.40, 5000000, 2000000),
    ('KM011', 'Valentine ngot ngao', '2026-02-12 00:00:00', '2026-02-16 23:59:59', 'KET_THUC', 0.14, 800000, 150000),
    ('KM012', 'Chao mung nam moi 2026', '2026-01-01 00:00:00', '2026-01-05 23:59:59', 'KET_THUC', 0.50, 2000000, 1000000),
    ('KM013', 'Uu dai dat phong som', '2026-01-01 00:00:00', '2026-12-31 23:59:59', 'DANG_AP_DUNG', 0.05, 0, 50000);

-- =========================
-- LOAI PHONG
-- =========================
INSERT INTO LoaiPhong
    (maLoaiPhong, gia, ngayTao, soNguoiLonToiDa, soTreEmToiDa, tenLoaiPhong)
VALUES
    ('LP001', 500000, '2026-04-17', 2, 1, 'Phong Don Standard'),
    ('LP002', 850000, '2026-04-17', 2, 2, 'Phong Doi Deluxe'),
    ('LP003', 1500000, '2026-04-17', 4, 2, 'Phong Gia Dinh Suite');

-- =========================
-- PHONG
INSERT INTO Phong
(maPhong, moTa, soPhong, tang, tinhTrang, trangThai, maLoaiPhong)
VALUES
    ('P101', N'View sân vườn', '101', 1, N'Trống', N'Sẵn sàng', 'LP001'),
    ('P102', N'Gần thang máy', '102', 1, N'Trống', N'Sẵn sàng', 'LP001'),
    ('P103', N'View phố', '103', 1, N'Trống', N'Đang sửa chữa', 'LP001'),
    ('P201', N'Ban công rộng', '201', 2, N'Trống', N'Sẵn sàng', 'LP002'),
    ('P202', N'Cửa sổ lớn', '202', 2, N'Đang thuê', N'Sẵn sàng', 'LP002'),
    ('P203', N'Yên tĩnh', '203', 2, N'Trống', N'Sẵn sàng', 'LP002'),
    ('P301', N'Phòng VIP tầng cao', '301', 3, N'Trống', N'Sẵn sàng', 'LP003'),
    ('P302', N'Nội thất gỗ', '302', 3, N'Trống', N'Sẵn sàng', 'LP003'),
    ('P303', N'Cận sân thượng', '303', 3, N'Trống', N'Sẵn sàng', 'LP003'),
    ('P304', N'View biển', '304', 3, N'Trống', N'Sẵn sàng', 'LP003');
-- =========================
-- KHACH HANG
-- =========================
INSERT INTO KhachHang
    (maKhachHang, CCCD, hoTen, soDienThoai, email, ngayTao)
VALUES
    ('KH001', '001234567890', 'Nguyen Van An', '0901234567', 'an.nguyen@gmail.com', '2026-01-10'),
    ('KH002', '002345678901', 'Tran Thi Binh', '0912345678', 'binh.tran@gmail.com', '2026-01-15'),
    ('KH003', '003456789012', 'Le Hoang Cuong', '0923456789', 'cuong.le@gmail.com', '2026-02-01'),
    ('KH004', '004567890123', 'Pham Thi Dung', '0934567890', 'dung.pham@gmail.com', '2026-02-20'),
    ('KH005', '005678901234', 'Hoang Minh Duc', '0945678901', 'duc.hoang@gmail.com', '2026-03-05'),
    ('KH006', '006789012345', 'Bui Thi Hoa', '0956789012', 'hoa.bui@gmail.com', '2026-03-18');

-- =========================
-- PHIEU DAT PHONG
-- =========================
INSERT INTO PhieuDatPhong
    (maPhieuDatPhong, ngayTao, trangThai, tienDatCoc, maKhachHang)
VALUES
    ('PDP001', '2026-04-01', 'Đã thanh toán', 500000, 'KH001'),
    ('PDP002', '2026-04-05', 'Đã thanh toán', 850000, 'KH002'),
    ('PDP003', '2026-04-10', 'Đã đặt', 1500000, 'KH003'),
    ('PDP004', '2026-04-15', 'Đã nhận phòng', 500000, 'KH004'),
    ('PDP005', '2026-04-18', 'Đã hủy', 0, 'KH005'),
    ('PDP006', '2026-04-22', 'Đã đặt', 850000, 'KH006');

-- =========================
-- CHI TIET PHIEU DAT PHONG
-- id se tu dong tang 1..6 neu bang dang rong
-- =========================
INSERT INTO ChiTietPhieuDatPhong
    (maPhieuDatPhong, maPhong, trangThai, soGioLuuTru, thoiGianNhanPhong, thoiGianTraPhong, soNguoi)
VALUES
    ('PDP001', 'P101', 'Đã thanh toán', 46, '2026-04-20 14:00:00', '2026-04-22 12:00:00', 2),
    ('PDP002', 'P201', 'Đã thanh toán', 70, '2026-04-21 14:00:00', '2026-04-24 12:00:00', 2),
    ('PDP003', 'P301', 'Chưa thanh toán', 46, '2026-04-30 14:00:00', '2026-05-02 12:00:00', 4),
    ('PDP004', 'P202', 'Chưa thanh toán', 46, '2026-04-27 14:00:00', '2026-04-29 12:00:00', 1),
    ('PDP005', 'P203', 'Đã hủy', 22, '2026-04-23 14:00:00', '2026-04-24 12:00:00', 2),
    ('PDP006', 'P302', 'Chưa thanh toán', 22, '2026-05-03 14:00:00', '2026-05-04 12:00:00', 2);

-- =========================
-- PHIEU HUY PHONG
-- chiTietId = 5 tuong ung dong PDP005 neu bang da rong truoc khi import
-- =========================
INSERT INTO PhieuHuyPhong
    (maHuyPhong, chiTietId, lyDo, ngayHuy)
VALUES
    (1, 5, 'Khach thay doi ke hoach dot xuat', '2026-04-19 09:00:00');

-- =========================
-- HOA DON
-- Dung cho thong ke doanh thu
-- =========================
INSERT INTO HoaDon
    (maHoaDon, ngayDat, maKhachHang, maNhanVien, maKhuyenMai, ngayTao, trangThai, tongTien, tienKhachDua, tienThoi)
VALUES
    ('HD2026042201', '2026-04-22 12:15:00', 'KH001', 'NV001', 'KM005', '2026-04-22 12:15:00', 'Đã thanh toán', 990000, 1000000, 10000),
    ('HD2026042401', '2026-04-24 12:20:00', 'KH002', 'NV002', NULL, '2026-04-24 12:20:00', 'Đã thanh toán', 1870000, 2000000, 130000);

-- =========================
-- GHI CHU DANG NHAP
-- =========================
-- Tai khoan admin: NV001 / NV001
-- Nhan vien thuong: NV002 / NV002
-- Mat khau mac dinh cua nhan vien moi them trong app: chinh la ma nhan vien
-- =========================
-- CHÈN THÊM 10 PHÒNG MỚI
-- =========================
INSERT INTO Phong
(maPhong, moTa, soPhong, tang, tinhTrang, trangThai, maLoaiPhong)
VALUES
    ('P401', N'Phòng Standard yên tĩnh', '401', 4, N'Trống', N'Sẵn sàng', 'LP001'),
    ('P402', N'Phòng Standard view phố', '402', 4, N'Trống', N'Sẵn sàng', 'LP001'),
    ('P403', N'Phòng Deluxe ban công', '403', 4, N'Trống', N'Sẵn sàng', 'LP002'),
    ('P501', N'Phòng Deluxe tầng cao', '501', 5, N'Trống', N'Sẵn sàng', 'LP002'),
    ('P502', N'Phòng Deluxe rộng rãi', '502', 5, N'Trống', N'Sẵn sàng', 'LP002'),
    ('P503', N'Phòng Suite sang trọng', '503', 5, N'Trống', N'Sẵn sàng', 'LP003'),
    ('P601', N'Penthouse Suite đặc biệt', '601', 6, N'Trống', N'Sẵn sàng', 'LP003'),
    ('P602', N'Phòng Suite view toàn cảnh', '602', 6, N'Trống', N'Sẵn sàng', 'LP003'),
    ('P603', N'Phòng Standard tiết kiệm', '603', 6, N'Trống', N'Đang sửa chữa', 'LP001'),
    ('P604', N'Phòng Deluxe hiện đại', '604', 6, N'Trống', N'Sẵn sàng', 'LP002');


-- =========================
-- CHÈN THÊM 10 PHÒNG MỚI (ĐỢT 2)
-- =========================
INSERT INTO Phong
(maPhong, moTa, soPhong, tang, tinhTrang, trangThai, maLoaiPhong)
VALUES
    ('P701', N'Phòng Standard hướng đông', '701', 7, N'Trống', N'Sẵn sàng', 'LP001'),
    ('P702', N'Phòng Standard hướng tây', '702', 7, N'Trống', N'Sẵn sàng', 'LP001'),
    ('P801', N'Phòng Deluxe có bồn tắm', '801', 8, N'Trống', N'Sẵn sàng', 'LP002'),
    ('P802', N'Phòng Deluxe máy chiếu', '802', 8, N'Trống', N'Sẵn sàng', 'LP002'),
    ('P803', N'Phòng Deluxe yên tĩnh', '803', 8, N'Trống', N'Sẵn sàng', 'LP002'),
    ('P901', N'Phòng Suite gia đình lớn', '901', 9, N'Trống', N'Sẵn sàng', 'LP003'),
    ('P902', N'Phòng Suite quầy bar mini', '902', 9, N'Trống', N'Sẵn sàng', 'LP003'),
    ('P1001', N'Presidential Suite', '1001', 10, N'Trống', N'Sẵn sàng', 'LP003'),
    ('P1002', N'Phòng Suite tầng thượng', '1002', 10, N'Trống', N'Sẵn sàng', 'LP003'),
    ('P1003', N'Phòng Standard nhỏ', '1003', 10, N'Trống', N'Đang dọn dẹp', 'LP001');