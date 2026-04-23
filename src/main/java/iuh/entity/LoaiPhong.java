package iuh.entity;

import java.time.LocalDate;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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
@Table(name = "LoaiPhong")
public class LoaiPhong {

    @Id

    @Column(name = "maLoaiPhong", nullable = false, length = 20)
    @EqualsAndHashCode.Include
    private String maLoaiPhong;

    @Column(name = "tenLoaiPhong")
    private String tenLoaiPhong;

    @Column(name = "gia")
    private double gia;

    @Column(name = "ngayTao")
    private LocalDate ngayTao;

    @Column(name = "soNguoiLonToiDa")
    private int soNguoiLonToiDa;

    @Column(name = "soTreEmToiDa")
    private int soTreEmToiDa;

    @OneToMany(mappedBy = "loaiPhong")
    @JsonIgnore
    private Set<Phong> phongs;

    @Override
    public String toString() {
        return this.getTenLoaiPhong();
    }
}