package iuh.mapper;

import iuh.dto.*;
import iuh.entity.*;
import iuh.enums.TinhTrangPhong;
import iuh.enums.TrangThaiPhong;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Mapper {

    // ==================== KhachHang ====================
    public static KhachHang map(KhachHangDTO dto) {
        if (dto == null)
            return null;
        return KhachHang.builder()
                .maKhachHang(dto.getMaKhachHang())
                .CCCD(dto.getCCCD())
                .tenKhachHang(dto.getTenKhachHang())
                .soDienThoai(dto.getSoDienThoai())
                .email(dto.getEmail())
                .ngayTao(dto.getNgayTao())
                .build();
    }

    public static KhachHangDTO map(KhachHang entity) {
        if (entity == null)
            return null;
        return KhachHangDTO.builder()
                .maKhachHang(entity.getMaKhachHang())
                .CCCD(entity.getCCCD())
                .tenKhachHang(entity.getTenKhachHang())
                .soDienThoai(entity.getSoDienThoai())
                .email(entity.getEmail())
                .ngayTao(entity.getNgayTao())
                .build();
    }

    // ==================== NhanVien ====================
    public static NhanVien map(NhanVienDTO dto) {
        if (dto == null)
            return null;
        return NhanVien.builder()
                .maNhanVien(dto.getMaNhanVien())
                .CCCD(dto.getCCCD())
                .tenNhanVien(dto.getTenNhanVien())
                .gioiTinh(dto.isGioiTinh())
                .ngaySinh(dto.getNgaySinh())
                .email(dto.getEmail())
                .soDienThoai(dto.getSoDienThoai())
                .ngayBatDau(dto.getNgayBatDau())
                .trangThai(dto.getTrangThai())
                .diaChi(dto.getDiaChi())
                .taiKhoan(dto.getTaiKhoan() != null ? map(dto.getTaiKhoan()) : null)
                .build();
    }

    public static NhanVienDTO map(NhanVien entity) {
        if (entity == null)
            return null;
        return NhanVienDTO.builder()
                .maNhanVien(entity.getMaNhanVien())
                .CCCD(entity.getCCCD())
                .tenNhanVien(entity.getTenNhanVien())
                .gioiTinh(entity.isGioiTinh())
                .ngaySinh(entity.getNgaySinh())
                .email(entity.getEmail())
                .soDienThoai(entity.getSoDienThoai())
                .ngayBatDau(entity.getNgayBatDau())
                .trangThai(entity.getTrangThai())
                .diaChi(entity.getDiaChi())
                .taiKhoan(entity.getTaiKhoan() != null ? map(entity.getTaiKhoan()) : null)
                .build();
    }

    // ==================== TaiKhoan ====================
    public static TaiKhoan map(TaiKhoanDTO dto) {
        if (dto == null)
            return null;
        return TaiKhoan.builder()
                .maNhanVien(dto.getMaNhanVien())
                .matKhau(dto.getMatKhau())
                .vaiTro(dto.getVaiTro())
                .build();
    }

    public static TaiKhoanDTO map(TaiKhoan entity) {
        if (entity == null)
            return null;
        return TaiKhoanDTO.builder()
                .maNhanVien(entity.getMaNhanVien())
                .matKhau(entity.getMatKhau())
                .vaiTro(entity.getVaiTro())
                .build();
    }

    // ==================== LoaiPhong ====================
    public static LoaiPhongDTO map(LoaiPhong entity) {
        if (entity == null)
            return null;
        return LoaiPhongDTO.builder()
                .maLoaiPhong(entity.getMaLoaiPhong())
                .tenLoaiPhong(entity.getTenLoaiPhong())
                .gia(entity.getGia())
                .ngayTao(entity.getNgayTao())
                .soNguoiLonToiDa(entity.getSoNguoiLonToiDa())
                .soTreEmToiDa(entity.getSoTreEmToiDa())
                .build();
    }

    public static LoaiPhong map(LoaiPhongDTO entity) {
        if (entity == null)
            return null;
        return LoaiPhong.builder()
                .maLoaiPhong(entity.getMaLoaiPhong())
                .tenLoaiPhong(entity.getTenLoaiPhong())
                .gia(entity.getGia())
                .ngayTao(entity.getNgayTao())
                .soNguoiLonToiDa(entity.getSoNguoiLonToiDa())
                .soTreEmToiDa(entity.getSoTreEmToiDa())
                .build();
    }

    // ==================== Phong ====================
    public static Phong map(PhongDTO dto) {
        if (dto == null)
            return null;

        LoaiPhong lp = map(dto.getLoaiPhong());
        return Phong.builder()
                .maPhong(dto.getMaPhong())
                .soPhong(dto.getSoPhong())
                .loaiPhong(lp)
                .trangThai(dto.getTrangThai() != null ? TrangThaiPhong.valueOf(dto.getTrangThai().name())
                        : null)
                .tang(dto.getTang())
                .tinhTrang(dto.getTinhTrang() != null ? TinhTrangPhong.valueOf(dto.getTinhTrang().name())
                        : null)
                .moTa(dto.getMoTa())
                .build();
    }

    public static PhongDTO map(Phong dto) {
        if (dto == null)
            return null;

        LoaiPhongDTO lp = map(dto.getLoaiPhong());

        return PhongDTO.builder()
                .maPhong(dto.getMaPhong())
                .soPhong(dto.getSoPhong())
                .loaiPhong(lp)
                .trangThai(
                        dto.getTrangThai() != null ? TrangThaiPhong.valueOf(dto.getTrangThai().name()) : null)
                .tang(dto.getTang())
                .tinhTrang(
                        dto.getTinhTrang() != null ? TinhTrangPhong.valueOf(dto.getTinhTrang().name()) : null)
                .moTa(dto.getMoTa())
                .build();
    }

    // ==================== KhuyenMai ====================
    public static KhuyenMai map(KhuyenMaiDTO dto) {
        if (dto == null)
            return null;
        return KhuyenMai.builder()
                .maKhuyenMai(dto.getMaKhuyenMai())
                .tenKhuyenMai(dto.getTenKhuyenMai())
                .ngayBatDau(dto.getNgayBatDau())
                .ngayKetThuc(dto.getNgayKetThuc())
                .trangThai(dto.getTrangThai())
                .heSo(dto.getHeSo())
                .tongTienToiThieu(dto.getTongTienToiThieu())
                .tongKhuyenMaiToiDa(dto.getTongKhuyenMaiToiDa())
                .build();
    }

    public static KhuyenMaiDTO map(KhuyenMai entity) {
        if (entity == null)
            return null;
        return KhuyenMaiDTO.builder()
                .maKhuyenMai(entity.getMaKhuyenMai())
                .tenKhuyenMai(entity.getTenKhuyenMai())
                .ngayBatDau(entity.getNgayBatDau())
                .ngayKetThuc(entity.getNgayKetThuc())
                .trangThai(entity.getTrangThai())
                .heSo(entity.getHeSo())
                .tongTienToiThieu(entity.getTongTienToiThieu())
                .tongKhuyenMaiToiDa(entity.getTongKhuyenMaiToiDa())
                .build();
    }

    // ==================== HoaDon ====================
    public static HoaDon map(HoaDonDTO dto) {
        if (dto == null)
            return null;
        return HoaDon.builder()
                .maHoaDon(dto.getMaHoaDon())
                .ngayDat(dto.getNgayDat())
                .khachHang(dto.getKhachHang() != null ? map(dto.getKhachHang()) : null)
                .nhanVien(dto.getNhanVien() != null ? map(dto.getNhanVien()) : null)
                .khuyenMai(dto.getKhuyenMai() != null ? map(dto.getKhuyenMai()) : null)
                .ngayTao(dto.getNgayTao())
                .trangThai(dto.getTrangThai())
                .tongTien(dto.getTongTien())
                .tienKhachDua(dto.getTienKhachDua())
                .tienThoi(dto.getTienThoi())
                .build();
    }

    public static HoaDonDTO map(HoaDon entity) {
        if (entity == null)
            return null;
        return HoaDonDTO.builder()
                .maHoaDon(entity.getMaHoaDon())
                .ngayDat(entity.getNgayDat())
                .khachHang(entity.getKhachHang() != null ? map(entity.getKhachHang()) : null)
                .nhanVien(entity.getNhanVien() != null ? map(entity.getNhanVien()) : null)
                .khuyenMai(entity.getKhuyenMai() != null ? map(entity.getKhuyenMai()) : null)
                .ngayTao(entity.getNgayTao())
                .trangThai(entity.getTrangThai())
                .tongTien(entity.getTongTien())
                .tienKhachDua(entity.getTienKhachDua())
                .tienThoi(entity.getTienThoi())
                .chiTietHoaDon(entity.getChiTietHoaDon())
                .build();
    }

    // ==================== ChiTietHoaDon ====================
    public static ChiTietHoaDonDTO map(ChiTietHoaDon entity) {
        if (entity == null)
            return null;
        return ChiTietHoaDonDTO.builder()
                .id(entity.getId())
                .hoaDon(entity.getHoaDon() != null ? map(entity.getHoaDon()) : null)
                .chiTietPhieuDatPhong(
                        entity.getChiTietPhieuDatPhong() != null ? map(entity.getChiTietPhieuDatPhong()) : null)
                .phong(entity.getPhong() != null ? map(entity.getPhong()) : null)
                .ngayTao(entity.getNgayTao())
                .tongTien(entity.getTongTien())
                .build();
    }

    public static ChiTietHoaDon map(ChiTietHoaDonDTO entity) {
        if (entity == null)
            return null;
        return ChiTietHoaDon.builder()
                .id(entity.getId())
                .hoaDon(entity.getHoaDon() != null ? map(entity.getHoaDon()) : null)
                .chiTietPhieuDatPhong(
                        entity.getChiTietPhieuDatPhong() != null ? map(entity.getChiTietPhieuDatPhong()) : null)
                .phong(entity.getPhong() != null ? map(entity.getPhong()) : null)
                .ngayTao(entity.getNgayTao())
                .tongTien(entity.getTongTien())
                .build();
    }

    // ==================== PhieuDatPhong ====================
    public static PhieuDatPhong map(PhieuDatPhongDTO dto) {
        if (dto == null)
            return null;
        return PhieuDatPhong.builder()
                .maPhieuDatPhong(dto.getMaPhieuDatPhong())
                .ngayTao(dto.getNgayTao())
                .trangThai(dto.getTrangThai())
                .tienDatCoc(dto.getTienDatCoc())
                .khachHang(dto.getKhachHang() != null ? map(dto.getKhachHang()) : null)
                .dsachPhieuDatPhong(dto.getDsachPhieuDatPhong() != null
                        ? dto.getDsachPhieuDatPhong().stream()
                                .map(ct -> map(ct)).collect(Collectors.toList())
                        : new ArrayList<>())
                .build();
    }

    public static PhieuDatPhongDTO map(PhieuDatPhong entity) {
        if (entity == null)
            return null;
        return PhieuDatPhongDTO.builder()
                .maPhieuDatPhong(entity.getMaPhieuDatPhong())
                .ngayTao(entity.getNgayTao())
                .trangThai(entity.getTrangThai())
                .tienDatCoc(entity.getTienDatCoc())
                .khachHang(entity.getKhachHang() != null ? map(entity.getKhachHang()) : null)
                .dsachPhieuDatPhong(entity.getDsachPhieuDatPhong() != null
                        ? entity.getDsachPhieuDatPhong().stream()
                                .map(ct -> map(ct)).collect(Collectors.toList())
                        : new ArrayList<>())
                .build();
    }

    // ==================== ChiTietPhieuDatPhong ====================
    public static ChiTietPhieuDatPhong map(ChiTietPhieuDatPhongDTO entity) {
        if (entity == null)
            return null;
        return ChiTietPhieuDatPhong.builder()
                .id(entity.getId())
                .phieuDatPhong(entity.getPhieuDatPhong() != null ? map(entity.getPhieuDatPhong()) : null)
                .phong(entity.getPhong())
                .trangThai(entity.getTrangThai())
                .soGioLuuTru(entity.getSoGioLuuTru())
                .thoiGianNhanPhong(entity.getThoiGianNhanPhong())
                .thoiGianTraPhong(entity.getThoiGianTraPhong())
                .soNguoi(entity.getSoNguoi())
                .build();
    }

    public static ChiTietPhieuDatPhongDTO map(ChiTietPhieuDatPhong entity) {
        if (entity == null)
            return null;
        return ChiTietPhieuDatPhongDTO.builder()
                .id(entity.getId())
                .phieuDatPhong(entity.getPhieuDatPhong() != null ? map(entity.getPhieuDatPhong()) : null)
                .phong(entity.getPhong())
                .trangThai(entity.getTrangThai())
                .soGioLuuTru(entity.getSoGioLuuTru())
                .thoiGianNhanPhong(entity.getThoiGianNhanPhong())
                .thoiGianTraPhong(entity.getThoiGianTraPhong())
                .soNguoi(entity.getSoNguoi())
                .build();
    }

    // ==================== CaLamViecNhanVien ====================
    public static CaLamViecNhanVien map(CaLamViecNhanVienDTO dto) {
        if (dto == null)
            return null;
        return CaLamViecNhanVien.builder()
                .maCaLamViec(dto.getMaCaLamViec())
                .tienMoCa(dto.getTienMoCa())
                .tienKetCa(dto.getTienKetCa())
                .trangThai(dto.getTrangThai())
                .tongChi(dto.getTongChi())
                .tongThu(dto.getTongThu())
                .ngay(dto.getNgay())
                .thoiGianBatDau(dto.getThoiGianBatDau())
                .thoiGianKetThuc(dto.getThoiGianKetThuc())
                .build();
    }

    public static CaLamViecNhanVienDTO map(CaLamViecNhanVien entity) {
        if (entity == null)
            return null;
        return CaLamViecNhanVienDTO.builder()
                .maCaLamViec(entity.getMaCaLamViec())
                .tienMoCa(entity.getTienMoCa())
                .tienKetCa(entity.getTienKetCa())
                .trangThai(entity.getTrangThai())
                .tongChi(entity.getTongChi())
                .tongThu(entity.getTongThu())
                .maCa(entity.getCa() != null ? entity.getCa().getMaCa() : null)
                .maNhanVien(entity.getNhanVien() != null ? entity.getNhanVien().getMaNhanVien() : null)
                .tenNhanVien(entity.getNhanVien() != null ? entity.getNhanVien().getTenNhanVien() : null)
                .ngay(entity.getNgay())
                .thoiGianBatDau(entity.getThoiGianBatDau())
                .thoiGianKetThuc(entity.getThoiGianKetThuc())
                .build();
    }

    // ==================== DatPhongResultDTO ====================
    public static DatPhongResultDTO mapToDatPhongResult(PhieuDatPhong phieuDatPhong, DatPhongRequestDTO request) {
        if (phieuDatPhong == null)
            return null;
        return DatPhongResultDTO.builder()
                .maPhieuDatPhong(phieuDatPhong.getMaPhieuDatPhong())
                .maKhachHang(
                        phieuDatPhong.getKhachHang() != null ? phieuDatPhong.getKhachHang().getMaKhachHang() : null)
                .trangThai(phieuDatPhong.getTrangThai())
                .tienDatCoc(phieuDatPhong.getTienDatCoc())
                .ngayTao(phieuDatPhong.getNgayTao())
                .checkIn(request != null ? request.getCheckIn() : null)
                .checkOut(request != null ? request.getCheckOut() : null)
                .maPhongs(request != null ? request.getMaPhongs() : null)
                .build();
    }
}
