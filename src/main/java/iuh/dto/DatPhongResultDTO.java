package iuh.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DatPhongResultDTO {
    private String maPhieuDatPhong;
    private String maKhachHang;
    private String trangThai;
    private long tienDatCoc;
    private LocalDate ngayTao;
    private LocalDateTime checkIn;
    private LocalDateTime checkOut;
    private List<String> maPhongs;
}

