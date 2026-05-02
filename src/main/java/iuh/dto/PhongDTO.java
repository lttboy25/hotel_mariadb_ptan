package iuh.dto;

import iuh.entity.LoaiPhong;
import iuh.dto.TinhTrangPhong;
import iuh.dto.TrangThaiPhong;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PhongDTO implements Serializable {

    private String maPhong;

    private String soPhong;

    private LoaiPhongDTO loaiPhong;

    private TrangThaiPhong trangThai;

    private int tang;

    private TinhTrangPhong tinhTrang;

    private String moTa;

}
