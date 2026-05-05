package iuh.dao;

import iuh.entity.CaLamViecNhanVien;

import java.util.List;

public interface CaLamViecNhanVienDao extends GenericDao<CaLamViecNhanVien, String> {
    List<CaLamViecNhanVien> findByNhanVien(String maNhanVien);
    CaLamViecNhanVien findActiveShift(String maNhanVien);
    List<CaLamViecNhanVien> findCompletedShifts(String maNhanVien);
}
