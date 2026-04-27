INSERT INTO NhanVien (
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
) VALUES

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