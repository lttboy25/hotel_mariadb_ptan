package iuh.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

import iuh.entity.PhieuDatPhong;
import iuh.entity.PhieuHuyPhong;
import iuh.entity.Phong;
import iuh.enums.TrangThaiChiTietPhieuDatPhong;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ChiTietPhieuDatPhongDTO implements Serializable {

    private Long id;
    private PhieuDatPhong phieuDatPhong;
    private Phong phong;

    private TrangThaiChiTietPhieuDatPhong trangThai;
    private int soGioLuuTru;
    private LocalDateTime thoiGianNhanPhong;
    private LocalDateTime thoiGianTraPhong;
    private int soNguoi;

    public double tinhThanhTien() {
        if (this.phong != null && this.phong.getLoaiPhong() != null) {
            return this.soGioLuuTru * this.phong.getLoaiPhong().getGia();
        }
        return 0;
    }

    private PhieuHuyPhong phieuHuyPhong;
}
