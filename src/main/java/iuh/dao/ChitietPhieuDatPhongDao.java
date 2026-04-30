/*
 * @ (#) ChitietPhieuDatPhongDao.java     1.0    4/29/2026
 *
 * Copyright (c) 2026 IUH. All rights reserved.
 */
package iuh.dao;

import iuh.entity.ChiTietPhieuDatPhong;
import iuh.entity.TrangThaiChiTietPhieuDatPhong;
import iuh.entity.TrangThaiPhieuDatPhong;
import iuh.entity.TrangThaiPhong;

import java.time.LocalDateTime;
import java.util.List;

/*
 * @description
 * @author:NguyenTruong
 * @date:  4/29/2026
 * @version:    1.0
 */
public interface ChitietPhieuDatPhongDao {
    // lay phong theo ma phieu dat phong
    List<ChiTietPhieuDatPhong> getByMaPhieuDatPhong(String maPDP);

    // kiem tra trung lich
    boolean isRoomAvailable(String maPhong, LocalDateTime checkIn, LocalDateTime checkOut);

    List<ChiTietPhieuDatPhong> getAll();

    ChiTietPhieuDatPhong findChiTietPhieuDatPhongByMaPhong(String maPhong);

    boolean updateStatusDetail(Long id, TrangThaiChiTietPhieuDatPhong status);

    @Deprecated
    boolean updateStatusDetailTicketByRoomCode(String maPhong, TrangThaiChiTietPhieuDatPhong status);

    List<ChiTietPhieuDatPhong> getChiTietPhieuDatPhongByToPayment(TrangThaiPhieuDatPhong statusTicket,
            TrangThaiChiTietPhieuDatPhong statusDetail,
            String cccd);

    // Tìm các phòng đang ở trạng thái 'Đã đặt' dựa trên CCCD
    List<ChiTietPhieuDatPhong> getPhongDeHuyByCCCD(String cccd);

    List<ChiTietPhieuDatPhong> getPhongDeNhanByCCCD(String cccd);

    List<ChiTietPhieuDatPhong> getDangThueBySDT(String soDienThoai);

    boolean updateGiaHan(Long id, LocalDateTime thoiGianTraMoi, int soGioMoi);

    ChiTietPhieuDatPhong findById(Long id);

    boolean isRoomAvailableForExtension(Long chiTietId, LocalDateTime newEndTime);

    List<ChiTietPhieuDatPhong> searchPhongDangThue(String keyword);
}
