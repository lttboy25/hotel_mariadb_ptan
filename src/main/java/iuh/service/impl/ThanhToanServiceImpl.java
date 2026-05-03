package iuh.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import iuh.dao.impl.ChiTietHoaDonDaoImpl;
import iuh.dao.impl.HoaDonDaoImpl;
import iuh.dto.ChiTietPhieuDatPhongDTO;
import iuh.dto.HoaDonDTO;
import iuh.dto.KhachHangDTO;
import iuh.dto.KhuyenMaiDTO;
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

    private Mapper mapper = new Mapper();

    @Override
    public List<Phong> getRoomsByStatus(TinhTrangPhong status) {
        return phongServiceImpl.getRoomsByStatus(status);
    }

    @Override
    public List<ChiTietPhieuDatPhongDTO> getDanhSachPhieuDatPhongDeThanhToan(String cccd) {
        return chiTietPhieuDatPhongServiceImpl
                .getChiTietPhieuDatPhongByToPayment(TrangThaiPhieuDatPhong.NHAN_PHONG,
                        TrangThaiChiTietPhieuDatPhong.NHAN_PHONG, cccd);

    }

    @Override
    public boolean coTheThanhToan(double tienKhachDua, double tongTien) {
        if (tienKhachDua < tongTien)
            return false;

        return true;
    }


    @Override
    public HoaDonDTO thanhToan(List<ChiTietPhieuDatPhongDTO> listThanhToan,
                               double tienKhachDua, double tienThua) {

        if (listThanhToan == null || listThanhToan.isEmpty()) return null;

        LocalDateTime now = LocalDateTime.now();
        double tongTien = 0.0;
        List<ChiTietHoaDon> dsChiTietHoaDon = new ArrayList<>();

        String maPDP = listThanhToan.get(0).getPhieuDatPhong().getMaPhieuDatPhong();

        for (ChiTietPhieuDatPhongDTO ctpdp : listThanhToan) {

            if (TrangThaiChiTietPhieuDatPhong.DA_THANH_TOAN.equals(ctpdp.getTrangThai())) {
                throw new RuntimeException("Phòng " + ctpdp.getPhong().getMaPhong() + " đã thanh toán!");
            }

            double tien = ctpdp.tinhThanhTien() + ctpdp.tinhThanhTien() * 0.1;
            tongTien += tien;

            ChiTietHoaDon cthd = new ChiTietHoaDon();
            cthd.setChiTietPhieuDatPhong(mapper.map(ctpdp));
            cthd.setPhong(mapper.map(ctpdp.getPhong()));
            cthd.setNgayTao(now);
            cthd.setTongTien(tien);

            dsChiTietHoaDon.add(cthd);
        }

        ChiTietPhieuDatPhongDTO firstCT = listThanhToan.get(0);
        PhieuDatPhong phieuDatPhong = mapper.map(firstCT.getPhieuDatPhong());

        HoaDon hoaDon = new HoaDon();
        hoaDon.setNgayDat(now);
        hoaDon.setKhachHang(phieuDatPhong.getKhachHang());
        hoaDon.setTrangThai(TrangThaiHoaDon.DA_THANH_TOAN);
        hoaDon.setNgayTao(now);
        hoaDon.setTongTien(tongTien);
        hoaDon.setTienKhachDua(tienKhachDua);
        hoaDon.setTienThoi(tienThua);

        hoaDon = hoaDonDao.save(mapper.map(hoaDon));
        if (hoaDon == null) throw new RuntimeException("Không thể tạo hóa đơn");

        for (ChiTietHoaDon cthd : dsChiTietHoaDon) {
            cthd.setHoaDon(hoaDon);

            ChiTietHoaDon luuCT = chiTietHoaDonDao.save(mapper.map(cthd));
            if (luuCT == null)
                throw new RuntimeException("Lỗi lưu chi tiết hóa đơn");

            boolean ktraChiTietPhieu = chiTietPhieuDatPhongServiceImpl
                    .updateTrangThaiByMaPhong(cthd.getPhong().getMaPhong(),
                            TrangThaiChiTietPhieuDatPhong.DA_THANH_TOAN);
            if (!ktraChiTietPhieu)
                throw new RuntimeException("Lỗi cập nhật chi tiết phiếu");

            boolean ktraPhong = phongServiceImpl
                    .updateStatusRoom(cthd.getPhong().getMaPhong(),
                            TrangThaiPhong.SAN_SANG, TinhTrangPhong.TRONG);
            if (!ktraPhong)
                throw new RuntimeException("Lỗi cập nhật phòng");
        }

        List<ChiTietPhieuDatPhongDTO> dsAll = chiTietPhieuDatPhongServiceImpl
                .getChiTietPhieuDatPhongByMaPDP(maPDP);

        boolean thanhToanToanBo = dsAll.stream()
                .allMatch(ct -> TrangThaiChiTietPhieuDatPhong.DA_THANH_TOAN
                        .equals(ct.getTrangThai()));

        if (thanhToanToanBo) {
            phieuDatPhongServiceImpl.updateTrangThai(
                    maPDP, TrangThaiPhieuDatPhong.DA_THANH_TOAN);
        }

        return HoaDonDTO.builder()
                .maHoaDon(hoaDon.getMaHoaDon())
                .ngayDat(hoaDon.getNgayDat())
                .ngayTao(hoaDon.getNgayTao())
                .trangThai(hoaDon.getTrangThai())
                .tongTien(hoaDon.getTongTien())
                .tienKhachDua(hoaDon.getTienKhachDua())
                .tienThoi(hoaDon.getTienThoi())
                .khachHang(hoaDon.getKhachHang() != null ? KhachHangDTO.builder()
                        .maKhachHang(hoaDon.getKhachHang().getMaKhachHang())
                        .tenKhachHang(hoaDon.getKhachHang().getTenKhachHang())
                        .soDienThoai(hoaDon.getKhachHang().getSoDienThoai())
                        .build() : null)
                .build();
    }

    @Override
    public List<KhuyenMaiDTO> getDsKhuyenMai() {
        return khuyenMaiServiceImpl.getKhuyenMaiByTrangThai(TrangThai.DANG_AP_DUNG);
    }

    @Override
    public double tienSauKhiApGiamGia(double tongTien, KhuyenMaiDTO khuyenMai) {
        return tongTien - khuyenMai.getHeSo()*tongTien;
    }


}
