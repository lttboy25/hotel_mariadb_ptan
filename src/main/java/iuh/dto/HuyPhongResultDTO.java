package iuh.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class HuyPhongResultDTO implements Serializable {
    private boolean success;
    private String message;
    private double tienHoanThuc;
}

