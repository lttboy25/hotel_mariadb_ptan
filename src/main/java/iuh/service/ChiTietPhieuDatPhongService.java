package iuh.service;

import iuh.dao.ChitietPhieuDatPhongDao;
import iuh.entity.ChiTietPhieuDatPhong;

import java.util.ArrayList;
import java.util.List;

public class ChiTietPhieuDatPhongService {
    ChitietPhieuDatPhongDao chitietPhieuDatPhongDao = new ChitietPhieuDatPhongDao();

    public List<ChiTietPhieuDatPhong> getChiTietPhieuDatPhongByMaPDP(String maPDP){
        List<ChiTietPhieuDatPhong> ds = new ArrayList<>();
        if (maPDP == null){
            return ds;
        }
        return  chitietPhieuDatPhongDao.getByMaPhieuDatPhong(maPDP);
    }
}
