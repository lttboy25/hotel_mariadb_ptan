package iuh.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

import iuh.entity.PhieuDatPhong;
import iuh.entity.PhieuHuyPhong;
import iuh.entity.Phong;

public class ChiTietPhieuDatPhongDTO implements Serializable {

    private Long id;
    private PhieuDatPhong phieuDatPhong;
    private Phong phong;

    private String trangThai;
    private long soGioLuuTru;
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
