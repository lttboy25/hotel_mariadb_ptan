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
    // Quy trình thực hiện thanh toán:
    // B1: Nhập CCCD tìm Phòng cần thanh toán
    // B2: Chọn phòng cần thanh toán
    // B3: Chọn phương thức thanh toán
    // B4: Thanh Toán

    // 3.1 Thanh toán bằng tiền mặt
    // Có các mệnh giá chọn nhanh
    // Ô nhập số tiền khách đưa
    // Hiển thị số tiền hoàn trả

    // 3.2 Hiển thị mã QR quét mã để thanh toán

    public List<Phong> getRoomsByStatus(String status) {
        return phongService.getRoomsByStatus(status);
    }

    public List<ChiTietPhieuDatPhong> getDanhSachPhieuDatPhongDeThanhToan(String cccd) {
        List<ChiTietPhieuDatPhong> dsPhongDangThue = new ArrayList<>();
        List<PhieuDatPhong> dsPhieuDatPhongChuaLoc = phieuDatPhongService.getByTrangThai("Đã nhận phòng");
        List<PhieuDatPhong> dsPhieuDatPhong = phieuDatPhongService.filteredListByCCCD(dsPhieuDatPhongChuaLoc, cccd);

        if (dsPhieuDatPhong == null || dsPhieuDatPhong.isEmpty())
            return dsPhongDangThue;

        dsPhieuDatPhong.forEach(pdp -> {
            List<ChiTietPhieuDatPhong> ds = chiTietPhieuDatPhongService
                    .getChiTietPhieuDatPhongByMaPDP(pdp.getMaPhieuDatPhong());
            if (ds != null)
                dsPhongDangThue.addAll(ds);
        });

        return dsPhongDangThue;
    }

    public boolean thanhToan(List<ChiTietPhieuDatPhong> listThanhToan) {

        if (listThanhToan == null || listThanhToan.isEmpty()) {
            return false;
        }

        double tongTien = 0.0;
        List<ChiTietHoaDon> dsChiTietHoaDon = new ArrayList<>();

        PhieuDatPhong phieuDatPhong = listThanhToan.get(0).getPhieuDatPhong();

        for (ChiTietPhieuDatPhong ctpdp : listThanhToan) {

            if ("Đã thanh toán".equalsIgnoreCase(ctpdp.getTrangThai())) {
                throw new RuntimeException("Phòng " + ctpdp.getPhong().getMaPhong() + " đã thanh toán!");
            }

            double tien = ctpdp.tinhThanhTien();
            tongTien += tien;

            ChiTietHoaDon cthd = new ChiTietHoaDon();
            cthd.setChiTietPhieuDatPhong(ctpdp);
            cthd.setPhong(ctpdp.getPhong());
            cthd.setNgayTao(ctpdp.getThoiGianTraPhong());
            cthd.setTongTien(tien);

            dsChiTietHoaDon.add(cthd);
        }

        NhanVien nv = nhanVienService.getNhanVienById("NV001").orElse(null);

        HoaDon hoaDon = new HoaDon();
        hoaDon.setNgayDat(LocalDateTime.now());
        hoaDon.setKhachHang(phieuDatPhong.getKhachHang());
         hoaDon.setNhanVien(nv);
        hoaDon.setTrangThai("Đã thanh toán");
        hoaDon.setTongTien(tongTien);

        hoaDon = hoaDonDao.save(mapper.map(hoaDon));
        if (hoaDon == null) {
            throw new RuntimeException("Không thể tạo hóa đơn");
        }

        for (ChiTietHoaDon cthd : dsChiTietHoaDon) {

            cthd.setHoaDon(hoaDon);

            ChiTietHoaDon saved = chiTietHoaDonDao.save(mapper.map(cthd));
            if (saved == null) {
                throw new RuntimeException("Lỗi lưu chi tiết hóa đơn");
            }

            boolean ktraChiTietPhieu = chiTietPhieuDatPhongService
                    .updateTrangThaiByMaPhong(cthd.getPhong().getMaPhong(), "Đã thanh toán");

            if (!ktraChiTietPhieu) {
                throw new RuntimeException("Lỗi cập nhật chi tiết phiếu");
            }

            boolean ktraPhong = phongService
                    .updateStatusRoom(cthd.getPhong().getMaPhong(), "Trống");

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

        return true;
    }
}
