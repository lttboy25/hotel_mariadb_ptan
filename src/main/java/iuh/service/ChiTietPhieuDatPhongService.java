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

    public boolean updateTrangThaiByMaPhong(String maPhong, String trangThai){
        if (maPhong == null || trangThai == null){return false;}
        if (chitietPhieuDatPhongDao.findChiTietPhieuDatPhongByMaPhong(maPhong) == null){return false;}

        return  chitietPhieuDatPhongDao.updateStatusDetailTicketByRoomCode(maPhong, trangThai);
    }
}
