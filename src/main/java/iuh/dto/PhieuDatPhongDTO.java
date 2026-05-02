package iuh.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import iuh.entity.ChiTietPhieuDatPhong;
import iuh.entity.KhachHang;
import iuh.enums.TrangThaiPhieuDatPhong;

public class PhieuDatPhongDTO implements Serializable {
    private String maPhieuDatPhong;

    private LocalDate ngayTao;
    private TrangThaiPhieuDatPhong trangThai;
    private long tienDatCoc;

    private KhachHang khachHang;

    private List<ChiTietPhieuDatPhong> dsachPhieuDatPhong = new ArrayList<>();

    public long tinhTongTien() {
        if (dsachPhieuDatPhong == null)
            return 0;
        long tongTien = 0;
        for (ChiTietPhieuDatPhong ct : dsachPhieuDatPhong) {
            tongTien += ct.tinhThanhTien();
        }
        return tongTien;
    }
}
