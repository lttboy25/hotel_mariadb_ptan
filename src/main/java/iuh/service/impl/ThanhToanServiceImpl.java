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
                .getChiTietPhieuDatPhongByToPayment(TrangThaiPhieuDatPhong.NHAN_PHONG,
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
        double tongTienDaTinh = thanhToanRequest.getTongTien(); // ← đã có KM + VAT

        List<ChiTietPhieuDatPhong> listThanhToan = listThanhToanDto.stream()
                .map(Mapper::map).collect(Collectors.toList());

        if (listThanhToan == null || listThanhToan.isEmpty())
            return null;

        LocalDateTime now = LocalDateTime.now();
        List<ChiTietHoaDon> dsChiTietHoaDon = new ArrayList<>();
        PhieuDatPhong phieuDatPhong = listThanhToan.get(0).getPhieuDatPhong();

        for (ChiTietPhieuDatPhong ctpdp : listThanhToan) {
            if (TrangThaiChiTietPhieuDatPhong.DA_THANH_TOAN.equals(ctpdp.getTrangThai())) {
                throw new RuntimeException("Phòng " + ctpdp.getPhong().getMaPhong() + " đã thanh toán!");
            }

            ChiTietHoaDon cthd = new ChiTietHoaDon();
            cthd.setChiTietPhieuDatPhong(ctpdp);
            cthd.setPhong(ctpdp.getPhong());
            cthd.setNgayTao(now);
            cthd.setTongTien(cthd.getTongTien());
            dsChiTietHoaDon.add(cthd);
        }

        NhanVien nv = nhanVienServiceImpl.getNhanVienById(thanhToanRequest.getMaNhanVien()).orElse(null);

        HoaDon hoaDon = new HoaDon();
        hoaDon.setNgayDat(now);
        hoaDon.setKhachHang(phieuDatPhong.getKhachHang());
        hoaDon.setNhanVien(nv);
        hoaDon.setTrangThai(TrangThaiHoaDon.DA_THANH_TOAN);
        hoaDon.setNgayTao(now);
        hoaDon.setTongTien(tongTienDaTinh); // ← dùng giá trị đã tính (có KM)
        hoaDon.setTienKhachDua(tienKhachDua);
        hoaDon.setTienThoi(tienThua);

        hoaDon = hoaDonDao.save(Mapper.map(hoaDon));
        if (hoaDon == null) {
            throw new RuntimeException("Không thể tạo hóa đơn");
        }

        for (ChiTietHoaDon cthd : dsChiTietHoaDon) {

            cthd.setHoaDon(hoaDon);

            ChiTietHoaDon luuChiTietHoaDon = chiTietHoaDonDao.save(Mapper.map(cthd));
            if (luuChiTietHoaDon == null) {
                throw new RuntimeException("Lỗi lưu chi tiết hóa đơn");
            }

            boolean ktraChiTietPhieu = chiTietPhieuDatPhongServiceImpl
                    .updateTrangThaiByMaPhong(cthd.getPhong().getMaPhong(),
                            TrangThaiChiTietPhieuDatPhong.DA_THANH_TOAN);

            if (!ktraChiTietPhieu) {
                throw new RuntimeException("Lỗi cập nhật chi tiết phiếu");
            }

            boolean ktraPhong = phongServiceImpl
                    .updateStatusRoom(cthd.getPhong().getMaPhong(), TrangThaiPhong.SAN_SANG, TinhTrangPhong.TRONG);

            if (!ktraPhong) {
                throw new RuntimeException("Lỗi cập nhật phòng");
            }
        }

        List<ChiTietPhieuDatPhong> dsAll = chiTietPhieuDatPhongServiceImpl
                .getChiTietPhieuDatPhongByMaPDP(phieuDatPhong.getMaPhieuDatPhong());

        boolean thanhToanToanBoPhieu = true;
        for (ChiTietPhieuDatPhong ct : dsAll) {
            if (!TrangThaiChiTietPhieuDatPhong.DA_THANH_TOAN.equals(ct.getTrangThai())) {
                thanhToanToanBoPhieu = false;
                break;
            }
        }

        if (thanhToanToanBoPhieu) {
            phieuDatPhongServiceImpl.updateTrangThai(
                    phieuDatPhong.getMaPhieuDatPhong(),
                    TrangThaiPhieuDatPhong.DA_THANH_TOAN);
        }

        return Mapper.map(hoaDon);
    }

    @Override
    public List<KhuyenMaiDTO> getDsKhuyenMai() {
        return khuyenMaiServiceImpl.getKhuyenMaiByTrangThai(TrangThai.DANG_AP_DUNG);
    }

    @Override
    public double tienSauKhiApGiamGia(double tongTien, KhuyenMaiDTO khuyenMai) {
        if (khuyenMai == null || khuyenMai.getHeSo() == 0) {
            return tongTien;
        }
        return tongTien - khuyenMai.getHeSo() * tongTien;
    }

}
