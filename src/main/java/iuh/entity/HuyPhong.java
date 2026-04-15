package iuh.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
@Table(name = "HuyPhong")
public class HuyPhong {
    @Id
    private int maHuyPhong;

    @ManyToOne
    @JoinColumn(name = "maPhieuDatPhong")
    private PhieuDatPhong phieuDatPhong;

    @ManyToOne
    @JoinColumn(name = "maPhong")
    private Phong phong;

    @Column(name = "lyDo")
    private String lyDo;

    @Column(name = "ngayHuy")
    private LocalDateTime ngayHuy;

}