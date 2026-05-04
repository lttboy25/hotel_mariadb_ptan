package iuh.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

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
public class ChiTietHoaDonDTO implements Serializable {
    private Long id;

    private HoaDonDTO hoaDon;

    private ChiTietPhieuDatPhongDTO chiTietPhieuDatPhong;

    private PhongDTO phong;

    private LocalDateTime ngayTao;
    private double tongTien;
}
