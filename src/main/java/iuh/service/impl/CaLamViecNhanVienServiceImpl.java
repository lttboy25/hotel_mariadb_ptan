package iuh.service.impl;

import iuh.dao.CaDao;
import iuh.dao.CaLamViecNhanVienDao;
import iuh.dao.HoaDonDao;
import iuh.dao.impl.CaDaoImpl;
import iuh.dao.impl.CaLamViecNhanVienDaoImpl;
import iuh.dao.impl.HoaDonDaoImpl;
import iuh.dto.CaDTO;
import iuh.dto.CaLamViecNhanVienDTO;
import iuh.entity.Ca;
import iuh.entity.CaLamViecNhanVien;
import iuh.entity.NhanVien;
import iuh.service.CaLamViecNhanVienService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class CaLamViecNhanVienServiceImpl implements CaLamViecNhanVienService {

    private final CaLamViecNhanVienDao caLamViecNhanVienDao;
    private final HoaDonDao hoaDonDao;
    private final CaDao caDao;

    public CaLamViecNhanVienServiceImpl() {
        this.caLamViecNhanVienDao = new CaLamViecNhanVienDaoImpl();
        this.hoaDonDao = new HoaDonDaoImpl();
        this.caDao = new CaDaoImpl();
    }

    @Override
    public CaLamViecNhanVienDTO openShift(String maNhanVien, String maCa, double tienMoCa) {
        CaLamViecNhanVien activeShift = caLamViecNhanVienDao.findActiveShift(maNhanVien);
        if (activeShift != null) {
            throw new RuntimeException("Nhân viên hiện đang trong một ca làm việc khác.");
        }

        String maCaLamViec = generateNextMaCaLamViec();
        CaLamViecNhanVien entity = CaLamViecNhanVien.builder()
                .maCaLamViec(maCaLamViec)
                .tienMoCa(tienMoCa)
                .trangThai("ĐANG_LAM")
                .ngay(LocalDate.now())
                .thoiGianBatDau(LocalDateTime.now())
                .nhanVien(NhanVien.builder().maNhanVien(maNhanVien).build())
                .ca(Ca.builder().maCa(maCa).build())
                .build();

        return mapToDTO(caLamViecNhanVienDao.create(entity));
    }

    @Override
    public CaLamViecNhanVienDTO closeShift(String maCaLamViec, double tienKetCa) {
        CaLamViecNhanVien entity = caLamViecNhanVienDao.loadAll().stream()
                .filter(c -> c.getMaCaLamViec().equals(maCaLamViec))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Không tìm thấy ca làm việc."));

        LocalDateTime thoiGianKetThuc = LocalDateTime.now();
        double tongThu = hoaDonDao.calculateRevenue(entity.getNhanVien().getMaNhanVien(), entity.getThoiGianBatDau(), thoiGianKetThuc);
        
        entity.setThoiGianKetThuc(thoiGianKetThuc);
        entity.setTongThu(tongThu);
        entity.setTienKetCa(tienKetCa);
        entity.setTrangThai("DA_KET_THUC");
        
        return mapToDTO(caLamViecNhanVienDao.update(entity));
    }

    @Override
    public CaLamViecNhanVienDTO getActiveShift(String maNhanVien) {
        CaLamViecNhanVien active = caLamViecNhanVienDao.findActiveShift(maNhanVien);
        if (active != null) {
            // Cập nhật doanh thu tạm tính
            double currentRevenue = hoaDonDao.calculateRevenue(maNhanVien, active.getThoiGianBatDau(), LocalDateTime.now());
            active.setTongThu(currentRevenue);
        }
        return mapToDTO(active);
    }

    @Override
    public List<CaLamViecNhanVienDTO> getShiftHistory(String maNhanVien) {
        return caLamViecNhanVienDao.findCompletedShifts(maNhanVien).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public String generateNextMaCaLamViec() {
        List<CaLamViecNhanVien> all = caLamViecNhanVienDao.loadAll();
        int maxId = all.stream()
                .map(c -> {
                    String ma = c.getMaCaLamViec();
                    String numberOnly = ma.replaceAll("[^0-9]", "");
                    return numberOnly.isEmpty() ? 0 : Integer.parseInt(numberOnly);
                })
                .max(Integer::compare)
                .orElse(0);
        return String.format("CALV%03d", maxId + 1);
    }
    @Override
    public List<CaDTO> getAllCa() {
        return caDao.loadAll().stream()
                .map(this::mapToCaDTO)
                .collect(Collectors.toList());
    }
}
