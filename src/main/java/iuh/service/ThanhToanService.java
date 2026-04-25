package iuh.service;

import java.util.List;

import iuh.entity.Phong;

public class ThanhToanService {
    private PhongService phongService;
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
}
