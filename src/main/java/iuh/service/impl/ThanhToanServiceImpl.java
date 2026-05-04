package iuh.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import iuh.dao.impl.ChiTietHoaDonDaoImpl;
import iuh.dao.impl.HoaDonDaoImpl;
import iuh.dto.*;
import iuh.entity.*;
import iuh.enums.*;
import iuh.mapper.Mapper;

public class ThanhToanServiceImpl implements iuh.service.ThanhToanService {
    private PhongServiceImpl phongServiceImpl = new PhongServiceImpl();
    private ChiTietPhieuDatPhongServiceImpl chiTietPhieuDatPhongServiceImpl = new ChiTietPhieuDatPhongServiceImpl();
    private HoaDonDaoImpl hoaDonDao = new HoaDonDaoImpl();
    private ChiTietHoaDonDaoImpl chiTietHoaDonDao = new ChiTietHoaDonDaoImpl();
    private PhieuDatPhongServiceImpl phieuDatPhongServiceImpl = new PhieuDatPhongServiceImpl();
    private NhanVienServiceImpl nhanVienServiceImpl = new NhanVienServiceImpl();
    private KhuyenMaiServiceImpl khuyenMaiServiceImpl = new KhuyenMaiServiceImpl();

    @Override
    public List<ChiTietPhieuDatPhongDTO> getDanhSachPhieuDatPhongDeThanhToan(String cccd) {
        return chiTietPhieuDatPhongServiceImpl
                .getChiTietPhieuDatPhongByToPayment(
                        TrangThaiChiTietPhieuDatPhong.NHAN_PHONG, cccd)
                .stream()
                .map(e -> Mapper.map(e))
                .collect(Collectors.toList());

    }

    @Override
    public boolean coTheThanhToan(double tienKhachDua, double tongTien) {
        if (tienKhachDua < tongTien)
            return false;

        return true;
    }

    @Override
    public HoaDonDTO thanhToan(ThanhToanRequest thanhToanRequest) {
        List<ChiTietPhieuDatPhongDTO> listThanhToanDto = thanhToanRequest.getListThanhToan();
        double tienKhachDua = thanhToanRequest.getTienKhachDua();
        double tienThua = thanhToanRequest.getTienThua();
        double tongTienDaTinh = thanhToanRequest.getTongTien();
        String maNhanVien = thanhToanRequest.getMaNhanVien();

        // ── 1. Validate đầu vào ──
        if (listThanhToanDto == null || listThanhToanDto.isEmpty())
            return null;

        List<ChiTietPhieuDatPhong> listThanhToan = listThanhToanDto.stream()
                .map(Mapper::map)
                .collect(Collectors.toList());

        // ── 2. Kiểm tra trạng thái từng phòng ──
        for (ChiTietPhieuDatPhong ctpdp : listThanhToan) {
            if (TrangThaiChiTietPhieuDatPhong.DA_THANH_TOAN.equals(ctpdp.getTrangThai())) {
                throw new RuntimeException("Phòng " + ctpdp.getPhong().getMaPhong() + " đã thanh toán!");
            }
        }

        // ── 3. Tạo danh sách ChiTietHoaDon ──
        LocalDateTime now = LocalDateTime.now();
        PhieuDatPhong phieuDatPhong = listThanhToan.get(0).getPhieuDatPhong();
        NhanVienDTO nv = nhanVienServiceImpl.getNhanVienById(maNhanVien).orElse(null);

        List<ChiTietHoaDon> dsChiTietHoaDon = listThanhToan.stream()
                .map(ctpdp -> {
                    ChiTietHoaDon cthd = new ChiTietHoaDon();
                    cthd.setChiTietPhieuDatPhong(ctpdp);
                    cthd.setPhong(ctpdp.getPhong());
                    cthd.setNgayTao(now);
                    cthd.setTongTien(ctpdp.tinhThanhTien());
                    return cthd;
                })
                .collect(Collectors.toList());

        // ── 4. Tạo và lưu HoaDon ──
        HoaDon hoaDon = new HoaDon();
        hoaDon.setNgayDat(now);
        hoaDon.setNgayTao(now);
        hoaDon.setKhachHang(phieuDatPhong.getKhachHang());
        hoaDon.setNhanVien(Mapper.map(nv));
        hoaDon.setTrangThai(TrangThaiHoaDon.DA_THANH_TOAN);
        hoaDon.setTongTien(tongTienDaTinh);
        hoaDon.setTienKhachDua(tienKhachDua);
        hoaDon.setTienThoi(tienThua);

        hoaDon = hoaDonDao.save(Mapper.map(hoaDon));
        if (hoaDon == null)
            throw new RuntimeException("Không thể tạo hóa đơn");

        // ── 5. Lưu ChiTietHoaDon + cập nhật trạng thái phòng ──
        for (ChiTietHoaDon cthd : dsChiTietHoaDon) {
            cthd.setHoaDon(hoaDon);

            if (chiTietHoaDonDao.save(Mapper.map(cthd)) == null)
                throw new RuntimeException("Lỗi lưu chi tiết hóa đơn phòng " + cthd.getPhong().getMaPhong());

            if (!chiTietPhieuDatPhongServiceImpl.updateTrangThaiByMaPhong(
                    cthd.getPhong().getMaPhong(),
                    TrangThaiChiTietPhieuDatPhong.DA_THANH_TOAN))
                throw new RuntimeException("Lỗi cập nhật chi tiết phiếu phòng " + cthd.getPhong().getMaPhong());

            if (!phongServiceImpl.updateStatusRoom(
                    cthd.getPhong().getMaPhong(),
                    TrangThaiPhong.SAN_SANG,
                    TinhTrangPhong.TRONG))
                throw new RuntimeException("Lỗi cập nhật trạng thái phòng " + cthd.getPhong().getMaPhong());
        }

        // ── 6. Cập nhật trạng thái PhieuDatPhong nếu thanh toán toàn bộ ──
        boolean thanhToanToanBo = chiTietPhieuDatPhongServiceImpl
                .getChiTietPhieuDatPhongByMaPDP(phieuDatPhong.getMaPhieuDatPhong())
                .stream()
                .allMatch(ct -> TrangThaiChiTietPhieuDatPhong.DA_THANH_TOAN.equals(ct.getTrangThai()));

        if (thanhToanToanBo)
            phieuDatPhongServiceImpl.updateTrangThai(
                    phieuDatPhong.getMaPhieuDatPhong(),
                    TrangThaiPhieuDatPhong.DA_THANH_TOAN);

        // ── 7. Build và trả về DTO đầy đủ ──
        HoaDonDTO hoaDonDTO = Mapper.map(hoaDon);
        hoaDonDTO.setChiTietHoaDon(
                dsChiTietHoaDon.stream()
                        .map(Mapper::map)
                        .collect(Collectors.toList()));

        return hoaDonDTO;
    }

    @Override
    public List<KhuyenMaiDTO> getDsKhuyenMaiHopLe() {
        LocalDateTime now = LocalDateTime.now();
        return khuyenMaiServiceImpl.getKhuyenMaiByTrangThai(TrangThai.DANG_AP_DUNG)
                .stream()
                .filter(km -> km.getNgayBatDau() != null
                        && km.getNgayKetThuc() != null
                        && !now.isBefore(km.getNgayBatDau())
                        && !now.isAfter(km.getNgayKetThuc()))
                .collect(Collectors.toList());
    }

    @Override
    public KiemTraKhuyenMaiResult tienSauKhiApGiamGia(double tongTien, KhuyenMaiDTO khuyenMai) {
        double vat = tongTien * 0.1;

        if (khuyenMai == null || khuyenMai.getHeSo() == 0) {
            return KiemTraKhuyenMaiResult.builder()
                    .tien(tongTien + vat)
                    .message("Không có khuyến mãi áp dụng, chỉ tính VAT")
                    .build();
        }

        if (tongTien < khuyenMai.getTongTienToiThieu()) {
            return KiemTraKhuyenMaiResult.builder()
                    .tien(tongTien + vat)
                    .message("Tổng tiền chưa đạt mức tối thiểu, chỉ tính VAT")
                    .build();
        }

        double soTienSeGiam = Math.min(
                khuyenMai.getHeSo() * tongTien,
                khuyenMai.getTongKhuyenMaiToiDa());

        return KiemTraKhuyenMaiResult.builder()
                .tien(tongTien - soTienSeGiam + vat)
                .message("Áp dụng khuyến mãi " + khuyenMai.getTenKhuyenMai())
                .build();
    }

}