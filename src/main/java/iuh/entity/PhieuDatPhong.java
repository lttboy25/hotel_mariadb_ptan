package iuh.entity;

import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = { "khachHang", "dsachPhieuDatPhong" }) // Chỉ thêm exclude
@Entity
@Table(name = "PhieuDatPhong")
public class PhieuDatPhong implements Serializable {

    @Id
    @Column(name = "maPhieuDatPhong", nullable = false, length = 20)
    private String maPhieuDatPhong;

    @Column(name = "ngayTao")
    private LocalDate ngayTao;
    @Column(name = "trangThai", length = 20)
    private String trangThai; //Đã đặt, Đã nhận phòng, Đã thanh toán, Đã hủy
    @Column(name = "tienDatCoc")
    private long tienDatCoc;

    @ManyToOne
    @JoinColumn(name = "maKhachHang", nullable = false)
    private KhachHang khachHang;

    @OneToMany(mappedBy = "phieuDatPhong", cascade = CascadeType.ALL)
    @Builder.Default
    private List<ChiTietPhieuDatPhong> dsachPhieuDatPhong = new ArrayList<>();

    public double tinhTongTien() {
        if (dsachPhieuDatPhong == null)
            return 0.0;
        double tongTien = 0;
        for (ChiTietPhieuDatPhong ct : dsachPhieuDatPhong) {
            tongTien += ct.tinhThanhTien();
        }
        return tongTien;
    }
}