/*
 * @ (#) DatPhongServiceImpl.java     1.0    4/20/2026
 *
 * Copyright (c) 2026 IUH. All rights reserved.
 */
package iuh.service.impl;


/*
 * @description
 * @author:NguyenTruong
 * @date:  4/20/2026
 * @version:    1.0
 */

import iuh.dao.impl.DatPhongDaoImpl;
import iuh.dao.impl.PhongDaoImpl;
import iuh.dto.DatPhongRequestDTO;
import iuh.dto.DatPhongResultDTO;
import iuh.entity.PhieuDatPhong;
import iuh.entity.Phong;
import iuh.mapper.Mapper;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

public class DatPhongServiceImpl implements iuh.service.DatPhongService, Serializable {
    private final DatPhongDaoImpl datPhongDao;
    private PhongDaoImpl phongDaoImpl = new PhongDaoImpl();
    public DatPhongServiceImpl() {
        this.datPhongDao = new DatPhongDaoImpl();
    }



    @Override
    public DatPhongResultDTO datPhong(DatPhongRequestDTO request) {
        validateRequest(request);
        PhieuDatPhong phieuDatPhong = datPhongDao.saveDatPhong(request);
        return Mapper.mapToDatPhongResult(phieuDatPhong, request);
    }

    @Override
    public List<Phong> getDsPhongTrong(LocalDateTime checkIn, LocalDateTime checkOut) {
        if (checkIn == null || checkOut == null) {
            throw new IllegalArgumentException("Thiếu thời gian nhận/trả phòng.");
        }
        if (!checkOut.isAfter(checkIn)) {
            throw new IllegalArgumentException("Thời gian trả phòng phải sau thời gian nhận phòng.");
        }
        return phongDaoImpl.findPhongByDate(checkIn, checkOut);
    }

}
