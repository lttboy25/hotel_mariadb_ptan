package iuh.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString(exclude = {"ca", "nhanVien"})
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "CaLamViecNhanVien")
public class CaLamViecNhanVien implements Serializable {
    @Id
    @EqualsAndHashCode.Include
    @Column(name = "maCaLamViec", nullable = false, length = 20)
    private String maCaLamViec;

    @Column(name = "tienMoCa")
    private double tienMoCa;

    @Column(name = "tienKetCa")
    private double tienKetCa;

    @Column(name = "trangThai", length = 50)
    private String trangThai;

    @Column(name = "tongChi", nullable = false)
    private double tongChi;

    @Column(name = "tongThu", nullable = false)
    private double tongThu;

    @ManyToOne(optional = false)
    @JoinColumn(name = "maCa", nullable = false)
    @JsonIgnore
    private Ca ca;

    @ManyToOne(optional = false)
    @JoinColumn(name = "maNhanVien", nullable = false)
    @JsonIgnore
    private NhanVien nhanVien;

    @Column(name = "ngay", nullable = false)
    private LocalDate ngay;

    @Column(name = "thoiGianBatDau")
    private LocalDateTime thoiGianBatDau;

    @Column(name = "thoiGianKetThuc")
    private LocalDateTime thoiGianKetThuc;
}
