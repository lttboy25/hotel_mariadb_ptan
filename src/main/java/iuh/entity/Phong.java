package iuh.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Phong")
public class Phong implements Serializable {
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
