package iuh.service.impl;

import iuh.dao.impl.PhieuHuyPhongDaoImpl;
import iuh.dto.HuyPhongRequest;
import iuh.dto.HuyPhongResultDTO;
import iuh.dto.PhieuHuyPhongDTO;
import iuh.entity.PhieuHuyPhong;

import java.util.ArrayList;
import java.util.List;

public class PhieuHuyPhongServiceImpl implements iuh.service.PhieuHuyPhongService {
    private PhieuHuyPhongDaoImpl phieuHuyPhongDao = new PhieuHuyPhongDaoImpl();

    @Override
    public HuyPhongResultDTO thucHienHuyNhieuPhong(HuyPhongRequest huyPhongRequest) {
        if (huyPhongRequest == null) {
            return HuyPhongResultDTO.builder()
                    .success(false)
                    .message("Dữ liệu yêu cầu hủy phòng không hợp lệ")
                    .tienHoanThuc(0)
                    .build();
        }

        List<PhieuHuyPhongDTO> listPhieuHuyDTO = huyPhongRequest.getListPhieuHuy();
        double tienHoan = huyPhongRequest.getTienHoan();

        if (listPhieuHuyDTO == null || listPhieuHuyDTO.isEmpty()) {
            return HuyPhongResultDTO.builder()
                    .success(false)
                    .message("Danh sách phiếu hủy phòng trống")
                    .tienHoanThuc(0)
                    .build();
        }

        try {
            // Convert DTOs to Entities for DAO
            List<PhieuHuyPhongDTO> listPhieuHuy1 = new ArrayList<>();
            for (PhieuHuyPhongDTO dto : listPhieuHuyDTO) {
                listPhieuHuy1.add(PhieuHuyPhongDTO.builder()
                        .maHuyPhong(dto.getMaHuyPhong())
                        .lyDo(dto.getLyDo())
                        .ngayHuy(dto.getNgayHuy())
                        .chiTietPhieuDatPhong(dto.getChiTietPhieuDatPhong())
                        .build());
            }

            List<PhieuHuyPhong> listPhieuHuy = listPhieuHuy1.stream().map(
                    e -> PhieuHuyPhong.builder()
                            .maHuyPhong(e.getMaHuyPhong())
                            .lyDo(e.getLyDo())
                            .ngayHuy(e.getNgayHuy())
                            .chiTietPhieuDatPhong(e.getChiTietPhieuDatPhong())
                            .build())
                    .toList();

            boolean result = phieuHuyPhongDao.huyNhieuPhongNghiepVu(listPhieuHuy, tienHoan);
            if (result) {
                return HuyPhongResultDTO.builder()
                        .success(true)
                        .message("Hủy phòng thành công")
                        .tienHoanThuc(tienHoan)
                        .build();
            } else {
                return HuyPhongResultDTO.builder()
                        .success(false)
                        .message("Lỗi khi hủy phòng trong cơ sở dữ liệu")
                        .tienHoanThuc(0)
                        .build();
            }
        } catch (Exception e) {
            return HuyPhongResultDTO.builder()
                    .success(false)
                    .message("Lỗi hệ thống: " + e.getMessage())
                    .tienHoanThuc(0)
                    .build();
        }
    }
}