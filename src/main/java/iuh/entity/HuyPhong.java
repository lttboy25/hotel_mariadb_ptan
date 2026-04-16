package iuh.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
@Table(name = "HuyPhong")
public class HuyPhong {
    @Id
    @EqualsAndHashCode.Include
    private int maHuyPhong;

//    private PhieuDatPhong phieuDatPhong;

    @ManyToOne
    @JoinColumn(name = "maPhong")
    private Phong phong;

    @Column(name = "lyDo")
    private String lyDo;

    @Column(name = "ngayHuy")
    private LocalDateTime ngayHuy;

}