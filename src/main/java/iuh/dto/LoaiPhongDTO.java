package iuh.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class LoaiPhongDTO implements Serializable {
    private String maLoaiPhong;

    private String tenLoaiPhong;

    private double gia;

    private LocalDate ngayTao;

    private int soNguoiLonToiDa;

    private int soTreEmToiDa;


    @Override
    public String toString() {
        return this.getTenLoaiPhong();
    }
}
