package iuh.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString(exclude = {"khachHang", "dsachPhieuDatPhong"})
@Entity
@Table(name = "phieuDatPhongs")
public class PhieuDatPhong {

    @Id
    private String maPhieuDatPhong;

    private LocalDate ngayTao;
    private String trangThai;
    private long tienDatCoc;

    @ManyToOne
    @JoinColumn(name = "maKhachHang")
    private KhachHang khachHang;

    @OneToMany(mappedBy = "phieuDatPhong", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<ChiTietPhieuDatPhong> dsachPhieuDatPhong = new ArrayList<>();

    public long tinhTongTien() {
        return (long) dsachPhieuDatPhong.stream()
                .mapToDouble(ChiTietPhieuDatPhong::tinhThanhTien)
                .sum();
    }
}