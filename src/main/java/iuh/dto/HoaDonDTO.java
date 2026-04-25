package iuh.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import iuh.entity.ChiTietHoaDon;
import iuh.entity.KhachHang;
import iuh.entity.KhuyenMai;
import iuh.entity.NhanVien;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class HoaDonDTO implements Serializable {
    private String maHoaDon;
    private LocalDateTime ngayDat;

    private KhachHang khachHang;

    private NhanVien nhanVien;

    private KhuyenMai khuyenMai;
    private LocalDateTime ngayTao;
    private String trangThai;
    private double tongTien;

    private List<ChiTietHoaDon> chiTietHoaDon = new ArrayList<>();
}
