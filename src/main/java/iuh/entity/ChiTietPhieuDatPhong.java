package iuh.entity;

import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;
import java.time.LocalDateTime;

import iuh.enums.TrangThaiChiTietPhieuDatPhong;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = { "phieuDatPhong", "phong" })
@Entity
@Table(name = "ChiTietPhieuDatPhong")
public class ChiTietPhieuDatPhong implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "maPhieuDatPhong")
    private PhieuDatPhong phieuDatPhong;

    @ManyToOne
    @JoinColumn(name = "maPhong")
    private Phong phong;

    @Enumerated(EnumType.STRING)
    private TrangThaiChiTietPhieuDatPhong trangThai;

    @Column(name = "soGioLuuTru")
    private int soGioLuuTru;
    private LocalDateTime thoiGianNhanPhong;
    private LocalDateTime thoiGianTraPhong;
    private int soNguoi;

    public double tinhThanhTien() {
        if (this.phong == null || this.phong.getLoaiPhong() == null) {
            return 0;
        }

        double giaPhong = this.phong.getLoaiPhong().getGia();
        double donGiaTheoGio;

        if (this.soGioLuuTru < 12) {
            donGiaTheoGio = giaPhong * 0.3;
        } else if (this.soGioLuuTru < 24) {
            donGiaTheoGio = giaPhong * 0.5;
        } else {
            donGiaTheoGio = giaPhong;
        }

        return this.soGioLuuTru * donGiaTheoGio;
    }

    @OneToOne(mappedBy = "chiTietPhieuDatPhong", cascade = CascadeType.ALL, orphanRemoval = true)
    private PhieuHuyPhong phieuHuyPhong;

    @OneToOne(mappedBy = "chiTietPhieuDatPhong")
    private ChiTietHoaDon chiTietHoaDon;
}
