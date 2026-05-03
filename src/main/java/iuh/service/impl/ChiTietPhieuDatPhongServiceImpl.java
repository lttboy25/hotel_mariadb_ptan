package iuh.service.impl;

import iuh.dao.impl.ChitietPhieuDatPhongDaoImpl;
import iuh.dto.ChiTietPhieuDatPhongDTO;
import iuh.entity.ChiTietPhieuDatPhong;
import iuh.enums.TrangThaiChiTietPhieuDatPhong;
import iuh.enums.TrangThaiPhieuDatPhong;
import iuh.mapper.Mapper;
import iuh.service.ChiTietPhieuDatPhongService;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;

import java.util.ArrayList;
import java.util.List;

public class ChiTietPhieuDatPhongServiceImpl implements ChiTietPhieuDatPhongService {
    ChitietPhieuDatPhongDaoImpl chitietPhieuDatPhongDao = new ChitietPhieuDatPhongDaoImpl();

    @Override
    public List<ChiTietPhieuDatPhongDTO> getChiTietPhieuDatPhongByMaPDP(String maPDP) {
        if (maPDP == null) return new ArrayList<>();

        return chitietPhieuDatPhongDao.getByMaPhieuDatPhong(maPDP)
                .stream()
                .map(Mapper::map)
                .toList();
    }

    @Override
    public boolean updateStatusDetail(Long id, TrangThaiChiTietPhieuDatPhong status) {
        if (id == null || status == null) {
            return false;
        }
        return chitietPhieuDatPhongDao.updateStatusDetail(id, status);
    }

    @Override
    @Deprecated
    public boolean updateTrangThaiByMaPhong(String maPhong, TrangThaiChiTietPhieuDatPhong trangThai) {
        if (maPhong == null || trangThai == null) {
            return false;
        }

        return chitietPhieuDatPhongDao.updateStatusDetailTicketByRoomCode(maPhong, trangThai);
    }

    @Override
    public List<ChiTietPhieuDatPhongDTO> getChiTietPhieuDatPhongByToPayment(
            TrangThaiPhieuDatPhong statusTicket,
            TrangThaiChiTietPhieuDatPhong statusDetail,
            String cccd) {

        if (statusTicket == null || statusDetail == null || cccd == null) {
            throw new IllegalArgumentException("Tham số không hợp lệ");
        }

        return chitietPhieuDatPhongDao.getChiTietPhieuDatPhongByToPayment(statusTicket, statusDetail, cccd)
                .stream()
                .map(Mapper::map)
                .toList();
    }

    @Override
    public List<ChiTietPhieuDatPhongDTO> getPhongDeHuyByCCCD(String cccd) {
        if (cccd == null || cccd.trim().isEmpty()) return new ArrayList<>();

        return chitietPhieuDatPhongDao.getPhongDeHuyByCCCD(cccd)
                .stream()
                .map(Mapper::map)
                .toList();
    }


    @Override
    public List<ChiTietPhieuDatPhongDTO> getPhongDeNhanByCCCD(String cccd) {
        if (cccd == null || cccd.trim().isEmpty()) return new ArrayList<>();

        return chitietPhieuDatPhongDao.getPhongDeNhanByCCCD(cccd)
                .stream()
                .map(Mapper::map)
                .toList();
    }

    @Override
    public List<ChiTietPhieuDatPhongDTO> timPhongDangThueBySDT(String soDienThoai) {
        if (soDienThoai == null || soDienThoai.isBlank())
            throw new IllegalArgumentException("Số điện thoại không được để trống.");

        return chitietPhieuDatPhongDao.getDangThueBySDT(soDienThoai.trim())
                .stream()
                .map(Mapper::map)
                .toList();
    }


    @Override
    public void giaHanNhieu(Map<Long, LocalDateTime> requests) {

        for (Map.Entry<Long, LocalDateTime> entry : requests.entrySet()) {

            Long id = entry.getKey();
            LocalDateTime thoiGianTraMoi = entry.getValue();

            ChiTietPhieuDatPhong entity = chitietPhieuDatPhongDao.findById(id);
            if (entity == null)
                throw new IllegalArgumentException("Không tìm thấy ID: " + id);

            if (thoiGianTraMoi == null || !thoiGianTraMoi.isAfter(entity.getThoiGianTraPhong()))
                throw new IllegalArgumentException(
                        "Thời gian gia hạn không hợp lệ cho phòng "
                                + (entity.getPhong() != null ? entity.getPhong().getSoPhong() : id)
                );

            int soGioMoi = (int) ChronoUnit.HOURS.between(
                    entity.getThoiGianNhanPhong(), thoiGianTraMoi);

            chitietPhieuDatPhongDao.updateGiaHan(id, thoiGianTraMoi, soGioMoi);
        }
    }


    @Override
    public boolean isRoomAvailableForExtension(Long chiTietId, LocalDateTime newEndTime) {
        return chitietPhieuDatPhongDao.isRoomAvailableForExtension(chiTietId, newEndTime);
    }

    @Override
    public List<ChiTietPhieuDatPhongDTO> timPhongDangThue(String keyword) {
        return chitietPhieuDatPhongDao.searchPhongDangThue(keyword)
                .stream()
                .map(Mapper::map)
                .toList();
    }
}
