package iuh.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "TaiKhoan")
public class TaiKhoan {

    @Id
    @EqualsAndHashCode.Include
    @Column(name = "tenDangNhap", nullable = false, length = 50)
    private String tenDangNhap;

    @Column(name = "matKhau", nullable = false, length = 100)
    private String matKhau;

    @Column(name = "vaiTro", nullable = false, length = 50)
    private String vaiTro;

    @OneToOne(mappedBy = "taiKhoan")
    private NhanVien nhanVien;
}
