package iuh.service;

import iuh.dto.CaLamViecNhanVienDTO;
import iuh.entity.CaLamViecNhanVien;

import java.util.List;

public interface CaLamViecNhanVienService {
    CaLamViecNhanVienDTO openShift(String maNhanVien, String maCa, double tienMoCa);
    CaLamViecNhanVienDTO closeShift(String maCaLamViec, double tienKetCa);
    CaLamViecNhanVienDTO getActiveShift(String maNhanVien);
    List<CaLamViecNhanVienDTO> getShiftHistory(String maNhanVien);
    String generateNextMaCaLamViec();
    
    default CaLamViecNhanVienDTO mapToDTO(CaLamViecNhanVien entity) {
        if (entity == null) return null;
        return CaLamViecNhanVienDTO.builder()
                .maCaLamViec(entity.getMaCaLamViec())
                .tienMoCa(entity.getTienMoCa())
                .tienKetCa(entity.getTienKetCa())
                .trangThai(entity.getTrangThai())
                .tongChi(entity.getTongChi())
                .tongThu(entity.getTongThu())
                .maCa(entity.getCa() != null ? entity.getCa().getMaCa() : "")
                .maNhanVien(entity.getNhanVien() != null ? entity.getNhanVien().getMaNhanVien() : "")
                .tenNhanVien(entity.getNhanVien() != null ? entity.getNhanVien().getTenNhanVien() : "")
                .ngay(entity.getNgay())
                .build();
    }
}
