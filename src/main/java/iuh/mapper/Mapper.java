package iuh.mapper;

import iuh.dto.DatPhongRequestDTO;
import iuh.dto.DatPhongResultDTO;
import iuh.dto.KhachHangDTO;
import iuh.dto.KhuyenMaiDTO;
import iuh.entity.KhachHang;
import iuh.entity.PhieuDatPhong;
import iuh.entity.KhuyenMai;

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
}
