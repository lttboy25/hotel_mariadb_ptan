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
@ToString(exclude = {"khachHang", "dsachPhieuDatPhong"}) // Chỉ thêm exclude
@Entity
@Table(name = "PhieuDatPhong")
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
    @Builder.Default
    private List<ChiTietPhieuDatPhong> dsachPhieuDatPhong = new ArrayList<>();

    public long tinhTongTien() {
        if (dsachPhieuDatPhong == null) return 0;
        long tongTien = 0;
        for (ChiTietPhieuDatPhong ct : dsachPhieuDatPhong) {
            tongTien += ct.tinhThanhTien();
        }
        return tongTien;
    }
}