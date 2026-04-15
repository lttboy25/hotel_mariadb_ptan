package iuh.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChiTietHoaDon {

    private PhieuDatPhong phieuDatPhong;
    private Phong phong;

    private LocalDateTime ngayTao;
    private double tongTien;

    @Override
    public String toString() {
        return "ChiTietHoaDon{" +
                ", maPhieuDatPhong=" + (phieuDatPhong != null ? phieuDatPhong.getMaPhieuDatPhong() : "null") +
                ", ngayTao=" + ngayTao +
                ", tongTien=" + tongTien +
                '}';
    }
}
