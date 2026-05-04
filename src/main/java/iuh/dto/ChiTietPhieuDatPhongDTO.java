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
    private PhieuDatPhongDTO phieuDatPhong;
    private PhongDTO phong;

    private TrangThaiChiTietPhieuDatPhong trangThai;
    private int soGioLuuTru;
    private LocalDateTime thoiGianNhanPhong;
    private LocalDateTime thoiGianTraPhong;
    private int soNguoi;

    public double tinhThanhTien() {
        if (this.phong == null || this.phong.getLoaiPhong() == null) {
            return 0;
        }

        double giaPhong = this.phong.getLoaiPhong().getGia();
        double donGiaTheoGio;

        if (this.soGioLuuTru < 12) {
            donGiaTheoGio = giaPhong * 0.3;
        } else if (this.soGioLuuTru < 24) {
            donGiaTheoGio = giaPhong * 0.5;
        } else {
            donGiaTheoGio = giaPhong;
        }

        return this.soGioLuuTru * donGiaTheoGio;
    }

    private PhieuHuyPhong phieuHuyPhong;
}
