package iuh.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString(exclude = {"khachHang", "nhanVien", "khuyenMai", "chiTietHoaDon"})
@Entity
@Table(name = "hoaDons")
public class HoaDon {

    @Id
    private String maHoaDon;

    private LocalDateTime ngayDat;
    private LocalDateTime ngayTao;
    private String trangThai;
    private double tongTien;

    @ManyToOne
    @JoinColumn(name = "maKhachHang")
    private KhachHang khachHang;

    @ManyToOne
    @JoinColumn(name = "maNhanVien")
    private NhanVien nhanVien;

    @ManyToOne
    @JoinColumn(name = "maKhuyenMai")
    private KhuyenMai khuyenMai;

    @OneToMany(mappedBy = "hoaDon", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<ChiTietHoaDon> chiTietHoaDon = new ArrayList<>();
}