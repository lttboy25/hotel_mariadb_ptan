package iuh.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString(exclude = {"ca"})

@Entity
@Table(name = "caLamViecNhanViens")
public class CaLamViecNhanVien {
    @Id
    private String maCaLamViec;
    private String tenCaLamViec;
    private LocalDateTime thoiGianBatDau;
    private float tienMoCa;
    private double tienKetCa;
    private String trangThai; // Changed from boolean to String
    private double tongChi;
    private double tongThu;
    @ManyToOne
    @JoinColumn(name = "maCa")
    @JsonIgnore
    private Ca ca;
//    private NhanVien nhanVien;
    private Date ngay;
}
