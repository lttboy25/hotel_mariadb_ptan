/*
 * @ (#) DatPhongService.java     1.0    4/20/2026
 *
 * Copyright (c) 2026 IUH. All rights reserved.
 */
package iuh.service;


/*
 * @description
 * @author:NguyenTruong
 * @date:  4/20/2026
 * @version:    1.0
 */

import iuh.dao.DatPhongDao;
import iuh.dao.PhongDao;
import iuh.dto.DatPhongRequestDTO;
import iuh.dto.DatPhongResultDTO;
import iuh.entity.PhieuDatPhong;
import iuh.entity.Phong;
import iuh.mapper.Mapper;

import java.time.LocalDateTime;
import java.util.List;

public class DatPhongService {
    private final DatPhongDao datPhongDao;
    private PhongDao phongDao = new PhongDao();
    public DatPhongService() {
        this.datPhongDao = new DatPhongDao();
    }



    public DatPhongResultDTO datPhong(DatPhongRequestDTO request) {
        validateRequest(request);
        PhieuDatPhong phieuDatPhong = datPhongDao.saveDatPhong(request);
        return Mapper.mapToDatPhongResult(phieuDatPhong, request);
    }

    private void validateRequest(DatPhongRequestDTO request) {
        if (request == null) {
            throw new IllegalArgumentException("Thiếu dữ liệu đặt phòng.");
        }
        if (isBlank(request.getMaKhachHang())) {
            throw new IllegalArgumentException("Thiếu mã khách hàng.");
        }
        if (request.getMaPhongs() == null || request.getMaPhongs().isEmpty()) {
            throw new IllegalArgumentException("Phải chọn ít nhất một phòng.");
        }
        LocalDateTime checkIn = request.getCheckIn();
        LocalDateTime checkOut = request.getCheckOut();
        if (checkIn == null || checkOut == null) {
            throw new IllegalArgumentException("Thiếu thời gian nhận/trả phòng.");
        }
        if (checkIn.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Ngày nhận phòng không được trong quá khứ.");
        }
        if (!checkOut.isAfter(checkIn)) {
            throw new IllegalArgumentException("Thời gian trả phòng phải sau thời gian nhận phòng.");
        }
        if (request.getSoNguoiLon() < 0 || request.getSoTreEm() < 0) {
            throw new IllegalArgumentException("Số người lớn/trẻ em không hợp lệ.");
        }
        int tongNguoi = request.getSoNguoiLon() + request.getSoTreEm();
        if (tongNguoi <= 0) {
            throw new IllegalArgumentException("Số người phải lớn hơn 0.");
        }
        request.setSoNguoi(tongNguoi);
    }
    public List<Phong> getDsPhongTrong(LocalDateTime checkIn, LocalDateTime checkOut) {
        if (checkIn == null || checkOut == null) {
            throw new IllegalArgumentException("Thiếu thời gian nhận/trả phòng.");
        }
        if (!checkOut.isAfter(checkIn)) {
            throw new IllegalArgumentException("Thời gian trả phòng phải sau thời gian nhận phòng.");
        }
        return phongDao.findPhongByDate(checkIn, checkOut);
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
