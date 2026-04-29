package iuh.service.impl;

import iuh.dao.impl.PhieuHuyPhongDaoImpl;
import iuh.entity.PhieuHuyPhong;

import java.util.List;

public class PhieuHuyPhongServiceImpl implements iuh.service.PhieuHuyPhongService {
    private PhieuHuyPhongDaoImpl phieuHuyPhongDao = new PhieuHuyPhongDaoImpl();

//    public boolean thucHienHuyPhong(PhieuHuyPhong phieuHuy) {
//        if (phieuHuy == null || phieuHuy.getChiTietPhieuDatPhong() == null) {
//            return false;
//        }
//        return phieuHuyPhongDao.huyPhongNghiepVu(phieuHuy);
//    }

    @Override
    public boolean thucHienHuyNhieuPhong(List<PhieuHuyPhong> listPhieuHuy, double tienHoan) {
        if (listPhieuHuy == null || listPhieuHuy.isEmpty()) {
            return false;
        }
        return phieuHuyPhongDao.huyNhieuPhongNghiepVu(listPhieuHuy, tienHoan);
    }
}