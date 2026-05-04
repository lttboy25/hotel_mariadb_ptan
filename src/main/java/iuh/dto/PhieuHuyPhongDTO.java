package iuh.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder

public class PhieuHuyPhongDTO implements Serializable {

    private Long maHuyPhong;

    private ChiTietPhieuDatPhongDTO chiTietPhieuDatPhong;

    private String lyDo;

    private LocalDateTime ngayHuy;

}
