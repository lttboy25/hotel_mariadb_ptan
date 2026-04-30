-- ========================================
-- HỆ THỐNG QUẢN LÝ KHÁCH SẠN VICTORYA
-- DỮ LIỆU MẪU - NGÀY 30/04/2026
-- ========================================
-- Password mặc định tất cả nhân viên = maNhanVien
-- Status: DANG_LAM (Đang làm), MOI_VAO (Mới vào), THU_VIEC (Thử việc), DA_NGHI (Đã nghỉ)

SET FOREIGN_KEY_CHECKS = 0;

-- ========================================
-- 1. NHÂN VIÊN (NhanVien)
-- ========================================
INSERT INTO
    NhanVien (
        maNhanVien,
        CCCD,
        tenNhanVien,
        gioiTinh,
        ngaySinh,
        email,
        soDienThoai,
        ngayBatDau,
        trangThai,
        diaChi
    )
VALUES (
        'NV001',
        '079123456789',
        'Nguyễn Văn An',
        1,
        '1990-05-12',
        'an.admin@hotel.com',
        '0909111111',
        '2022-01-10',
        'DANG_LAM',
        'TP.HCM'
    ),
    (
        'NV002',
        '079987654321',
        'Trần Thị Bình',
        0,
        '1995-08-22',
        'binh.manager@hotel.com',
        '0911111111',
        '2022-06-01',
        'DANG_LAM',
        'Hà Nội'
    ),
    (
        'NV003',
        '079456789123',
        'Lê Văn Cường',
        1,
        '1998-02-15',
        'cuong.staff@hotel.com',
        '0988111111',
        '2023-03-20',
        'DANG_LAM',
        'Đà Nẵng'
    ),
    (
        'NV004',
        '079333222111',
        'Phạm Thị Dung',
        0,
        '2000-12-01',
        'dung.staff@hotel.com',
        '0977111111',
        '2025-03-01',
        'DANG_LAM',
        'Cần Thơ'
    ),
    (
        'NV005',
        '079111222333',
        'Hoàng Văn Em',
        1,
        '1999-07-19',
        'em.staff@hotel.com',
        '0966111111',
        '2025-10-10',
        'MOI_VAO',
        'Bình Dương'
    ),
    (
        'NV006',
        '079999888777',
        'Võ Thị Phương',
        0,
        '1992-03-10',
        'phuong.reception@hotel.com',
        '0955111111',
        '2021-05-15',
        'DANG_LAM',
        'Huế'
    ),
    (
        'NV007',
        '079222333444',
        'Đỗ Văn Giang',
        1,
        '1988-09-09',
        'giang.chef@hotel.com',
        '0944111111',
        '2020-01-01',
        'DANG_LAM',
        'Quảng Ninh'
    ),
    (
        'NV008',
        '079555666777',
        'Nguyễn Thị Hạnh',
        0,
        '1994-11-25',
        'hanh.housekeeping@hotel.com',
        '0933111111',
        '2021-07-14',
        'DANG_LAM',
        'TP.HCM'
    ),
    (
        'NV009',
        '079777888999',
        'Bùi Văn Khoa',
        1,
        '1996-04-30',
        'khoa.maintenance@hotel.com',
        '0922111111',
        '2022-09-07',
        'DANG_LAM',
        'Hà Nội'
    ),
    (
        'NV010',
        '079444555666',
        'Trịnh Thị Lan',
        0,
        '2003-06-18',
        'lan.intern@hotel.com',
        '0911999999',
        '2026-04-01',
        'THU_VIEC',
        'Đồng Nai'
    );

-- ========================================
-- 2. TÀI KHOẢN (TaiKhoan)
-- ========================================
INSERT INTO
    TaiKhoan (maNhanVien, matKhau, vaiTro)
VALUES ('NV001', 'NV001', 'admin'),
    ('NV002', 'NV002', 'manager'),
    ('NV003', 'NV003', 'staff'),
    ('NV004', 'NV004', 'staff'),
    ('NV005', 'NV005', 'staff'),
    ('NV006', 'NV006', 'staff'),
    ('NV007', 'NV007', 'staff'),
    ('NV008', 'NV008', 'staff'),
    ('NV009', 'NV009', 'staff'),
    ('NV010', 'NV010', 'staff');

-- ========================================
-- 3. LOẠI PHÒNG (LoaiPhong)
-- ========================================
INSERT INTO
    LoaiPhong (
        maLoaiPhong,
        tenLoaiPhong,
        gia,
        ngayTao,
        soNguoiLonToiDa,
        soTreEmToiDa
    )
VALUES (
        'LP001',
        'Phòng Đơn Cơ Bản',
        500000,
        '2025-01-01',
        1,
        0
    ),
    (
        'LP002',
        'Phòng Đôi Cơ Bản',
        700000,
        '2025-01-01',
        2,
        1
    ),
    (
        'LP003',
        'Phòng Gia Đình',
        1000000,
        '2025-01-01',
        2,
        2
    ),
    (
        'LP004',
        'Phòng Suite Cao Cấp',
        1500000,
        '2025-01-01',
        2,
        1
    ),
    (
        'LP005',
        'Phòng VIP',
        2500000,
        '2025-01-01',
        4,
        2
    );

-- ========================================
-- 4. PHÒNG (Phong)
-- ========================================
INSERT INTO
    Phong (
        maPhong,
        soPhong,
        maLoaiPhong,
        trangThai,
        tang,
        tinhTrang,
        moTa
    )
VALUES
    -- Tầng 1: Phòng đơn (5 phòng)
    (
        'P101',
        '101',
        'LP001',
        'SAN_SANG',
        1,
        'TRONG',
        'Phòng đơn tầng 1 - có cửa sổ'
    ),
    (
        'P102',
        '102',
        'LP001',
        'SAN_SANG',
        1,
        'TRONG',
        'Phòng đơn tầng 1 - nhìn ra sân'
    ),
    (
        'P103',
        '103',
        'LP001',
        'SAN_SANG',
        1,
        'TRONG',
        'Phòng đơn tầng 1'
    ),
    (
        'P104',
        '104',
        'LP001',
        'SAN_SANG',
        1,
        'TRONG',
        'Phòng đơn tầng 1'
    ),
    (
        'P105',
        '105',
        'LP001',
        'DANG_SUA_CHUA',
        1,
        'TRONG',
        'Phòng đơn tầng 1 - cần bảo trì'
    ),

-- Tầng 2: Phòng đôi (5 phòng)
(
    'P201',
    '201',
    'LP002',
    'SAN_SANG',
    2,
    'TRONG',
    'Phòng đôi tầng 2 - view đường phố'
),
(
    'P202',
    '202',
    'LP002',
    'SAN_SANG',
    2,
    'TRONG',
    'Phòng đôi tầng 2 - view sân'
),
(
    'P203',
    '203',
    'LP002',
    'SAN_SANG',
    2,
    'TRONG',
    'Phòng đôi tầng 2'
),
(
    'P204',
    '204',
    'LP002',
    'SAN_SANG',
    2,
    'TRONG',
    'Phòng đôi tầng 2'
),
(
    'P205',
    '205',
    'LP002',
    'SAN_SANG',
    2,
    'TRONG',
    'Phòng đôi tầng 2'
),

-- Tầng 3: Phòng gia đình (3 phòng)
(
    'P301',
    '301',
    'LP003',
    'SAN_SANG',
    3,
    'TRONG',
    'Phòng gia đình tầng 3 - có phòng riêng cho bé'
),
(
    'P302',
    '302',
    'LP003',
    'SAN_SANG',
    3,
    'TRONG',
    'Phòng gia đình tầng 3'
),
(
    'P303',
    '303',
    'LP003',
    'SAN_SANG',
    3,
    'TRONG',
    'Phòng gia đình tầng 3'
),

-- Tầng 4: Phòng Suite (4 phòng)
(
    'P401',
    '401',
    'LP004',
    'SAN_SANG',
    4,
    'TRONG',
    'Suite tầng 4 - view panorama'
),
(
    'P402',
    '402',
    'LP004',
    'SAN_SANG',
    4,
    'TRONG',
    'Suite tầng 4 - ban công'
),
(
    'P403',
    '403',
    'LP004',
    'SAN_SANG',
    4,
    'TRONG',
    'Suite tầng 4'
),
(
    'P404',
    '404',
    'LP004',
    'SAN_SANG',
    4,
    'TRONG',
    'Suite tầng 4'
),

-- Tầng 5: Phòng VIP (3 phòng)
(
    'P501',
    '501',
    'LP005',
    'SAN_SANG',
    5,
    'TRONG',
    'Phòng VIP Penthouse - hồ bơi riêng'
),
(
    'P502',
    '502',
    'LP005',
    'SAN_SANG',
    5,
    'TRONG',
    'Phòng VIP - tầng cao nhất'
),
(
    'P503',
    '503',
    'LP005',
    'SAN_SANG',
    5,
    'TRONG',
    'Phòng VIP - tầng cao'
);

-- ========================================
-- 5. CA LÀNG VIỆC (Ca)
-- ========================================
INSERT INTO
    cas (maCa, ngayBatDau, ngayKetThuc)
VALUES (
        'CA001',
        '2026-04-01',
        '2026-04-30'
    ),
    (
        'CA002',
        '2026-04-15',
        '2026-05-15'
    ),
    (
        'CA003',
        '2026-05-01',
        '2026-05-31'
    );

-- ========================================
-- 6. CA LÀM VIỆC NHÂN VIÊN (CaLamViecNhanVien)
-- ========================================
INSERT INTO
    CaLamViecNhanVien (
        maCaLamViec,
        tienMoCa,
        tienKetCa,
        trangThai,
        tongChi,
        tongThu,
        maCa,
        maNhanVien,
        ngay
    )
VALUES (
        'CALVN001',
        0,
        0,
        'DANG_DIEN_RA',
        0,
        0,
        'CA001',
        'NV001',
        '2026-04-20'
    ),
    (
        'CALVN002',
        0,
        0,
        'DANG_DIEN_RA',
        0,
        0,
        'CA001',
        'NV002',
        '2026-04-20'
    ),
    (
        'CALVN003',
        0,
        0,
        'DANG_DIEN_RA',
        0,
        0,
        'CA001',
        'NV006',
        '2026-04-20'
    ),
    (
        'CALVN004',
        0,
        0,
        'DANG_DIEN_RA',
        0,
        0,
        'CA001',
        'NV007',
        '2026-04-20'
    ),
    (
        'CALVN005',
        0,
        0,
        'DANG_DIEN_RA',
        0,
        0,
        'CA001',
        'NV008',
        '2026-04-20'
    ),
    (
        'CALVN006',
        0,
        0,
        'DANG_DIEN_RA',
        0,
        0,
        'CA001',
        'NV009',
        '2026-04-20'
    );

-- ========================================
-- 7. KHÁCH HÀNG (KhachHang)
-- ========================================
INSERT INTO
    KhachHang (
        maKhachHang,
        CCCD,
        hoTen,
        soDienThoai,
        email,
        ngayTao
    )
VALUES (
        'KH001',
        '079111111111',
        'Trần Văn Tâm',
        '0901111111',
        'tam.tran@email.com',
        '2026-03-15'
    ),
    (
        'KH002',
        '079222222222',
        'Lý Thị Mỹ',
        '0912222222',
        'my.ly@email.com',
        '2026-03-16'
    ),
    (
        'KH003',
        '079333333333',
        'Phan Minh Tuấn',
        '0923333333',
        'tuan.phan@email.com',
        '2026-03-20'
    ),
    (
        'KH004',
        '079444444444',
        'Võ Tú Linh',
        '0934444444',
        'linh.vo@email.com',
        '2026-03-22'
    ),
    (
        'KH005',
        '079555555555',
        'Đặng Hữu Phúc',
        '0945555555',
        'phuc.dang@email.com',
        '2026-03-25'
    ),
    (
        'KH006',
        '079666666666',
        'Hoàng Thu Hương',
        '0956666666',
        'huong.hoang@email.com',
        '2026-04-01'
    ),
    (
        'KH007',
        '079777777777',
        'Bố Thế Anh',
        '0967777777',
        'anh.bo@email.com',
        '2026-04-05'
    ),
    (
        'KH008',
        '079888888888',
        'Kiều Việt Sơn',
        '0978888888',
        'son.kieu@email.com',
        '2026-04-10'
    ),
    (
        'KH009',
        '079999999999',
        'Nông Khánh Vy',
        '0989999999',
        'vy.nong@email.com',
        '2026-04-15'
    ),
    (
        'KH010',
        '079101010101',
        'Tô Gia Hân',
        '0990101010',
        'han.to@email.com',
        '2026-04-18'
    );

-- ========================================
-- 8. KHUYẾN MÃI (KhuyenMai)
-- ========================================
INSERT INTO
    KhuyenMai (
        maKhuyenMai,
        tenKhuyenMai,
        ngayBatDau,
        ngayKetThuc,
        trangThai,
        heSo,
        tongTienToiThieu,
        tongKhuyenMaiToiDa
    )
VALUES (
        'KM001',
        'Khuyến mãi Lễ 30/4 - 1/5',
        '2026-04-25 00:00:00',
        '2026-05-05 23:59:59',
        'DANG_AP_DUNG',
        0.20,
        1000000,
        500000
    ),
    (
        'KM002',
        'Giảm giá cuối tuần 10%',
        '2026-04-25 00:00:00',
        '2026-04-27 23:59:59',
        'DANG_AP_DUNG',
        0.10,
        500000,
        100000
    ),
    (
        'KM003',
        'Khách hàng VIP 15%',
        '2026-04-01 00:00:00',
        '2026-04-30 23:59:59',
        'DANG_AP_DUNG',
        0.15,
        2000000,
        1000000
    ),
    (
        'KM004',
        'Mua hè rực rỡ 2026 - 25%',
        '2026-06-01 00:00:00',
        '2026-08-31 23:59:59',
        'CHUA_AP_DUNG',
        0.25,
        3000000,
        800000
    ),
    (
        'KM005',
        'Quốc tế Thiếu nhi',
        '2026-05-25 00:00:00',
        '2026-06-05 23:59:59',
        'CHUA_AP_DUNG',
        0.30,
        1000000,
        200000
    );

-- ========================================
-- 9. PHIẾU ĐẶT PHÒNG (PhieuDatPhong)
-- ========================================
INSERT INTO
    PhieuDatPhong (
        maPhieuDatPhong,
        ngayTao,
        trangThai,
        tienDatCoc,
        maKhachHang
    )
VALUES (
        'PDP001',
        '2026-04-15',
        'DA_DAT',
        150000,
        'KH001'
    ),
    (
        'PDP002',
        '2026-04-16',
        'DA_DAT',
        210000,
        'KH002'
    ),
    (
        'PDP003',
        '2026-04-17',
        'NHAN_PHONG',
        300000,
        'KH003'
    ),
    (
        'PDP004',
        '2026-04-18',
        'NHAN_PHONG',
        450000,
        'KH004'
    ),
    (
        'PDP005',
        '2026-04-19',
        'NHAN_PHONG',
        750000,
        'KH005'
    ),
    (
        'PDP006',
        '2026-04-20',
        'DA_DAT',
        1500000,
        'KH006'
    ),
    (
        'PDP007',
        '2026-04-20',
        'DA_DAT',
        2250000,
        'KH007'
    ),
    (
        'PDP008',
        '2026-04-21',
        'NHAN_PHONG',
        100000,
        'KH008'
    ),
    (
        'PDP009',
        '2026-04-22',
        'DA_THANH_TOAN',
        150000,
        'KH009'
    ),
    (
        'PDP010',
        '2026-04-23',
        'DA_DAT',
        300000,
        'KH010'
    );

-- ========================================
-- 10. CHI TIẾT PHIẾU ĐẶT PHÒNG (ChiTietPhieuDatPhong)
-- ========================================
INSERT INTO
    ChiTietPhieuDatPhong (
        maPhieuDatPhong,
        maPhong,
        trangThai,
        soGioLuuTru,
        thoiGianNhanPhong,
        thoiGianTraPhong,
        soNguoi
    )
VALUES (
        1,
        'P101',
        'CHUA_THANH_TOAN',
        24,
        '2026-04-20 14:00:00',
        '2026-04-21 11:00:00',
        1
    ),
    (
        2,
        'P201',
        'CHUA_THANH_TOAN',
        24,
        '2026-04-21 14:00:00',
        '2026-04-22 11:00:00',
        2
    ),
    (
        3,
        'P301',
        'DA_THANH_TOAN',
        48,
        '2026-04-20 14:00:00',
        '2026-04-22 11:00:00',
        3
    ),
    (
        4,
        'P401',
        'DA_THANH_TOAN',
        36,
        '2026-04-21 14:00:00',
        '2026-04-23 11:00:00',
        2
    ),
    (
        5,
        'P501',
        'DA_THANH_TOAN',
        72,
        '2026-04-22 14:00:00',
        '2026-04-25 11:00:00',
        4
    ),
    (
        6,
        'P102',
        'CHUA_THANH_TOAN',
        24,
        '2026-04-25 14:00:00',
        '2026-04-26 11:00:00',
        1
    ),
    (
        7,
        'P202',
        'CHUA_THANH_TOAN',
        24,
        '2026-04-25 14:00:00',
        '2026-04-26 11:00:00',
        2
    ),
    (
        8,
        'P302',
        'DA_THANH_TOAN',
        48,
        '2026-04-23 14:00:00',
        '2026-04-25 11:00:00',
        2
    ),
    (
        9,
        'P402',
        'DA_THANH_TOAN',
        36,
        '2026-04-22 14:00:00',
        '2026-04-24 11:00:00',
        2
    ),
    (
        10,
        'P502',
        'CHUA_THANH_TOAN',
        24,
        '2026-04-25 14:00:00',
        '2026-04-26 11:00:00',
        3
    );

-- ========================================
-- 11. HOÁ ĐƠN (HoaDon)
-- ========================================
INSERT INTO
    HoaDon (
        maHoaDon,
        ngayDat,
        maKhachHang,
        maNhanVien,
        maKhuyenMai,
        ngayTao,
        trangThai,
        tongTien,
        tienKhachDua,
        tienThoi
    )
VALUES (
        'HD001',
        '2026-04-20 14:00:00',
        'KH003',
        'NV002',
        NULL,
        '2026-04-22 11:30:00',
        'DA_THANH_TOAN',
        1000000,
        1000000,
        0
    ),
    (
        'HD002',
        '2026-04-21 14:00:00',
        'KH004',
        'NV002',
        'KM001',
        '2026-04-23 11:30:00',
        'DA_THANH_TOAN',
        1350000,
        1400000,
        50000
    ),
    (
        'HD003',
        '2026-04-22 14:00:00',
        'KH005',
        'NV003',
        'KM001',
        '2026-04-25 11:30:00',
        'DA_THANH_TOAN',
        2400000,
        2500000,
        100000
    ),
    (
        'HD004',
        '2026-04-23 14:00:00',
        'KH008',
        'NV002',
        NULL,
        '2026-04-25 11:30:00',
        'DA_THANH_TOAN',
        480000,
        500000,
        20000
    ),
    (
        'HD005',
        '2026-04-22 14:00:00',
        'KH009',
        'NV006',
        NULL,
        '2026-04-24 11:30:00',
        'DA_THANH_TOAN',
        1500000,
        1500000,
        0
    );

-- ========================================
-- 12. CHI TIẾT HOÁ ĐƠN (ChiTietHoaDon)
-- ========================================
INSERT INTO
    ChiTietHoaDon (
        maHoaDon,
        id,
        maPhong,
        ngayTao,
        tongTien
    )
VALUES (
        'HD001',
        3,
        'P301',
        '2026-04-22 11:30:00',
        1000000
    ),
    (
        'HD002',
        4,
        'P401',
        '2026-04-23 11:30:00',
        1350000
    ),
    (
        'HD003',
        5,
        'P501',
        '2026-04-25 11:30:00',
        2400000
    ),
    (
        'HD004',
        8,
        'P302',
        '2026-04-25 11:30:00',
        480000
    ),
    (
        'HD005',
        9,
        'P402',
        '2026-04-24 11:30:00',
        1500000
    );

-- ========================================
-- 13. PHIẾU HỦY PHÒNG (PhieuHuyPhong)
-- ========================================
INSERT INTO
    PhieuHuyPhong (
        maPhieuHuyPhong,
        id,
        lyDoHuy,
        tienHoanLai,
        ngayHuy,
        trangThaiHuy
    )
VALUES (
        'PHP001',
        1,
        'Khách hủy do công việc gấp',
        100000,
        '2026-04-20 15:00:00',
        'DA_HUY'
    );

SET FOREIGN_KEY_CHECKS = 1;

-- ========================================
-- TỔNG KẾT DỮ LIỆU
-- ========================================
-- 10 Nhân viên (1 Admin + 1 Manager + 8 Staff)
-- 10 Tài khoản
-- 5 Loại phòng
-- 23 Phòng (1 cần bảo trì, 22 khả dụng)
-- 3 Ca làm việc
-- 6 Ca làm việc nhân viên đang hoạt động
-- 10 Khách hàng
-- 5 Khuyến mãi (3 đang áp dụng, 2 chuẩn bị áp dụng)
-- 10 Phiếu đặt phòng (3 đã hủy/hoàn thành, 7 đang chờ)
-- 10 Chi tiết phiếu đặt phòng (4 đã thanh toán, 6 chưa thanh toán)
-- 5 Hoá đơn đã thanh toán
-- 5 Chi tiết hoá đơn
-- 1 Phiếu hủy phòng
-- ========================================
(
    'KM009',
    'Le hoi am nhac thang 7',
    '2026-07-10 00:00:00',
    '2026-07-20 23:59:59',
    'CHUA_AP_DUNG',
    0.12,
    1200000,
    300000
),
(
    'KM010',
    'Uu dai Tet Nguyen Dan',
    '2026-01-20 00:00:00',
    '2026-02-10 23:59:59',
    'KET_THUC',
    0.40,
    5000000,
    2000000
),
(
    'KM011',
    'Valentine ngot ngao',
    '2026-02-12 00:00:00',
    '2026-02-16 23:59:59',
    'KET_THUC',
    0.14,
    800000,
    150000
),
(
    'KM012',
    'Chao mung nam moi 2026',
    '2026-01-01 00:00:00',
    '2026-01-05 23:59:59',
    'KET_THUC',
    0.50,
    2000000,
    1000000
),
(
    'KM013',
    'Uu dai dat phong som',
    '2026-01-01 00:00:00',
    '2026-12-31 23:59:59',
    'DANG_AP_DUNG',
    0.05,
    0,
    50000
);

-- =========================
-- LOAI PHONG
-- =========================
INSERT INTO
    LoaiPhong (
        maLoaiPhong,
        gia,
        ngayTao,
        soNguoiLonToiDa,
        soTreEmToiDa,
        tenLoaiPhong
    )
VALUES (
        'LP001',
        500000,
        '2026-04-17',
        2,
        1,
        'Phong Don Standard'
    ),
    (
        'LP002',
        850000,
        '2026-04-17',
        2,
        2,
        'Phong Doi Deluxe'
    ),
    (
        'LP003',
        1500000,
        '2026-04-17',
        4,
        2,
        'Phong Gia Dinh Suite'
    );

-- =========================
-- PHONG
INSERT INTO
    Phong (
        maPhong,
        moTa,
        soPhong,
        tang,
        tinhTrang,
        trangThai,
        maLoaiPhong
    )
VALUES (
        'P101',
        N'View sân vườn',
        '101',
        1,
        'TRONG',
        'SAN_SANG',
        'LP001'
    ),
    (
        'P102',
        N'Gần thang máy',
        '102',
        1,
        'TRONG',
        'SAN_SANG',
        'LP001'
    ),
    (
        'P103',
        N'View phố',
        '103',
        1,
        'TRONG',
        'DANG_SUA_CHUA',
        'LP001'
    ),
    (
        'P201',
        N'Ban công rộng',
        '201',
        2,
        'TRONG',
        'SAN_SANG',
        'LP002'
    ),
    (
        'P202',
        N'Cửa sổ lớn',
        '202',
        2,
        'DANG_THUE',
        'SAN_SANG',
        'LP002'
    ),
    (
        'P203',
        N'Yên tĩnh',
        '203',
        2,
        'TRONG',
        'SAN_SANG',
        'LP002'
    ),
    (
        'P301',
        N'Phòng VIP tầng cao',
        '301',
        3,
        'TRONG',
        'SAN_SANG',
        'LP003'
    ),
    (
        'P302',
        N'Nội thất gỗ',
        '302',
        3,
        'TRONG',
        'SAN_SANG',
        'LP003'
    ),
    (
        'P303',
        N'Cận sân thượng',
        '303',
        3,
        'TRONG',
        'SAN_SANG',
        'LP003'
    ),
    (
        'P304',
        N'View biển',
        '304',
        3,
        'TRONG',
        'SAN_SANG',
        'LP003'
    );
-- =========================
-- KHACH HANG
-- =========================
INSERT INTO
    KhachHang (
        maKhachHang,
        CCCD,
        hoTen,
        soDienThoai,
        email,
        ngayTao
    )
VALUES (
        'KH001',
        '001234567890',
        'Nguyen Van An',
        '0901234567',
        'an.nguyen@gmail.com',
        '2026-01-10'
    ),
    (
        'KH002',
        '002345678901',
        'Tran Thi Binh',
        '0912345678',
        'binh.tran@gmail.com',
        '2026-01-15'
    ),
    (
        'KH003',
        '003456789012',
        'Le Hoang Cuong',
        '0923456789',
        'cuong.le@gmail.com',
        '2026-02-01'
    ),
    (
        'KH004',
        '004567890123',
        'Pham Thi Dung',
        '0934567890',
        'dung.pham@gmail.com',
        '2026-02-20'
    ),
    (
        'KH005',
        '005678901234',
        'Hoang Minh Duc',
        '0945678901',
        'duc.hoang@gmail.com',
        '2026-03-05'
    ),
    (
        'KH006',
        '006789012345',
        'Bui Thi Hoa',
        '0956789012',
        'hoa.bui@gmail.com',
        '2026-03-18'
    );

-- =========================
-- PHIEU DAT PHONG
-- =========================
INSERT INTO
    PhieuDatPhong (
        maPhieuDatPhong,
        ngayTao,
        trangThai,
        tienDatCoc,
        maKhachHang
    )
VALUES (
        'PDP001',
        '2026-04-01',
        'DA_THANH_TOAN',
        500000,
        'KH001'
    ),
    (
        'PDP002',
        '2026-04-05',
        'DA_THANH_TOAN',
        850000,
        'KH002'
    ),
    (
        'PDP003',
        '2026-04-10',
        'DA_DAT',
        1500000,
        'KH003'
    ),
    (
        'PDP004',
        '2026-04-15',
        'NHAN_PHONG',
        500000,
        'KH004'
    ),
    (
        'PDP005',
        '2026-04-18',
        'DA_HUY',
        0,
        'KH005'
    ),
    (
        'PDP006',
        '2026-04-22',
        'DA_DAT',
        850000,
        'KH006'
    );

-- =========================
-- CHI TIET PHIEU DAT PHONG
-- id se tu dong tang 1..6 neu bang dang rong
-- =========================
INSERT INTO
    ChiTietPhieuDatPhong (
        maPhieuDatPhong,
        maPhong,
        trangThai,
        soGioLuuTru,
        thoiGianNhanPhong,
        thoiGianTraPhong,
        soNguoi
    )
VALUES (
        'PDP001',
        'P101',
        'DA_THANH_TOAN',
        46,
        '2026-04-20 14:00:00',
        '2026-04-22 12:00:00',
        2
    ),
    (
        'PDP002',
        'P201',
        'DA_THANH_TOAN',
        70,
        '2026-04-21 14:00:00',
        '2026-04-24 12:00:00',
        2
    ),
    (
        'PDP003',
        'P301',
        'CHUA_THANH_TOAN',
        46,
        '2026-04-30 14:00:00',
        '2026-05-02 12:00:00',
        4
    ),
    (
        'PDP004',
        'P202',
        'CHUA_THANH_TOAN',
        46,
        '2026-04-27 14:00:00',
        '2026-04-29 12:00:00',
        1
    ),
    (
        'PDP005',
        'P203',
        'DA_HUY',
        22,
        '2026-04-23 14:00:00',
        '2026-04-24 12:00:00',
        2
    ),
    (
        'PDP006',
        'P302',
        'CHUA_THANH_TOAN',
        22,
        '2026-05-03 14:00:00',
        '2026-05-04 12:00:00',
        2
    );

-- =========================
-- PHIEU HUY PHONG
-- chiTietId = 5 tuong ung dong PDP005 neu bang da rong truoc khi import
-- =========================
INSERT INTO
    PhieuHuyPhong (
        maHuyPhong,
        chiTietId,
        lyDo,
        ngayHuy
    )
VALUES (
        1,
        5,
        'Khach thay doi ke hoach dot xuat',
        '2026-04-19 09:00:00'
    );

-- =========================
-- HOA DON
-- Dung cho thong ke doanh thu
-- =========================
INSERT INTO
    HoaDon (
        maHoaDon,
        ngayDat,
        maKhachHang,
        maNhanVien,
        maKhuyenMai,
        ngayTao,
        trangThai,
        tongTien,
        tienKhachDua,
        tienThoi
    )
VALUES (
        'HD2026042201',
        '2026-04-22 12:15:00',
        'KH001',
        'NV001',
        'KM005',
        '2026-04-22 12:15:00',
        'DA_THANH_TOAN',
        990000,
        1000000,
        10000
    ),
    (
        'HD2026042401',
        '2026-04-24 12:20:00',
        'KH002',
        'NV002',
        NULL,
        '2026-04-24 12:20:00',
        'DA_THANH_TOAN',
        1870000,
        2000000,
        130000
    );

-- =========================
-- GHI CHU DANG NHAP
-- =========================
-- Tai khoan admin: NV001 / NV001
-- Nhan vien thuong: NV002 / NV002
-- Mat khau mac dinh cua nhan vien moi them trong app: chinh la ma nhan vien
-- =========================
-- CHÈN THÊM 10 PHÒNG MỚI
-- =========================
INSERT INTO
    Phong (
        maPhong,
        moTa,
        soPhong,
        tang,
        tinhTrang,
        trangThai,
        maLoaiPhong
    )
VALUES (
        'P401',
        N'Phòng Standard yên tĩnh',
        '401',
        4,
        'TRONG',
        'SAN_SANG',
        'LP001'
    ),
    (
        'P402',
        N'Phòng Standard view phố',
        '402',
        4,
        'TRONG',
        'SAN_SANG',
        'LP001'
    ),
    (
        'P403',
        N'Phòng Deluxe ban công',
        '403',
        4,
        'TRONG',
        'SAN_SANG',
        'LP002'
    ),
    (
        'P501',
        N'Phòng Deluxe tầng cao',
        '501',
        5,
        'TRONG',
        'SAN_SANG',
        'LP002'
    ),
    (
        'P502',
        N'Phòng Deluxe rộng rãi',
        '502',
        5,
        'TRONG',
        'SAN_SANG',
        'LP002'
    ),
    (
        'P503',
        N'Phòng Suite sang trọng',
        '503',
        5,
        'TRONG',
        'SAN_SANG',
        'LP003'
    ),
    (
        'P601',
        N'Penthouse Suite đặc biệt',
        '601',
        6,
        'TRONG',
        'SAN_SANG',
        'LP003'
    ),
    (
        'P602',
        N'Phòng Suite view toàn cảnh',
        '602',
        6,
        'TRONG',
        'SAN_SANG',
        'LP003'
    ),
    (
        'P603',
        N'Phòng Standard tiết kiệm',
        '603',
        6,
        'TRONG',
        'DANG_SUA_CHUA',
        'LP001'
    ),
    (
        'P604',
        N'Phòng Deluxe hiện đại',
        '604',
        6,
        'TRONG',
        'SAN_SANG',
        'LP002'
    );

-- =========================
-- CHÈN THÊM 10 PHÒNG MỚI (ĐỢT 2)
-- =========================
INSERT INTO
    Phong (
        maPhong,
        moTa,
        soPhong,
        tang,
        tinhTrang,
        trangThai,
        maLoaiPhong
    )
VALUES (
        'P701',
        N'Phòng Standard hướng đông',
        '701',
        7,
        'TRONG',
        'SAN_SANG',
        'LP001'
    ),
    (
        'P702',
        N'Phòng Standard hướng tây',
        '702',
        7,
        'TRONG',
        'SAN_SANG',
        'LP001'
    ),
    (
        'P801',
        N'Phòng Deluxe có bồn tắm',
        '801',
        8,
        'TRONG',
        'SAN_SANG',
        'LP002'
    ),
    (
        'P802',
        N'Phòng Deluxe máy chiếu',
        '802',
        8,
        'TRONG',
        'SAN_SANG',
        'LP002'
    ),
    (
        'P803',
        N'Phòng Deluxe yên tĩnh',
        '803',
        8,
        'TRONG',
        'SAN_SANG',
        'LP002'
    ),
    (
        'P901',
        N'Phòng Suite gia đình lớn',
        '901',
        9,
        'TRONG',
        'SAN_SANG',
        'LP003'
    ),
    (
        'P902',
        N'Phòng Suite quầy bar mini',
        '902',
        9,
        'TRONG',
        'SAN_SANG',
        'LP003'
    ),
    (
        'P1001',
        N'Presidential Suite',
        '1001',
        10,
        'TRONG',
        'SAN_SANG',
        'LP003'
    ),
    (
        'P1002',
        N'Phòng Suite tầng thượng',
        '1002',
        10,
        'TRONG',
        'SAN_SANG',
        'LP003'
    ),
    (
        'P1003',
        N'Phòng Standard nhỏ',
        '1003',
        10,
        'TRONG',
        'DANG_DON_DEP',
        'LP001'
    );