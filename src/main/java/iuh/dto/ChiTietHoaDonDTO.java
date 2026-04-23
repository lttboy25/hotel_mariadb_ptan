package iuh.dto;

import java.time.LocalDateTime;

import iuh.entity.HoaDon;
import iuh.entity.PhieuDatPhong;
import iuh.entity.Phong;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class ChiTietHoaDonDTO {
    private Long id;

    private HoaDon hoaDon;

    private PhieuDatPhong phieuDatPhong;

    private Phong phong;

    private LocalDateTime ngayTao;
    private double tongTien;
}
