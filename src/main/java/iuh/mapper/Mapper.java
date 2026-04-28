package iuh.mapper;

import iuh.dto.*;
import iuh.entity.*;

public class Mapper {

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

    public static DatPhongResultDTO mapToDatPhongResult(PhieuDatPhong phieuDatPhong, DatPhongRequestDTO request) {
        if (phieuDatPhong == null) return null;
        return DatPhongResultDTO.builder()
                .maPhieuDatPhong(phieuDatPhong.getMaPhieuDatPhong())
                .maKhachHang(phieuDatPhong.getKhachHang() != null ? phieuDatPhong.getKhachHang().getMaKhachHang() : null)
                .trangThai(phieuDatPhong.getTrangThai())
                .tienDatCoc(phieuDatPhong.getTienDatCoc())
                .ngayTao(phieuDatPhong.getNgayTao())
                .checkIn(request != null ? request.getCheckIn() : null)
                .checkOut(request != null ? request.getCheckOut() : null)
                .maPhongs(request != null ? request.getMaPhongs() : null)
                .build();
    }

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

    public static HoaDon map(HoaDonDTO hoaDonDTO) {
        if (hoaDonDTO == null) return null;
        return HoaDon
                .builder()
                .maHoaDon(hoaDonDTO.getMaHoaDon())
                .ngayDat(hoaDonDTO.getNgayDat())
                .khachHang(hoaDonDTO.getKhachHang())
                .nhanVien(hoaDonDTO.getNhanVien())
                .khuyenMai(hoaDonDTO.getKhuyenMai())
                .ngayTao(hoaDonDTO.getNgayTao())
                .trangThai(hoaDonDTO.getTrangThai())
                .tongTien(hoaDonDTO.getTongTien())
                .chiTietHoaDon(hoaDonDTO.getChiTietHoaDon())
                .tienKhachDua(hoaDonDTO.getTienKhachDua())
                .tienThoi(hoaDonDTO.getTienThoi())
                .build();
    }

    public static HoaDonDTO map(HoaDon hoaDon) {
        if (hoaDon == null) return null;
        return HoaDonDTO
                .builder()
                .maHoaDon(hoaDon.getMaHoaDon())
                .ngayDat(hoaDon.getNgayDat())
                .khachHang(hoaDon.getKhachHang())
                .nhanVien(hoaDon.getNhanVien())
                .khuyenMai(hoaDon.getKhuyenMai())
                .ngayTao(hoaDon.getNgayTao())
                .trangThai(hoaDon.getTrangThai())
                .tongTien(hoaDon.getTongTien())
                .chiTietHoaDon(hoaDon.getChiTietHoaDon())
                .tienKhachDua(hoaDon.getTienKhachDua())
                .tienThoi(hoaDon.getTienThoi())
                .build();
    }

    public static ChiTietHoaDonDTO map(ChiTietHoaDon entity) {
        if (entity == null) return null;
        return ChiTietHoaDonDTO.builder()
                .id(entity.getId())
                .ngayTao(entity.getNgayTao())
                .tongTien(entity.getTongTien())
                .hoaDon(entity.getHoaDon())
                .chiTietPhieuDatPhong(entity.getChiTietPhieuDatPhong())
                .phong(entity.getPhong())
                .build();
    }

    public static ChiTietHoaDon map(ChiTietHoaDonDTO entity) {
        if (entity == null) return null;
        return ChiTietHoaDon.builder()
                .id(entity.getId())
                .ngayTao(entity.getNgayTao())
                .tongTien(entity.getTongTien())
                .hoaDon(entity.getHoaDon())
                .chiTietPhieuDatPhong(entity.getChiTietPhieuDatPhong())
                .phong(entity.getPhong())
                .build();
    }

    public static ChiTietPhieuDatPhong map(ChiTietPhieuDatPhongDTO entity) {
        if (entity == null) return null;
        return ChiTietPhieuDatPhong.builder()
                .soGioLuuTru(entity.getSoGioLuuTru())
                .soNguoi(entity.getSoNguoi())
                .thoiGianNhanPhong(entity.getThoiGianNhanPhong())
                .thoiGianTraPhong(entity.getThoiGianTraPhong())
                .trangThai(entity.getTrangThai())
                .phieuDatPhong(entity.getPhieuDatPhong())
                .phong(entity.getPhong())
                .build();
    }

    public static ChiTietPhieuDatPhongDTO map(ChiTietPhieuDatPhong entity) {
        if (entity == null) return null;
        return ChiTietPhieuDatPhongDTO.builder()
                .soGioLuuTru(entity.getSoGioLuuTru())
                .soNguoi(entity.getSoNguoi())
                .thoiGianNhanPhong(entity.getThoiGianNhanPhong())
                .thoiGianTraPhong(entity.getThoiGianTraPhong())
                .trangThai(entity.getTrangThai())
                .phieuDatPhong(entity.getPhieuDatPhong())
                .phong(entity.getPhong())
                .build();
    }
}
