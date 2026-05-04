package iuh.mapper;

import iuh.dto.*;
import iuh.entity.*;

import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * Mapper – chuyển đổi hai chiều giữa Entity và DTO.
 * <p>
 * QUY TẮC tránh vòng lặp đệ quy / LazyInitializationException:
 * <p>
 * 1. HoaDon      → HoaDonDTO          : map chiTietHoaDon (đã JOIN FETCH ở DAO)
 * 2. ChiTietHoaDon → ChiTietHoaDonDTO : KHÔNG map ngược hoaDon (set null)
 * tránh: HoaDon→ChiTiet→HoaDon→...
 * 3. PhieuDatPhong → PhieuDatPhongDTO  : KHÔNG map dsachPhieuDatPhong (set [])
 * 4. ChiTietPhieuDatPhong → DTO        : map phieuDatPhong (shallow – không có list)
 * KHÔNG map chiTietHoaDon ngược
 * 5. TaiKhoan    → TaiKhoanDTO         : KHÔNG map ngược nhanVien
 * 6. PhieuHuyPhong → DTO               : KHÔNG map ngược chiTietPhieuDatPhong
 * 7. CaLamViecNhanVien → DTO           : chỉ lấy mã/tên từ Ca & NhanVien (không map full entity)
 */
public class Mapper {

    // =========================================================================
    // KhachHang
    // =========================================================================

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

    // =========================================================================
    // TaiKhoan
    // =========================================================================

    /**
     * DTO → Entity: KHÔNG set nhanVien — JPA tự quản lý qua @MapsId
     */
    public static TaiKhoan map(TaiKhoanDTO dto) {
        if (dto == null) return null;
        return TaiKhoan.builder()
                .maNhanVien(dto.getMaNhanVien())
                .matKhau(dto.getMatKhau())
                .vaiTro(dto.getVaiTro())
                // nhanVien = null: tránh vòng lặp NhanVien↔TaiKhoan
                .build();
    }

    /**
     * Entity → DTO: KHÔNG map ngược nhanVien để tránh đệ quy
     */
    public static TaiKhoanDTO map(TaiKhoan entity) {
        if (entity == null) return null;
        return TaiKhoanDTO.builder()
                .maNhanVien(entity.getMaNhanVien())
                .matKhau(entity.getMatKhau())
                .vaiTro(entity.getVaiTro())
                .build();
    }

    // =========================================================================
    // NhanVien
    // =========================================================================

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

    // =========================================================================
    // LoaiPhong
    // =========================================================================

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

    // =========================================================================
    // Phong
    // =========================================================================

    public static Phong map(PhongDTO dto) {
        if (dto == null) return null;
        return Phong.builder()
                .maPhong(dto.getMaPhong())
                .soPhong(dto.getSoPhong())
                .loaiPhong(map(dto.getLoaiPhong()))
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

    // =========================================================================
    // KhuyenMai
    // =========================================================================

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

    /**
     * Entity → DTO: KHÔNG map Set<HoaDon> — tránh lazy + đệ quy
     */
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

    // =========================================================================
    // PhieuHuyPhong
    // =========================================================================

    /**
     * Entity → DTO: KHÔNG map ngược chiTietPhieuDatPhong
     * tránh: ChiTietPhieuDatPhong → PhieuHuyPhong → ChiTietPhieuDatPhong → ...
     */
    public static PhieuHuyPhongDTO map(PhieuHuyPhong entity) {
        if (entity == null) return null;
        return PhieuHuyPhongDTO.builder()
                .maHuyPhong(entity.getMaHuyPhong())
                .lyDo(entity.getLyDo())
                .ngayHuy(entity.getNgayHuy())
                // chiTietPhieuDatPhong = null: tránh đệ quy
                .build();
    }

    /**
     * DTO → Entity: KHÔNG set chiTietPhieuDatPhong — service tự set sau
     */
    public static PhieuHuyPhong map(PhieuHuyPhongDTO dto) {
        if (dto == null) return null;
        return PhieuHuyPhong.builder()
                .maHuyPhong(dto.getMaHuyPhong())
                .lyDo(dto.getLyDo())
                .ngayHuy(dto.getNgayHuy())
                // chiTietPhieuDatPhong = null: service tự set
                .build();
    }

    // =========================================================================
    // PhieuDatPhong
    // =========================================================================

    /**
     * DTO → Entity: KHÔNG map dsachPhieuDatPhong
     * Khi cần persist ChiTietPhieuDatPhong, service tự set phieuDatPhong trên entity.
     */
    public static PhieuDatPhong map(PhieuDatPhongDTO dto) {
        if (dto == null) return null;
        return PhieuDatPhong.builder()
                .maPhieuDatPhong(dto.getMaPhieuDatPhong())
                .ngayTao(dto.getNgayTao())
                .trangThai(dto.getTrangThai())
                .tienDatCoc(dto.getTienDatCoc())
                .khachHang(map(dto.getKhachHang()))
                // dsachPhieuDatPhong = []: tránh StackOverflow ChiTiet→PhieuDat→ChiTiet
                .dsachPhieuDatPhong(new ArrayList<>())
                .build();
    }

    /**
     * Entity → DTO: KHÔNG load dsachPhieuDatPhong (LAZY)
     * Nếu cần chi tiết, dùng mapPhieuDatPhongWithDetails() sau khi JOIN FETCH ở DAO.
     */
    public static PhieuDatPhongDTO map(PhieuDatPhong entity) {
        if (entity == null) return null;
        return PhieuDatPhongDTO.builder()
                .maPhieuDatPhong(entity.getMaPhieuDatPhong())
                .ngayTao(entity.getNgayTao())
                .trangThai(entity.getTrangThai())
                .tienDatCoc(entity.getTienDatCoc())
                .khachHang(map(entity.getKhachHang()))
                // dsachPhieuDatPhong = []: tránh LazyInitializationException
                .dsachPhieuDatPhong(new ArrayList<>())
                .build();
    }

    /**
     * Entity → DTO có chi tiết — chỉ gọi khi DAO đã JOIN FETCH dsachPhieuDatPhong.
     */
    public static PhieuDatPhongDTO mapWithDetails(PhieuDatPhong entity) {
        if (entity == null) return null;
        return PhieuDatPhongDTO.builder()
                .maPhieuDatPhong(entity.getMaPhieuDatPhong())
                .ngayTao(entity.getNgayTao())
                .trangThai(entity.getTrangThai())
                .tienDatCoc(entity.getTienDatCoc())
                .khachHang(map(entity.getKhachHang()))
                .dsachPhieuDatPhong(entity.getDsachPhieuDatPhong() != null
                        ? entity.getDsachPhieuDatPhong().stream()
                          .map(Mapper::map)
                          .collect(Collectors.toList())
                        : new ArrayList<>())
                .build();
    }

    // =========================================================================
    // ChiTietPhieuDatPhong
    // =========================================================================

    /**
     * DTO → Entity: phieuDatPhong được map shallow (không có list con)
     * → an toàn, không đệ quy.
     */
    public static ChiTietPhieuDatPhong map(ChiTietPhieuDatPhongDTO dto) {
        if (dto == null) return null;
        return ChiTietPhieuDatPhong.builder()
                .id(dto.getId())
                .phieuDatPhong(map(dto.getPhieuDatPhong()))   // shallow — dsach = []
                .phong(map(dto.getPhong()))
                .trangThai(dto.getTrangThai())
                .soGioLuuTru(dto.getSoGioLuuTru())
                .thoiGianNhanPhong(dto.getThoiGianNhanPhong())
                .thoiGianTraPhong(dto.getThoiGianTraPhong())
                .soNguoi(dto.getSoNguoi())
                // phieuHuyPhong = null: service tự set nếu cần
                .build();
    }

    /**
     * Entity → DTO:
     * - phieuDatPhong: map shallow (không có list con)
     * - phieuHuyPhong: map nếu có, KHÔNG map ngược chiTietPhieuDatPhong
     * - chiTietHoaDon (OneToOne ngược): bỏ qua — không cần thiết ở đây
     */
    public static ChiTietPhieuDatPhongDTO map(ChiTietPhieuDatPhong entity) {
        if (entity == null) return null;
        return ChiTietPhieuDatPhongDTO.builder()
                .id(entity.getId())
                .phieuDatPhong(map(entity.getPhieuDatPhong()))  // shallow
                .phong(map(entity.getPhong()))
                .trangThai(entity.getTrangThai())
                .soGioLuuTru(entity.getSoGioLuuTru())
                .thoiGianNhanPhong(entity.getThoiGianNhanPhong())
                .thoiGianTraPhong(entity.getThoiGianTraPhong())
                .soNguoi(entity.getSoNguoi())
                .build();
    }

    // =========================================================================
    // HoaDon
    // =========================================================================

    /**
     * DTO → Entity: KHÔNG map chiTietHoaDon để tránh cascade phức tạp
     */
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
                // chiTietHoaDon = []: service/DAO tự quản lý cascade
                .chiTietHoaDon(new ArrayList<>())
                .build();
    }

    /**
     * Entity → DTO có chi tiết — chỉ gọi khi DAO đã JOIN FETCH chiTietHoaDon.
     * ChiTietHoaDon con sẽ KHÔNG map ngược hoaDon (set null) để tránh đệ quy.
     */
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
                          .map(Mapper::map)           // gọi map(ChiTietHoaDon) — hoaDon = null
                          .collect(Collectors.toList())
                        : new ArrayList<>())
                .build();
    }

    /**
     * Entity → DTO không load chiTietHoaDon — dùng cho danh sách (tránh N+1 / lazy).
     */
    public static HoaDonDTO mapSummary(HoaDon entity) {
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
                .chiTietHoaDon(new ArrayList<>())  // không load — tránh lazy + N+1
                .build();
    }

    // =========================================================================
    // ChiTietHoaDon
    // =========================================================================

    /**
     * DTO → Entity: map hoaDon shallow (không có list chiTietHoaDon)
     */
    public static ChiTietHoaDon map(ChiTietHoaDonDTO dto) {
        if (dto == null) return null;
        return ChiTietHoaDon.builder()
                .id(dto.getId())
                .hoaDon(map(dto.getHoaDon()))              // shallow — chiTietHoaDon = []
                .chiTietPhieuDatPhong(map(dto.getChiTietPhieuDatPhong()))
                .phong(map(dto.getPhong()))
                .ngayTao(dto.getNgayTao())
                .tongTien(dto.getTongTien())
                .build();
    }

    /**
     * Entity → DTO: KHÔNG map ngược hoaDon
     * tránh: HoaDon → ChiTietHoaDon → HoaDon → ChiTietHoaDon → ... (StackOverflow)
     */
    public static ChiTietHoaDonDTO map(ChiTietHoaDon entity) {
        if (entity == null) return null;
        return ChiTietHoaDonDTO.builder()
                .id(entity.getId())
                .hoaDon(null)                              // KHÔNG map ngược — tránh đệ quy
                .chiTietPhieuDatPhong(map(entity.getChiTietPhieuDatPhong()))
                .phong(map(entity.getPhong()))
                .ngayTao(entity.getNgayTao())
                .tongTien(entity.getTongTien())
                .build();
    }

    // =========================================================================
    // CaLamViecNhanVien
    // =========================================================================

    /**
     * DTO → Entity: ca và nhanVien KHÔNG thể set từ DTO vì chỉ lưu mã (String).
     * Service phải fetch Ca và NhanVien từ DB rồi set thủ công sau khi gọi method này.
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
                // ca = null, nhanVien = null: service tự set sau khi fetch DB
                .build();
    }

    /**
     * Entity → DTO: chỉ lấy mã/tên từ Ca & NhanVien — không map full entity
     */
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

    // =========================================================================
    // DatPhongResultDTO  (utility – không có entity tương ứng)
    // =========================================================================

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