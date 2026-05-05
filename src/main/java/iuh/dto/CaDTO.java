package iuh.dto;

import lombok.*;

import java.io.Serializable;
import java.time.LocalTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class CaDTO implements Serializable {
    private String maCa;
    private String tenCa;
    private LocalTime gioBatDau;
    private LocalTime gioKetThuc;
    private String moTa;
}
