package iuh.service;

import iuh.dao.ChitietPhieuDatPhongDao;
import iuh.entity.ChiTietPhieuDatPhong;
import iuh.entity.PhieuDatPhong;

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

        return  chitietPhieuDatPhongDao.updateStatusDetailTicketByRoomCode(maPhong, trangThai);
    }

    public List<ChiTietPhieuDatPhong> getChiTietPhieuDatPhongByToPayment(String statusTicket, String statusDetail, String cccd) {
        if(statusTicket == null || statusDetail == null || cccd == null){throw new NullPointerException("Lấy danh sách phiếu đặt phòng bị rỗng");}

        return chitietPhieuDatPhongDao.getChiTietPhieuDatPhongByToPayment(statusTicket, statusDetail, cccd);

    }

    public List<ChiTietPhieuDatPhong> getPhongDeHuyByCCCD(String cccd) {
        if (cccd == null || cccd.trim().isEmpty()) {
            return new ArrayList<>();
        }
        return chitietPhieuDatPhongDao.getPhongDeHuyByCCCD(cccd);
    }

    public List<ChiTietPhieuDatPhong> getPhongDeNhanByCCCD(String cccd) {
        if (cccd == null || cccd.trim().isEmpty()) {
            return new ArrayList<>();
        }
        return chitietPhieuDatPhongDao.getPhongDeNhanByCCCD(cccd);
    }
}
