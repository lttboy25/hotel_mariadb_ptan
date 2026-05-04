package iuh.service.impl;

import iuh.dao.impl.HoaDonDaoImpl;
import iuh.dto.ChiTietHoaDonDTO;
import iuh.dto.HoaDonDTO;
import iuh.entity.HoaDon;
import iuh.enums.TrangThaiHoaDon;
import iuh.mapper.Mapper;
import iuh.service.HoaDonService;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

public class HoaDonServiceImpl implements HoaDonService {

    private final HoaDonDaoImpl hoaDonDao = new HoaDonDaoImpl();

    @Override
    public List<HoaDonDTO> getAllHoaDon() {
        return hoaDonDao.getAllHoaDon().stream()
                .map(Mapper::mapSummary)   // ← không load chiTietHoaDon, an toàn hơn
                .collect(Collectors.toList());
    }

    @Override
    public List<HoaDonDTO> search(String keyword,
                                  LocalDate tuNgay,
                                  LocalDate denNgay,
                                  TrangThaiHoaDon trangThai) {
        var tuDateTime = tuNgay != null ? tuNgay.atStartOfDay() : null;
        var denDateTime = denNgay != null ? denNgay.atTime(LocalTime.MAX) : null;

        return hoaDonDao.search(keyword, tuDateTime, denDateTime, trangThai)
                .stream()
                .map(Mapper::mapSummary)   // ← tương tự
                .collect(Collectors.toList());
    }


}