package iuh.dto;

import iuh.enums.TrangThaiPhieuDatPhong;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DatPhongResultDTO implements Serializable {
    private String maPhieuDatPhong;
    private String maKhachHang;
    private TrangThaiPhieuDatPhong trangThai;
    private long tienDatCoc;
    private LocalDate ngayTao;
    private LocalDateTime checkIn;
    private LocalDateTime checkOut;
    private List<String> maPhongs;
}
