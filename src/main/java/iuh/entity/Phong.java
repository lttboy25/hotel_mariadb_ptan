package iuh.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Phong")
public class Phong {
    @Id
    @Column(name = "maPhong", nullable = false, length = 20)
    @EqualsAndHashCode.Include
    private String maPhong;

    @Column(name = "soPhong")
    private String soPhong;

    @ManyToOne
    @JoinColumn(name = "maLoaiPhong", nullable = false)
    private LoaiPhong loaiPhong;

    @Column(name = "trangThai")
    private String trangThai;

    @Column(name = "tang")
    private int tang;

    @Column(name = "tinhTrang")
    private String tinhTrang;

    @Column(name = "moTa")
    private String moTa;

}
