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
@Table(name = "PhieuHuyPhong")
public class PhieuHuyPhong {
    @Id
    @EqualsAndHashCode.Include
    private Long maHuyPhong;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chiTietId", unique = true)
    private ChiTietPhieuDatPhong chiTietPhieuDatPhong;

    @Column(name = "lyDo")
    private String lyDo;

    @Column(name = "ngayHuy")
    private LocalDateTime ngayHuy;

}