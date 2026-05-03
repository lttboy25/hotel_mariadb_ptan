package iuh.mapper;

import iuh.dto.*;
import iuh.entity.*;
import iuh.enums.TinhTrangPhong;
import iuh.enums.TrangThaiPhong;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class Mapper {

    // ==================== KhachHang ====================
    public static KhachHang map(KhachHangDTO dto) {
        if (dto == null) return null;
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
        if (entity == null) return null;
        return KhachHangDTO.builder()
                .maKhachHang(entity.getMaKhachHang())
                .CCCD(entity.getCCCD())
                .tenKhachHang(entity.getTenKhachHang())
                .soDienThoai(entity.getSoDienThoai())
                .email(entity.getEmail())
                .ngayTao(entity.getNgayTao())
                .build();
    }

    // ==================== TaiKhoan ====================
    public static TaiKhoan map(TaiKhoanDTO dto) {
        if (dto == null) return null;
        return TaiKhoan.builder()
                .maNhanVien(dto.getMaNhanVien())
                .matKhau(dto.getMatKhau())
                .vaiTro(dto.getVaiTro())
                // nhanVien không set để tránh vòng lặp — JPA tự quản lý qua @MapsId
                .build();
    }

    public static TaiKhoanDTO map(TaiKhoan entity) {
        if (entity == null) return null;
        return TaiKhoanDTO.builder()
                .maNhanVien(entity.getMaNhanVien())
                .matKhau(entity.getMatKhau())
                .vaiTro(entity.getVaiTro())
                .build();
    }

    // ==================== NhanVien ====================
    public static NhanVien map(NhanVienDTO dto) {
        if (dto == null) return null;
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
                // TaiKhoan không set nhanVien ngược lại để tránh vòng lặp
                .taiKhoan(map(dto.getTaiKhoan()))
                .build();
    }

    public static NhanVienDTO map(NhanVien entity) {
        if (entity == null) return null;
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
                .taiKhoan(map(entity.getTaiKhoan()))
                .build();
    }

    // ==================== LoaiPhong ====================
    public static LoaiPhong map(LoaiPhongDTO dto) {
        if (dto == null) return null;
        return LoaiPhong.builder()
                .maLoaiPhong(dto.getMaLoaiPhong())
                .tenLoaiPhong(dto.getTenLoaiPhong())
                .gia(dto.getGia())
                .ngayTao(dto.getNgayTao())
                .soNguoiLonToiDa(dto.getSoNguoiLonToiDa())
                .soTreEmToiDa(dto.getSoTreEmToiDa())
                .build();
    }

    public static LoaiPhongDTO map(LoaiPhong entity) {
        if (entity == null) return null;
        return LoaiPhongDTO.builder()
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
        if (dto == null) return null;
        return Phong.builder()
                .maPhong(dto.getMaPhong())
                .soPhong(dto.getSoPhong())
                .loaiPhong(map(dto.getLoaiPhong()))
                // valueOf không cần thiết vì cùng enum type — gán trực tiếp
                .trangThai(dto.getTrangThai())
                .tang(dto.getTang())
                .tinhTrang(dto.getTinhTrang())
                .moTa(dto.getMoTa())
                .build();
    }

    public static PhongDTO map(Phong entity) {
        if (entity == null) return null;
        return PhongDTO.builder()
                .maPhong(entity.getMaPhong())
                .soPhong(entity.getSoPhong())
                .loaiPhong(map(entity.getLoaiPhong()))
                .trangThai(entity.getTrangThai())
                .tang(entity.getTang())
                .tinhTrang(entity.getTinhTrang())
                .moTa(entity.getMoTa())
                .build();
    }

    // ==================== KhuyenMai ====================
    public static KhuyenMai map(KhuyenMaiDTO dto) {
        if (dto == null) return null;
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
        if (entity == null) return null;
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
        if (dto == null) return null;
        return HoaDon.builder()
                .maHoaDon(dto.getMaHoaDon())
                .ngayDat(dto.getNgayDat())
                .khachHang(map(dto.getKhachHang()))
                .nhanVien(map(dto.getNhanVien()))
                .khuyenMai(map(dto.getKhuyenMai()))
                .ngayTao(dto.getNgayTao())
                .trangThai(dto.getTrangThai())
                .tongTien(dto.getTongTien())
                .tienKhachDua(dto.getTienKhachDua())
                .tienThoi(dto.getTienThoi())
                // chiTietHoaDon không map ngược để tránh cascade phức tạp
                .build();
    }

    public static HoaDonDTO map(HoaDon entity) {
        if (entity == null) return null;
        return HoaDonDTO.builder()
                .maHoaDon(entity.getMaHoaDon())
                .ngayDat(entity.getNgayDat())
                .khachHang(map(entity.getKhachHang()))
                .nhanVien(map(entity.getNhanVien()))
                .khuyenMai(map(entity.getKhuyenMai()))
                .ngayTao(entity.getNgayTao())
                .trangThai(entity.getTrangThai())
                .tongTien(entity.getTongTien())
                .tienKhachDua(entity.getTienKhachDua())
                .tienThoi(entity.getTienThoi())
                .chiTietHoaDon(entity.getChiTietHoaDon() != null
                        ? entity.getChiTietHoaDon().stream()
                        .map(Mapper::map)
                        .collect(Collectors.toList())
                        : new ArrayList<>())
                .build();
    }

    // ==================== ChiTietHoaDon ====================
    public static ChiTietHoaDon map(ChiTietHoaDonDTO dto) {
        if (dto == null) return null;
        return ChiTietHoaDon.builder()
                .id(dto.getId())
                .hoaDon(map(dto.getHoaDon()))
                .chiTietPhieuDatPhong(map(dto.getChiTietPhieuDatPhong()))
                .phong(map(dto.getPhong()))
                .ngayTao(dto.getNgayTao())
                .tongTien(dto.getTongTien())
                .build();
    }

    public static ChiTietHoaDonDTO map(ChiTietHoaDon entity) {
        if (entity == null) return null;
        return ChiTietHoaDonDTO.builder()
                .id(entity.getId())
                .hoaDon(map(entity.getHoaDon()))
                .chiTietPhieuDatPhong(map(entity.getChiTietPhieuDatPhong()))
                .phong(map(entity.getPhong()))
                .ngayTao(entity.getNgayTao())
                .tongTien(entity.getTongTien())
                .build();
    }

    // ==================== PhieuDatPhong ====================
    /**
     * DTO → Entity: KHÔNG map dsachPhieuDatPhong để tránh đệ quy
     * ChiTietPhieuDatPhong → PhieuDatPhong → ChiTietPhieuDatPhong → ...
     * Khi cần persist chi tiết, set phieuDatPhong trực tiếp trên entity.
     */
    public static PhieuDatPhong map(PhieuDatPhongDTO dto) {
        if (dto == null) return null;
        return PhieuDatPhong.builder()
                .maPhieuDatPhong(dto.getMaPhieuDatPhong())
                .ngayTao(dto.getNgayTao())
                .trangThai(dto.getTrangThai())
                .tienDatCoc(dto.getTienDatCoc())
                .khachHang(map(dto.getKhachHang()))
                // Không map dsachPhieuDatPhong — tránh StackOverflow
                .dsachPhieuDatPhong(new ArrayList<>())
                .build();
    }

    public static PhieuDatPhongDTO map(PhieuDatPhong entity) {
        if (entity == null) return null;
        return PhieuDatPhongDTO.builder()
                .maPhieuDatPhong(entity.getMaPhieuDatPhong())
                .ngayTao(entity.getNgayTao())
                .trangThai(entity.getTrangThai())
                .tienDatCoc(entity.getTienDatCoc())
                .khachHang(map(entity.getKhachHang()))
                // Không load LAZY collection — tránh LazyInitializationException
                .dsachPhieuDatPhong(new ArrayList<>())
                .build();
    }

    // ==================== ChiTietPhieuDatPhong ====================
    public static ChiTietPhieuDatPhong map(ChiTietPhieuDatPhongDTO dto) {
        if (dto == null) return null;
        return ChiTietPhieuDatPhong.builder()
                .id(dto.getId())
                // PhieuDatPhong.dsachPhieuDatPhong sẽ là [] — an toàn, không đệ quy
                .phieuDatPhong(map(dto.getPhieuDatPhong()))
                .phong(map(dto.getPhong()))
                .trangThai(dto.getTrangThai())
                .soGioLuuTru(dto.getSoGioLuuTru())
                .thoiGianNhanPhong(dto.getThoiGianNhanPhong())
                .thoiGianTraPhong(dto.getThoiGianTraPhong())
                .soNguoi(dto.getSoNguoi())
                // phieuHuyPhong không map — DTO đang giữ entity type (cần refactor DTO)
                .build();
    }

    public static ChiTietPhieuDatPhongDTO map(ChiTietPhieuDatPhong entity) {
        if (entity == null) return null;
        return ChiTietPhieuDatPhongDTO.builder()
                .id(entity.getId())
                .phieuDatPhong(map(entity.getPhieuDatPhong()))
                .phong(map(entity.getPhong()))
                .trangThai(entity.getTrangThai())
                .soGioLuuTru(entity.getSoGioLuuTru())
                .thoiGianNhanPhong(entity.getThoiGianNhanPhong())
                .thoiGianTraPhong(entity.getThoiGianTraPhong())
                .soNguoi(entity.getSoNguoi())
                // phieuHuyPhong bỏ qua — tránh vòng lặp entity↔entity
                .build();
    }

    // ==================== CaLamViecNhanVien ====================
    /**
     * DTO → Entity: ca và nhanVien KHÔNG thể set từ DTO vì chỉ có mã (String),
     * không có full entity. Service phải fetch Ca và NhanVien từ DB rồi set thủ công.
     * Method này chỉ dùng để đọc các field scalar.
     */
    public static CaLamViecNhanVien map(CaLamViecNhanVienDTO dto) {
        if (dto == null) return null;
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
                // ca và nhanVien = null — service tự set sau khi fetch DB
                .build();
    }

    public static CaLamViecNhanVienDTO map(CaLamViecNhanVien entity) {
        if (entity == null) return null;
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
    public static DatPhongResultDTO mapToDatPhongResult(PhieuDatPhong phieuDatPhong,
                                                        DatPhongRequestDTO request) {
        if (phieuDatPhong == null) return null;
        return DatPhongResultDTO.builder()
                .maPhieuDatPhong(phieuDatPhong.getMaPhieuDatPhong())
                .maKhachHang(phieuDatPhong.getKhachHang() != null
                        ? phieuDatPhong.getKhachHang().getMaKhachHang() : null)
                .trangThai(phieuDatPhong.getTrangThai())
                .tienDatCoc(phieuDatPhong.getTienDatCoc())
                .ngayTao(phieuDatPhong.getNgayTao())
                .checkIn(request != null ? request.getCheckIn() : null)
                .checkOut(request != null ? request.getCheckOut() : null)
                .maPhongs(request != null ? request.getMaPhongs() : null)
                .build();
    }
}