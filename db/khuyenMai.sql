INSERT INTO KhuyenMai (
    maKhuyenMai,
    tenKhuyenMai,
    ngayBatDau,
    ngayKetThuc,
    trangThai,
    heSo,
    tongTienToiThieu,
    tongKhuyenMaiToiDa
) VALUES
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