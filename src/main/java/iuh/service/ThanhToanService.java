package iuh.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import iuh.dao.ChiTietHoaDonDao;
import iuh.dao.HoaDonDao;
import iuh.dto.ChiTietHoaDonDTO;
import iuh.dto.HoaDonDTO;
import iuh.entity.*;
import iuh.mapper.Mapper;

public class ThanhToanService {
    private PhongService phongService = new PhongService();
    private ChiTietPhieuDatPhongService chiTietPhieuDatPhongService = new ChiTietPhieuDatPhongService();
    private HoaDonDao hoaDonDao = new HoaDonDao();
    private ChiTietHoaDonDao chiTietHoaDonDao = new ChiTietHoaDonDao();
    private PhieuDatPhongService phieuDatPhongService = new PhieuDatPhongService();
    private  NhanVienService nhanVienService = new NhanVienService();

    private Mapper mapper = new Mapper();

    public List<Phong> getRoomsByStatus(String status) {
        return phongService.getRoomsByStatus(status);
    }

    public List<ChiTietPhieuDatPhong> getDanhSachPhieuDatPhongDeThanhToan(String cccd) {
        return chiTietPhieuDatPhongService
                .getChiTietPhieuDatPhongByToPayment("Đã nhận phòng", "Chưa thanh toán", cccd);

    }

    public boolean coTheThanhToan(double tienKhachDua, double tongTien) {
        if (tienKhachDua < tongTien) return false;
        else return true;
    }

    public HoaDon thanhToan(List<ChiTietPhieuDatPhong> listThanhToan, double tienKhachDua, double tienThua) {

        if (listThanhToan == null || listThanhToan.isEmpty()) {
            return null;
        }

        LocalDateTime now = LocalDateTime.now();
        double tongTien = 0.0;
        List<ChiTietHoaDon> dsChiTietHoaDon = new ArrayList<>();

        PhieuDatPhong phieuDatPhong = listThanhToan.get(0).getPhieuDatPhong();

        for (ChiTietPhieuDatPhong ctpdp : listThanhToan) {

            if ("Đã thanh toán".equalsIgnoreCase(ctpdp.getTrangThai())) {
                throw new RuntimeException("Phòng " + ctpdp.getPhong().getMaPhong() + " đã thanh toán!");
            }

            double tien = (ctpdp.tinhThanhTien() + ctpdp.tinhThanhTien()*0.1);
            tongTien += tien;

            ChiTietHoaDon cthd = new ChiTietHoaDon();
            cthd.setChiTietPhieuDatPhong(ctpdp);
            cthd.setPhong(ctpdp.getPhong());
            cthd.setNgayTao(now);
            cthd.setTongTien(tien);

            dsChiTietHoaDon.add(cthd);
        }

        NhanVien nv = nhanVienService.getNhanVienById("NV001").orElse(null);

        HoaDon hoaDon = new HoaDon();
        hoaDon.setNgayDat(now);
        hoaDon.setKhachHang(phieuDatPhong.getKhachHang());
        hoaDon.setNhanVien(nv);
        hoaDon.setTrangThai("Đã thanh toán");
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

            boolean ktraChiTietPhieu = chiTietPhieuDatPhongService
                    .updateTrangThaiByMaPhong(cthd.getPhong().getMaPhong(), "Đã thanh toán");

            if (!ktraChiTietPhieu) {
                throw new RuntimeException("Lỗi cập nhật chi tiết phiếu");
            }

            boolean ktraPhong = phongService
                    .updateStatusRoom(cthd.getPhong().getMaPhong(), "Sẵn sàng", "Trống");

            if (!ktraPhong) {
                throw new RuntimeException("Lỗi cập nhật phòng");
            }
        }

        List<ChiTietPhieuDatPhong> dsAll = chiTietPhieuDatPhongService
                .getChiTietPhieuDatPhongByMaPDP(phieuDatPhong.getMaPhieuDatPhong());

        boolean thanhToanToanBoPhieu = true;
        for (ChiTietPhieuDatPhong ct : dsAll) {
            if (!"Đã thanh toán".equalsIgnoreCase(ct.getTrangThai())) {
                thanhToanToanBoPhieu = false;
                break;
            }
        }

        if (thanhToanToanBoPhieu) {
            phieuDatPhongService.updateTrangThai(
                    phieuDatPhong.getMaPhieuDatPhong(),
                    "Đã thanh toán");
        }

        return hoaDon;
    }
}
