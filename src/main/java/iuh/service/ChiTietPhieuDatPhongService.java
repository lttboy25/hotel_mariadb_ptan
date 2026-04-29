package iuh.service;

import iuh.dao.ChitietPhieuDatPhongDao;
import iuh.entity.ChiTietPhieuDatPhong;
import iuh.entity.PhieuDatPhong;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;

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

    public List<ChiTietPhieuDatPhong> timPhongDangThueBySDT(String soDienThoai) {
        if (soDienThoai == null || soDienThoai.isBlank())
            throw new IllegalArgumentException("Số điện thoại không được để trống.");
        return chitietPhieuDatPhongDao.getDangThueBySDT(soDienThoai.trim());
    }

    public void giaHanNhieu(Map<Long, LocalDateTime> requests) {
        for (Map.Entry<Long, LocalDateTime> entry : requests.entrySet()) {
            Long          id             = entry.getKey();
            LocalDateTime thoiGianTraMoi = entry.getValue();

            ChiTietPhieuDatPhong ct = chitietPhieuDatPhongDao.findById(id);
            if (ct == null)
                throw new IllegalArgumentException("Không tìm thấy chi tiết phiếu ID: " + id);

            if (thoiGianTraMoi == null || !thoiGianTraMoi.isAfter(ct.getThoiGianTraPhong()))
                throw new IllegalArgumentException(
                        "Thời gian gia hạn phải sau ngày trả hiện tại của phòng "
                                + (ct.getPhong() != null ? ct.getPhong().getSoPhong() : id));

            int soGioMoi = (int) ChronoUnit.HOURS.between(
                    ct.getThoiGianNhanPhong(), thoiGianTraMoi);

            chitietPhieuDatPhongDao.updateGiaHan(id, thoiGianTraMoi, soGioMoi);
        }
    }

    public boolean isRoomAvailableForExtension(Long chiTietId, LocalDateTime newEndTime) {
        return chitietPhieuDatPhongDao.isRoomAvailableForExtension(chiTietId, newEndTime);
    }

    public List<ChiTietPhieuDatPhong> timPhongDangThue(String keyword) {
        return chitietPhieuDatPhongDao.searchPhongDangThue(keyword);
    }
}
