package iuh.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString(exclude = {"hoaDon", "phieuDatPhong", "phong"})
@Entity
@Table(name = "chiTietHoaDons")

public class ChiTietHoaDon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "maHoaDon")
    private HoaDon hoaDon;

    @ManyToOne
    @JoinColumn(name = "maPhieuDatPhong")
    private PhieuDatPhong phieuDatPhong;

    @ManyToOne
    @JoinColumn(name = "maPhong")
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
