package iuh.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


import iuh.enums.TrangThaiPhieuDatPhong;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PhieuDatPhongDTO implements Serializable {
    private String maPhieuDatPhong;

    private LocalDate ngayTao;
    private TrangThaiPhieuDatPhong trangThai;
    private long tienDatCoc;

    private KhachHangDTO khachHang;

    @Builder.Default
    private List<ChiTietPhieuDatPhongDTO> dsachPhieuDatPhong = new ArrayList<>();

    public long tinhTongTien() {
        if (dsachPhieuDatPhong == null)
            return 0;
        long tongTien = 0;
        for (ChiTietPhieuDatPhongDTO ct : dsachPhieuDatPhong) {
            tongTien += (long) ct.tinhThanhTien();
        }
        return tongTien;
    }
}
