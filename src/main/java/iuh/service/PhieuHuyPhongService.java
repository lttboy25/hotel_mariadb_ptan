package iuh.service;

import iuh.dao.PhieuHuyPhongDao;
import iuh.entity.PhieuHuyPhong;

import java.util.List;

public class PhieuHuyPhongService {
    private PhieuHuyPhongDao phieuHuyPhongDao = new PhieuHuyPhongDao();

//    public boolean thucHienHuyPhong(PhieuHuyPhong phieuHuy) {
//        if (phieuHuy == null || phieuHuy.getChiTietPhieuDatPhong() == null) {
//            return false;
//        }
//        return phieuHuyPhongDao.huyPhongNghiepVu(phieuHuy);
//    }

    public boolean thucHienHuyNhieuPhong(List<PhieuHuyPhong> listPhieuHuy, double tienHoan) {
        if (listPhieuHuy == null || listPhieuHuy.isEmpty()) {
            return false;
        }
        return phieuHuyPhongDao.huyNhieuPhongNghiepVu(listPhieuHuy, tienHoan);
    }
}