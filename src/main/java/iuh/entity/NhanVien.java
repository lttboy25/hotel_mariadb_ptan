package iuh.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "NhanVien")
public class NhanVien implements Serializable {

    @Id
    @EqualsAndHashCode.Include
    @Column(name = "maNhanVien", nullable = false)
    private String maNhanVien;

    @Column(name = "tenNhanVien", nullable = false)
    private String tenNhanVien;

    @Column(name = "CCCD")
    private String CCCD;

    @Column(name = "gioiTinh")
    private boolean gioiTinh;

    @Column(name = "ngaySinh")
    private LocalDate ngaySinh;

    @Column(name = "email")
    private String email;

    @Column(name = "soDienThoai")
    private String soDienThoai;

    @Column(name = "ngayBatDau")
    private LocalDate ngayBatDau;

    @Column(name = "diaChi")
    private String diaChi;

    @Enumerated(EnumType.STRING)
    private TrangThaiNhanVien trangThai;

    // Quan hệ 1-1
    @OneToOne(mappedBy = "nhanVien", cascade = CascadeType.ALL)
    private TaiKhoan taiKhoan;

    @OneToMany(mappedBy = "nhanVien")
    private Set<HoaDon> hoaDons = new HashSet<>();
}