package iuh.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class CaLamViecNhanVienDTO {
    private String maCaLamViec;
    private double tienMoCa;
    private double tienKetCa;
    private String trangThai;
    private double tongChi;
    private double tongThu;
    private String maCa;
    private String maNhanVien;
    private String tenNhanVien;
    private LocalDate ngay;
    private LocalDateTime thoiGianBatDau;
    private LocalDateTime thoiGianKetThuc;
}
