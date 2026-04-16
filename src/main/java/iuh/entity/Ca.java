package iuh.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString(exclude = {"caLamViecNhanViens"})
@EqualsAndHashCode(onlyExplicitlyIncluded = true)

@Entity
@Table(name = "cas")
public class Ca {
    @Id
    @EqualsAndHashCode.Include
    private String maCa;
    private LocalDate ngayBatDau;
    private LocalDate ngayKetThuc;

    @OneToMany(mappedBy = "ca")
    @JsonIgnore
    private Set<CaLamViecNhanVien> caLamViecNhanViens;

}
