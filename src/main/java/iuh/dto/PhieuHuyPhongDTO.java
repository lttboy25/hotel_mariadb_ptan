package iuh.dto;

import iuh.entity.ChiTietPhieuDatPhong;
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

    private ChiTietPhieuDatPhong chiTietPhieuDatPhong;

    private String lyDo;

    private LocalDateTime ngayHuy;

}
