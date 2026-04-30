/*
 * @ (#) ChiTietPhieuDatPhongService.java     1.0    4/29/2026
 *
 * Copyright (c) 2026 IUH. All rights reserved.
 */
package iuh.service;

import iuh.entity.ChiTietPhieuDatPhong;
import iuh.entity.TrangThai;
import iuh.entity.TrangThaiChiTietPhieuDatPhong;
import iuh.entity.TrangThaiPhieuDatPhong;
import iuh.entity.TrangThaiPhong;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/*
 * @description
 * @author:NguyenTruong
 * @date:  4/29/2026
 * @version:    1.0
 */
public interface ChiTietPhieuDatPhongService {
    List<ChiTietPhieuDatPhong> getChiTietPhieuDatPhongByMaPDP(String maPDP);

    boolean updateStatusDetail(Long id, TrangThaiChiTietPhieuDatPhong status);

    @Deprecated
    boolean updateTrangThaiByMaPhong(String maPhong, TrangThaiChiTietPhieuDatPhong trangThai);

    List<ChiTietPhieuDatPhong> getChiTietPhieuDatPhongByToPayment(TrangThaiPhieuDatPhong statusTicket,
            TrangThaiChiTietPhieuDatPhong statusDetail,
            String cccd);

    List<ChiTietPhieuDatPhong> getPhongDeHuyByCCCD(String cccd);

    List<ChiTietPhieuDatPhong> getPhongDeNhanByCCCD(String cccd);

    List<ChiTietPhieuDatPhong> timPhongDangThueBySDT(String soDienThoai);

    void giaHanNhieu(Map<Long, LocalDateTime> requests);

    boolean isRoomAvailableForExtension(Long chiTietId, LocalDateTime newEndTime);

    List<ChiTietPhieuDatPhong> timPhongDangThue(String keyword);
}
