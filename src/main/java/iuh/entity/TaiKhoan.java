package iuh.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "TaiKhoan")
public class TaiKhoan implements Serializable {

    @Id
    @EqualsAndHashCode.Include
    @Column(name = "maNhanVien", nullable = false)
    private String maNhanVien;

    @Column(name = "matKhau", nullable = false)
    private String matKhau;

    @Column(name = "vaiTro", nullable = false)
    private String vaiTro;

    // mapping 1-1 (shared primary key)
    @OneToOne
    @MapsId
    @JoinColumn(name = "maNhanVien")
    private NhanVien nhanVien;
}