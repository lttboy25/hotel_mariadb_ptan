-- =============================================
-- HOTEL DATABASE (MARIADB VERSION)
-- =============================================

-- 1. Tạo database với tên mới: hotel
CREATE DATABASE IF NOT EXISTS hotel 
CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE hotel;

-- ===========================
-- TẠO CÁC BẢNG
-- ===========================

-- 1. TaiKhoan
CREATE TABLE TaiKhoan (
    tenDangNhap VARCHAR(50) PRIMARY KEY,
    matKhau VARCHAR(100) NOT NULL,
    vaiTro VARCHAR(50) NOT NULL DEFAULT 'employee'
) ENGINE=InnoDB;

-- 2. NhanVien
CREATE TABLE NhanVien (
    maNhanVien VARCHAR(20) PRIMARY KEY,
    CCCD VARCHAR(12),
    tenNhanVien VARCHAR(100) NOT NULL,
    gioiTinh TINYINT(1) DEFAULT 1,
    ngaySinh DATE,
    email VARCHAR(100),
    soDienThoai VARCHAR(20),
    ngayBatDau DATE DEFAULT (CURRENT_DATE),
    tenDangNhap VARCHAR(50) UNIQUE,
    trangThai VARCHAR(50) DEFAULT 'Đang làm việc',
    diaChi VARCHAR(255),
    CONSTRAINT FK_NhanVien_TaiKhoan FOREIGN KEY (tenDangNhap) REFERENCES TaiKhoan(tenDangNhap)
) ENGINE=InnoDB;

-- 3. Ca
CREATE TABLE Ca (
    maCa VARCHAR(20) PRIMARY KEY,
    ngayBatDau DATE NOT NULL,
    ngayKetThuc DATE NOT NULL
) ENGINE=InnoDB;

-- 4. CaLamViecNhanVien
CREATE TABLE CaLamViecNhanVien (
    maCaLamViec VARCHAR(20) PRIMARY KEY,
    maNhanVien VARCHAR(20) NOT NULL,
    ngay DATE NOT NULL,
    tienMoCa DECIMAL(18,2) DEFAULT 0,
    tienKetCa DECIMAL(18,2) DEFAULT 0,
    tongChi DECIMAL(10, 2) DEFAULT 0 NOT NULL,
    tongThu DECIMAL(10, 2) DEFAULT 0 NOT NULL,
    maCa VARCHAR(20) NOT NULL,
    trangThai VARCHAR(50) DEFAULT 'Đang mở',
    CONSTRAINT FK_CLV_NhanVien FOREIGN KEY (maNhanVien) REFERENCES NhanVien(maNhanVien),
    CONSTRAINT FK_CLV_Ca FOREIGN KEY (maCa) REFERENCES Ca(maCa)
) ENGINE=InnoDB;

-- 5. LoaiPhong
CREATE TABLE LoaiPhong (
    maLoaiPhong VARCHAR(20) PRIMARY KEY,
    tenLoaiPhong VARCHAR(100) NOT NULL,
    gia DECIMAL(18, 2) NOT NULL,
    soNguoiLonToiDa INT NOT NULL,
    soTreEmToiDa INT NOT NULL,
    ngayTao DATE DEFAULT (CURRENT_DATE)
) ENGINE=InnoDB;

-- 6. Phong
CREATE TABLE Phong (
    maPhong VARCHAR(20) PRIMARY KEY,
    soPhong VARCHAR(100) NOT NULL,
    trangThai VARCHAR(50) DEFAULT 'Trống',
    maLoaiPhong VARCHAR(20) NOT NULL,
    tang INT NOT NULL,
    tinhTrang VARCHAR(55) DEFAULT 'Tốt',
    moTa TEXT,
    CONSTRAINT FK_Phong_LoaiPhong FOREIGN KEY (maLoaiPhong) REFERENCES LoaiPhong(maLoaiPhong)
) ENGINE=InnoDB;

-- 7. LoaiDatPhong
CREATE TABLE LoaiDatPhong (
    maLoaiDatPhong VARCHAR(20) PRIMARY KEY,
    tenLoaiDatPhong VARCHAR(100) NOT NULL,
    ngayTao DATE DEFAULT (CURRENT_DATE)
) ENGINE=InnoDB;

-- 8. DichVu
CREATE TABLE DichVu (
    maDichVu VARCHAR(20) PRIMARY KEY,
    tenDichVu VARCHAR(100) NOT NULL,
    gia DECIMAL(18, 2) NOT NULL,
    moTa VARCHAR(255),
    donViTinh VARCHAR(50)
) ENGINE=InnoDB;

-- 9. DichVu_LoaiPhong
CREATE TABLE DichVu_LoaiPhong (
    maDichVu VARCHAR(20),
    maLoaiPhong VARCHAR(20),
    PRIMARY KEY (maLoaiPhong, maDichVu),
    CONSTRAINT FK_DVLP_LoaiPhong FOREIGN KEY (maLoaiPhong) REFERENCES LoaiPhong(maLoaiPhong),
    CONSTRAINT FK_DVLP_DichVu FOREIGN KEY (maDichVu) REFERENCES DichVu(maDichVu)
) ENGINE=InnoDB;

-- 10. KhachHang
CREATE TABLE KhachHang (
    maKhachHang VARCHAR(20) PRIMARY KEY,
    CCCD VARCHAR(20) UNIQUE NOT NULL,
    hoTen VARCHAR(100) NOT NULL,
    soDienThoai VARCHAR(20) NOT NULL,
    email VARCHAR(100),
    ngayTao DATE DEFAULT (CURRENT_DATE)
) ENGINE=InnoDB;

-- 11. PhieuDatPhong
CREATE TABLE PhieuDatPhong (
    maPhieuDatPhong VARCHAR(20) PRIMARY KEY,
    ngayTao DATE DEFAULT (CURRENT_DATE),
    maKhachHang VARCHAR(20) NOT NULL,
    trangThai VARCHAR(20) DEFAULT 'Đã đặt',
    tienDatCoc DECIMAL(18, 2) DEFAULT 0,
    CONSTRAINT FK_PDP_KhachHang FOREIGN KEY (maKhachHang) REFERENCES KhachHang(maKhachHang)
) ENGINE=InnoDB;

-- 12. ChiTietPhieuDatPhong
CREATE TABLE ChiTietPhieuDatPhong (
    maPhieuDatPhong VARCHAR(20),
    maPhong VARCHAR(20),
    thoiGianNhanPhong DATETIME NOT NULL,
    thoiGianTraPhong DATETIME NOT NULL,
    maLoaiDatPhong VARCHAR(20) NOT NULL,
    soNguoi INT DEFAULT 1,
    trangThai VARCHAR(50) DEFAULT 'Đã đặt',
    PRIMARY KEY (maPhieuDatPhong, maPhong),
    CONSTRAINT FK_CTPDP_PDP FOREIGN KEY (maPhieuDatPhong) REFERENCES PhieuDatPhong(maPhieuDatPhong),
    CONSTRAINT FK_CTPDP_Phong FOREIGN KEY (maPhong) REFERENCES Phong(maPhong),
    CONSTRAINT FK_CTPDP_LDP FOREIGN KEY (maLoaiDatPhong) REFERENCES LoaiDatPhong(maLoaiDatPhong),
    CONSTRAINT CK_CTPDP_Time CHECK (thoiGianTraPhong > thoiGianNhanPhong)
) ENGINE=InnoDB;

-- 13. ChiTietPhieuDatPhong_DichVu
CREATE TABLE ChiTietPhieuDatPhong_DichVu (
    maPhieuDatPhong VARCHAR(20),
    maPhong VARCHAR(20),
    maDichVu VARCHAR(20),
    soLuong INT DEFAULT 1,
    PRIMARY KEY (maPhieuDatPhong, maPhong, maDichVu),
    CONSTRAINT FK_CTPDPDV_CTPDP FOREIGN KEY (maPhieuDatPhong, maPhong) REFERENCES ChiTietPhieuDatPhong(maPhieuDatPhong, maPhong),
    CONSTRAINT FK_CTPDPDV_DV FOREIGN KEY (maDichVu) REFERENCES DichVu(maDichVu)
) ENGINE=InnoDB;

-- 14. KhuyenMai
CREATE TABLE KhuyenMai (
    maKhuyenMai VARCHAR(20) PRIMARY KEY,
    tenKhuyenMai VARCHAR(100) NOT NULL,
    ngayBatDau DATETIME NOT NULL,
    ngayKetThuc DATETIME NOT NULL,
    trangThai VARCHAR(50),
    heSo FLOAT NOT NULL,
    tongTienToiThieu DECIMAL(18, 2) DEFAULT 0,
    tongKhuyenMaiToiDa DECIMAL(18, 2) DEFAULT 0,
    CONSTRAINT CK_KhuyenMai_Ngay CHECK (ngayKetThuc >= ngayBatDau),
    CONSTRAINT CK_KhuyenMai_HeSo CHECK (heSo >= 0 AND heSo <= 1)
) ENGINE=InnoDB;

-- 15. HoaDon
CREATE TABLE HoaDon (
    maHoaDon VARCHAR(20) PRIMARY KEY,
    ngayDat DATETIME DEFAULT CURRENT_TIMESTAMP,
    maKhachHang VARCHAR(20) NOT NULL,
    maNhanVien VARCHAR(20) NOT NULL,
    maKhuyenMai VARCHAR(20),
    ngayTao DATETIME DEFAULT CURRENT_TIMESTAMP,
    trangThai VARCHAR(50) DEFAULT 'Chưa thanh toán',
    tongTien DECIMAL(18, 2) DEFAULT 0,
    CONSTRAINT FK_HoaDon_KhachHang FOREIGN KEY (maKhachHang) REFERENCES KhachHang(maKhachHang),
    CONSTRAINT FK_HoaDon_NhanVien FOREIGN KEY (maNhanVien) REFERENCES NhanVien(maNhanVien),
    CONSTRAINT FK_HoaDon_KhuyenMai FOREIGN KEY (maKhuyenMai) REFERENCES KhuyenMai(maKhuyenMai)
) ENGINE=InnoDB;

-- 16. ChiTietHoaDon
CREATE TABLE ChiTietHoaDon (
    maHoaDon VARCHAR(20),
    maPhieuDatPhong VARCHAR(20),
    maPhong VARCHAR(20),
    ngayTao DATETIME DEFAULT CURRENT_TIMESTAMP,
    tongTien DECIMAL(18, 2) DEFAULT 0,
    PRIMARY KEY (maHoaDon, maPhieuDatPhong, maPhong),
    CONSTRAINT FK_CTHD_HoaDon FOREIGN KEY (maHoaDon) REFERENCES HoaDon(maHoaDon),
    CONSTRAINT FK_CTHD_CTPDP FOREIGN KEY (maPhieuDatPhong, maPhong) REFERENCES ChiTietPhieuDatPhong(maPhieuDatPhong, maPhong)
) ENGINE=InnoDB;

-- 17. ChiTietHoaDon_DichVu
CREATE TABLE ChiTietHoaDon_DichVu (
    maHoaDon VARCHAR(20),
    maPhieuDatPhong VARCHAR(20),
    maPhong VARCHAR(20),
    maDichVu VARCHAR(20),
    PRIMARY KEY (maHoaDon, maPhieuDatPhong, maPhong, maDichVu),
    CONSTRAINT FK_CTHDDV_CTHD FOREIGN KEY (maHoaDon, maPhieuDatPhong, maPhong) REFERENCES ChiTietHoaDon(maHoaDon, maPhieuDatPhong, maPhong),
    CONSTRAINT FK_CTHDDV_DV FOREIGN KEY (maDichVu) REFERENCES DichVu(maDichVu)
) ENGINE=InnoDB;

-- 18. HuyPhong
CREATE TABLE HuyPhong (
    maHuyPhong INT AUTO_INCREMENT PRIMARY KEY,
    maPhieuDatPhong VARCHAR(20) NOT NULL,
    lyDo VARCHAR(255),
    ngayHuy DATETIME DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT FK_HuyPhong_PDP FOREIGN KEY (maPhieuDatPhong) REFERENCES PhieuDatPhong(maPhieuDatPhong)
) ENGINE=InnoDB;

-- ===========================
-- TẠO INDEX
-- ===========================
CREATE INDEX IX_NhanVien_TrangThai ON NhanVien(trangThai);
CREATE INDEX IX_Phong_TrangThai ON Phong(trangThai);
CREATE INDEX IX_HoaDon_NgayTao ON HoaDon(ngayTao);

-- ===========================
-- THÊM DỮ LIỆU MẪU
-- ===========================
INSERT INTO TaiKhoan (tenDangNhap, matKhau, vaiTro) VALUES
('0365271959', '$2a$10$9eypGo00I/fQB.j2X0ZyB.adLUV9/Kj/Mt8RUziE6UoFwTCYd.Ivi', 'admin'),
('0365271958', '$2a$10$Wzi1CNeJidRvX5Q4SRdbneW0VZmk1T1i6mrv1Hh7vIi6OMQdGwu..', 'employee');

INSERT INTO NhanVien (maNhanVien, CCCD, tenNhanVien, gioiTinh, ngaySinh, email, soDienThoai, tenDangNhap, diaChi) VALUES
('NV001', '042204003399', 'Nguyễn Huy Hoàng', 1, '2004-08-27', 'nguyenhuyhoang270804@gmail.com', '0901234567', '0365271959', 'Gò Vấp, HCM');

INSERT INTO LoaiPhong (maLoaiPhong, tenLoaiPhong, gia, soNguoiLonToiDa, soTreEmToiDa) VALUES
('LP01', 'Phòng đơn', 60000, 2, 1),
('LP02', 'Phòng đôi', 80000, 3, 2);

INSERT INTO Phong (maPhong, soPhong, trangThai, maLoaiPhong, tang, tinhTrang) VALUES
('P-0001', '101', 'Trống', 'LP01', 1, 'Tốt'),
('P-0002', '102', 'Trống', 'LP02', 1, 'Tốt');