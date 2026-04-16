package iuh.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
@Entity
@Table(name = "HoaDon")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class HoaDon {
    @Id
    private String maHoaDon;
    private LocalDateTime ngayDat;
    @ManyToOne(optional = false)
    @JoinColumn(name = "maKhachHang", nullable = false)
    private KhachHang khachHang;

    @ManyToOne(optional = false)
    @JoinColumn(name = "maNhanVien", nullable = false)
    private NhanVien nhanVien;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="maKhuyenMai")
    private KhuyenMai khuyenMai;
    private LocalDateTime ngayTao;
    private String trangThai;
    private double tongTien;

}
