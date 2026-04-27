package iuh.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
@Table(name = "NhanVien")
@Entity
public class NhanVien implements Serializable {

    @Id
    @EqualsAndHashCode.Include
    @Column(name = "maNhanVien", nullable = false, length = 20)
    private String maNhanVien;

    @Column(name = "CCCD", length = 12)
    private String CCCD;

    @Column(name = "tenNhanVien", nullable = false, length = 100)
    private String tenNhanVien;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "tenDangNhap", unique = true)
    private TaiKhoan taiKhoan;

    @Column(name = "gioiTinh")
    private boolean gioiTinh;

    @Column(name = "ngaySinh")
    private LocalDate ngaySinh;

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "soDienThoai", length = 20)
    private String soDienThoai;

    @Column(name = "ngayBatDau")
    private LocalDate ngayBatDau;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TrangThaiNhanVien trangThai;

    @Column(name = "diaChi", length = 255)
    private String diaChi;

    @OneToMany(mappedBy = "nhanVien")
    private Set<CaLamViecNhanVien> caLamViecNhanViens = new HashSet<>();

    @OneToMany(mappedBy = "nhanVien", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<HoaDon> hoaDons = new HashSet<>();
}
