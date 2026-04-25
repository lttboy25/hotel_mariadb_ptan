package iuh.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString(exclude = {"hoaDon"}) // CỰC KỲ QUAN TRỌNG: Ngắt vòng lặp để không văng app
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "KhachHang")
public class KhachHang implements Serializable {
    @Id
    @EqualsAndHashCode.Include
    @Column(name = "maKhachHang", nullable = false, length = 20)
    private String maKhachHang;

    @Column(name = "CCCD", length = 20)
    private String CCCD;

    @Column(name = "hoTen", nullable = false, length = 100)
    private String tenKhachHang;

    @Column(name = "soDienThoai", length = 15)
    private String soDienThoai;

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "ngayTao")
    private LocalDate ngayTao;

    @OneToMany(mappedBy = "khachHang")
    private List<HoaDon> hoaDon;
}