package iuh.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = { "phieuDatPhong", "phong" })
@Entity
@Table(name = "ChiTietPhieuDatPhong")
public class ChiTietPhieuDatPhong {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "maPhieuDatPhong")
    private PhieuDatPhong phieuDatPhong;

    @ManyToOne
    @JoinColumn(name = "maPhong")
    private Phong phong;

    private String trangThai;

    @Column(name = "soGioLuuTru")
    private int soGioLuuTru;
    private LocalDateTime thoiGianNhanPhong;
    private LocalDateTime thoiGianTraPhong;
    private int soNguoi;

    public double tinhThanhTien() {
        if (this.phong != null && this.phong.getLoaiPhong() != null) {
            return this.soGioLuuTru * this.phong.getLoaiPhong().getGia();
        }
        return 0;
    }

    @OneToOne(mappedBy = "chiTietPhieuDatPhong", cascade = CascadeType.ALL, orphanRemoval = true)
    private PhieuHuyPhong phieuHuyPhong;

    @OneToOne(mappedBy = "chiTietPhieuDatPhong")
    private ChiTietHoaDon chiTietHoaDon;
}
