package iuh.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "HoaDon")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class HoaDon implements Serializable {
    @Id
    @Column(name = "maHoaDon", nullable = false, length = 20)
    private String maHoaDon;
    @Column(name = "ngayDat")
    private LocalDateTime ngayDat;
    @ManyToOne(optional = false)
    @JoinColumn(name = "maKhachHang", nullable = false)
    private KhachHang khachHang;

    @ManyToOne(optional = false)
    @JoinColumn(name = "maNhanVien", nullable = false)
    private NhanVien nhanVien;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maKhuyenMai")
    private KhuyenMai khuyenMai;
    private LocalDateTime ngayTao;
    private String trangThai;
    private double tongTien;
    private double tienKhachDua;
    private double tienThoi;

    @OneToMany(mappedBy = "hoaDon", cascade = CascadeType.ALL)
    @Builder.Default
    private List<ChiTietHoaDon> chiTietHoaDon = new ArrayList<>();

}
