package iuh.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoaiPhong {
    private String maLoaiPhong;
    private String tenLoaiPhong;
    private double gia;
    private LocalDate ngayTao;

    // Sức chứa
    private int soNguoiLonToiDa;
    private int soTreEmToiDa;

    @Override
    public String toString() {
        return (tenLoaiPhong != null && !tenLoaiPhong.isBlank()) ? tenLoaiPhong : maLoaiPhong;
    }
}