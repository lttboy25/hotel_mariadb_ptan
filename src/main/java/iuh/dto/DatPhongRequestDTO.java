package iuh.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DatPhongRequestDTO implements Serializable {
    private String maKhachHang;
    private LocalDateTime checkIn;
    private LocalDateTime checkOut;
    private int soNguoi;
    private int soNguoiLon;
    private int soTreEm;
    private long tienDatCoc;
    private String trangThai;
    private List<String> maPhongs;
}
