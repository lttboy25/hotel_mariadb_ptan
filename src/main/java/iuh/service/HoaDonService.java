package iuh.service;

import iuh.dto.HoaDonDTO;
import iuh.enums.TrangThaiHoaDon;

import java.time.LocalDate;
import java.util.List;

public interface HoaDonService {

    List<HoaDonDTO> getAllHoaDon();

    List<HoaDonDTO> search(String keyword, LocalDate tuNgay, LocalDate denNgay, TrangThaiHoaDon trangThai);
}