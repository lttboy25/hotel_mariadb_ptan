/*
 * @ (#) ThongKeDTO.java     1.0    4/28/2026
 *
 * Copyright (c) 2026 IUH. All rights reserved.
 */
package iuh.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ThongKeDTO {
    private LocalDate tuNgay;
    private LocalDate denNgay;

    private long soKhachDangLuuTru;
    private long soPhongDangSuDung;
    private long tongSoPhong;
    private double tyLeLapDay;

    private long soLuotDatPhong;
    private long soLuotDatPhongKyTruoc;
    private double tyLeTangTruongDatPhong;

    private long soLuotHuyPhong;
    private double tyLeHuyPhong;

    private double tongDoanhThu;

    @Builder.Default
    private List<DuLieuCotDTO> dsDatPhongTheoThang = new ArrayList<>();
    @Builder.Default
    private List<DuLieuNgayDTO> dsDoanhThuTheoNgay = new ArrayList<>();
    @Builder.Default
    private List<DuLieuTyTrongDTO> dsTrangThaiDatPhong = new ArrayList<>();
    @Builder.Default
    private List<DuLieuTyTrongDTO> dsLoaiPhong = new ArrayList<>();

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DuLieuCotDTO {
        private String nhan;
        private double giaTri;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DuLieuNgayDTO {
        private LocalDate ngay;
        private double giaTri;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DuLieuTyTrongDTO {
        private String nhan;
        private double giaTri;
    }
}
