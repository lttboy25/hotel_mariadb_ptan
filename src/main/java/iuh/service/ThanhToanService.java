package iuh.service;

import java.util.ArrayList;
import java.util.List;

import iuh.entity.ChiTietPhieuDatPhong;
import iuh.entity.PhieuDatPhong;
import iuh.entity.Phong;

public class ThanhToanService {
    private PhongService phongService = new PhongService();
    private ChiTietPhieuDatPhongService  chiTietPhieuDatPhongService = new ChiTietPhieuDatPhongService();
    private PhieuDatPhongService phieuDatPhongService = new PhieuDatPhongService();
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

    public List<ChiTietPhieuDatPhong> getDanhSachPhieuDatPhongDeThanhToan() {
        List<ChiTietPhieuDatPhong> dsPhongDangThue = new ArrayList<>();
        List<PhieuDatPhong> dsPhieuDatPhong = phieuDatPhongService.getByTrangThai("Đã nhận phòng");

        if (dsPhieuDatPhong == null || dsPhieuDatPhong.isEmpty()) return dsPhongDangThue;

        dsPhieuDatPhong.forEach(pdp -> {
            List<ChiTietPhieuDatPhong> ds = chiTietPhieuDatPhongService
                    .getChiTietPhieuDatPhongByMaPDP(pdp.getMaPhieuDatPhong());
            if (ds != null) dsPhongDangThue.addAll(ds);
        });

        return dsPhongDangThue;
    }

    //Thanh toán 1 chi tiết phiếu đặt phon -> tạo 1 chi tiết hóa đơn
    // Kiểm tra nếu có phiểu đặt phòng
}
