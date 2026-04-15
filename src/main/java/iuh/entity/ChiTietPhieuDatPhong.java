package iuh.entity;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChiTietPhieuDatPhong {
    private PhieuDatPhong phieuDatPhong;
    private String trangThai;
    private int soGioLuuTru;
    private LocalDateTime thoiGianNhanPhong;
    private LocalDateTime thoiGianTraPhong;
    private Phong phong;
    private int soNguoi;

    public double tinhThanhTien() {
        // Tính tiền phòng
        double thanhTien = this.soGioLuuTru * this.phong.getLoaiPhong().getGia();

        return thanhTien;
    }

}
