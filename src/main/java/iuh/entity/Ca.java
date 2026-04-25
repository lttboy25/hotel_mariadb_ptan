package iuh.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.*;

import java.io.Serializable;
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
public class Ca implements Serializable {
    @Id
    @EqualsAndHashCode.Include
    @jakarta.persistence.Column(name = "maCa", nullable = false, length = 20)
    private String maCa;
    private LocalDate ngayBatDau;
    private LocalDate ngayKetThuc;

    @OneToMany(mappedBy = "ca")
    @JsonIgnore
    private Set<CaLamViecNhanVien> caLamViecNhanViens;

}
