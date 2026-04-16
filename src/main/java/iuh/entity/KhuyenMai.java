package iuh.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@ToString
@Entity
@Table(name = "KhuyenMai")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class KhuyenMai {
    @Id
    @EqualsAndHashCode.Include
    private String maKhuyenMai;
    private String tenKhuyenMai;
    private LocalDateTime ngayBatDau;
    private LocalDateTime ngayKetThuc;

    // 3 trạng thái
    private TrangThai trangThai;

    private float heSo;
    private float tongTienToiThieu;
    private float tongKhuyenMaiToiDa;
    @OneToMany(mappedBy = "khuyenMai")
    private Set<HoaDon> hoaDon = new HashSet<>();

}
