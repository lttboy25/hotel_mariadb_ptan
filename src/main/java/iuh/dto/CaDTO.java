package iuh.dto;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class CaDTO implements Serializable {
    private String maCa;
    private LocalDate ngayBatDau;
    private LocalDate ngayKetThuc;
}
