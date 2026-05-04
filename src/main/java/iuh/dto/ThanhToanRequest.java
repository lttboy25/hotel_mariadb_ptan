package iuh.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ThanhToanRequest implements Serializable {
    private List<ChiTietPhieuDatPhongDTO> listThanhToan;
    private double tienKhachDua;
    private double tienThua;
    private double tongTien;
    private String maNhanVien;
}
