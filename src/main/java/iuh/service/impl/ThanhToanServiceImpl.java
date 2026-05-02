package iuh.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import iuh.dao.impl.ChiTietHoaDonDaoImpl;
import iuh.dao.impl.HoaDonDaoImpl;
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

    private Mapper mapper = new Mapper();

    @Override
    public List<Phong> getRoomsByStatus(TinhTrangPhong status) {
        return phongServiceImpl.getRoomsByStatus(status);
    }

    @Override
    public List<ChiTietPhieuDatPhong> getDanhSachPhieuDatPhongDeThanhToan(String cccd) {
        return chiTietPhieuDatPhongServiceImpl
                .getChiTietPhieuDatPhongByToPayment(TrangThaiPhieuDatPhong.NHAN_PHONG,
                        TrangThaiChiTietPhieuDatPhong.NHAN_PHONG, cccd);

    }

    @Override
    public boolean coTheThanhToan(double tienKhachDua, double tongTien) {
        if (tienKhachDua < tongTien)
            return false;
        else
            return true;
    }

    @Override
    public HoaDon thanhToan(List<ChiTietPhieuDatPhong> listThanhToan, double tienKhachDua, double tienThua) {

        if (listThanhToan == null || listThanhToan.isEmpty()) {
            return null;
        }

        LocalDateTime now = LocalDateTime.now();
        double tongTien = 0.0;
        List<ChiTietHoaDon> dsChiTietHoaDon = new ArrayList<>();

        PhieuDatPhong phieuDatPhong = listThanhToan.get(0).getPhieuDatPhong();

        for (ChiTietPhieuDatPhong ctpdp : listThanhToan) {

            if (TrangThaiChiTietPhieuDatPhong.DA_THANH_TOAN.equals(ctpdp.getTrangThai())) {
                throw new RuntimeException("Phòng " + ctpdp.getPhong().getMaPhong() + " đã thanh toán!");
            }

            double tien = (ctpdp.tinhThanhTien() + ctpdp.tinhThanhTien() * 0.1);
            tongTien += tien;

            ChiTietHoaDon cthd = new ChiTietHoaDon();
            cthd.setChiTietPhieuDatPhong(ctpdp);
            cthd.setPhong(ctpdp.getPhong());
            cthd.setNgayTao(now);
            cthd.setTongTien(tien);

            dsChiTietHoaDon.add(cthd);
        }

        NhanVien nv = nhanVienServiceImpl.getNhanVienById("NV001").orElse(null);

        HoaDon hoaDon = new HoaDon();
        hoaDon.setNgayDat(now);
        hoaDon.setKhachHang(phieuDatPhong.getKhachHang());
        hoaDon.setNhanVien(nv);
        hoaDon.setTrangThai(TrangThaiHoaDon.DA_THANH_TOAN);
        hoaDon.setNgayTao(now);
        hoaDon.setTongTien(tongTien);
        hoaDon.setTienKhachDua(tienKhachDua);
        hoaDon.setTienThoi(tienThua);

        hoaDon = hoaDonDao.save(mapper.map(hoaDon));
        if (hoaDon == null) {
            throw new RuntimeException("Không thể tạo hóa đơn");
        }

        for (ChiTietHoaDon cthd : dsChiTietHoaDon) {

            cthd.setHoaDon(hoaDon);

            ChiTietHoaDon luuChiTietHoaDon = chiTietHoaDonDao.save(mapper.map(cthd));
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

        return hoaDon;
    }
}
