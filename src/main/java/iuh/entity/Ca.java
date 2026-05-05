package iuh.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.*;

import java.io.Serializable;
import java.time.LocalTime;
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

    @jakarta.persistence.Column(name = "tenCa", length = 50)
    private String tenCa;

    @jakarta.persistence.Column(name = "gioBatDau")
    private LocalTime gioBatDau;

    @jakarta.persistence.Column(name = "gioKetThuc")
    private LocalTime gioKetThuc;

    @jakarta.persistence.Column(name = "moTa", length = 255)
    private String moTa;

    @OneToMany(mappedBy = "ca")
    @JsonIgnore
    private Set<CaLamViecNhanVien> caLamViecNhanViens;

}
