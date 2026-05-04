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
import java.util.stream.Collectors;

public class ChiTietPhieuDatPhongServiceImpl implements ChiTietPhieuDatPhongService {
    ChitietPhieuDatPhongDaoImpl chitietPhieuDatPhongDao = new ChitietPhieuDatPhongDaoImpl();

    @Override
    public List<ChiTietPhieuDatPhongDTO> getChiTietPhieuDatPhongByMaPDP(String maPDP) {
        List<ChiTietPhieuDatPhong> ds = new ArrayList<>();
        if (maPDP == null) {
            return ds.stream().map(
                    e -> Mapper.map(e)).collect(Collectors.toList());
        }
        return chitietPhieuDatPhongDao.getByMaPhieuDatPhong(maPDP).stream().map(
                e -> Mapper.map(e)).collect(Collectors.toList());
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
    public List<ChiTietPhieuDatPhong> getChiTietPhieuDatPhongByToPayment(
            TrangThaiChiTietPhieuDatPhong statusDetail,
            String cccd) {
        if (statusDetail == null || cccd == null) {
            throw new NullPointerException("Lấy danh sách phiếu đặt phòng bị rỗng");
        }

        return chitietPhieuDatPhongDao.getChiTietPhieuDatPhongByToPayment(statusDetail, cccd);

    }

    @Override
    public List<ChiTietPhieuDatPhongDTO> getPhongDeHuyByCCCD(String cccd) {
        if (cccd == null || cccd.trim().isEmpty()) {
            return new ArrayList<>();
        }

        return chitietPhieuDatPhongDao.getPhongDeHuyByCCCD(cccd)
                .stream()
                .map(e -> Mapper.map(e))
                .collect(Collectors.toList());
    }

    @Override
    public List<ChiTietPhieuDatPhong> getPhongDeNhanByCCCD(String cccd) {
        if (cccd == null || cccd.trim().isEmpty()) {
            return new ArrayList<>();
        }
        return chitietPhieuDatPhongDao.getPhongDeNhanByCCCD(cccd);
    }

    @Override
    public List<ChiTietPhieuDatPhongDTO> timPhongDangThueBySDT(String soDienThoai) {
        if (soDienThoai == null || soDienThoai.isBlank())
            throw new IllegalArgumentException("Số điện thoại không được để trống.");
        return chitietPhieuDatPhongDao.getDangThueBySDT(soDienThoai.trim()).stream().map(
                e -> Mapper.map(e)).collect(Collectors.toList());
    }

    @Override
    public void giaHanNhieu(Map<Long, LocalDateTime> requests) {
        for (Map.Entry<Long, LocalDateTime> entry : requests.entrySet()) {
            Long id = entry.getKey();
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

    @Override
    public boolean isRoomAvailableForExtension(Long chiTietId, LocalDateTime newEndTime) {
        return chitietPhieuDatPhongDao.isRoomAvailableForExtension(chiTietId, newEndTime);
    }

    @Override
    public List<ChiTietPhieuDatPhongDTO> timPhongDangThue(String keyword) {
        return chitietPhieuDatPhongDao.searchPhongDangThue(keyword).stream().map(
                e -> Mapper.map(e)).collect(Collectors.toList());
    }
}