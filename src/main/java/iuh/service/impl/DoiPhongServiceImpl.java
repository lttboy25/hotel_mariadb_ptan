package iuh.service.impl;

import iuh.dao.impl.ChitietPhieuDatPhongDaoImpl;
import iuh.dao.impl.PhongDaoImpl;
import iuh.dto.*;
import iuh.entity.ChiTietPhieuDatPhong;
import iuh.entity.Phong;
import iuh.mapper.Mapper;
import iuh.service.DoiPhongService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DoiPhongServiceImpl implements DoiPhongService {
    private final PhongDaoImpl phongDaoImpl = new PhongDaoImpl();
    private final ChitietPhieuDatPhongDaoImpl ctDao = new ChitietPhieuDatPhongDaoImpl();

    // ✅ Không cần toDTO/toEntity ở đây vì DAO đã trả về DTO

    @Override
    public List<PhongDTO> getBookedRooms(String maPDP) {
        return ctDao.getByMaPhieuDatPhong(maPDP)
                .stream()
                .map(ChiTietPhieuDatPhong::getPhong)
                .map(Mapper::map)
                .collect(Collectors.toList());
    }

    @Override
    public List<PhongDTO> getAvailableRooms() {
        List<String> bookedIds = ctDao.getAll()
                .stream()
                .map(ct -> ct.getPhong().getMaPhong())
                .collect(Collectors.toList());

        return phongDaoImpl.getAvailableRooms()
                .stream()
                .filter(p -> !bookedIds.contains(p.getMaPhong()))
                .map(Mapper::map)
                .collect(Collectors.toList());
    }

    @Override
    public void doiPhong(String maPDP, String maPhongCu, String maPhongMoi) {
        List<ChiTietPhieuDatPhong> list = ctDao.getByMaPhieuDatPhong(maPDP);

        ChiTietPhieuDatPhong ct = list.stream()
                .filter(x -> x.getPhong().getMaPhong().equals(maPhongCu))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phòng cần đổi"));

        boolean isAvailable = ctDao.isRoomAvailable(
                maPhongMoi,
                ct.getThoiGianNhanPhong(),
                ct.getThoiGianTraPhong()
        );
        if (!isAvailable) {
            throw new RuntimeException("Phòng mới đã có người đặt!");
        }

        Phong phongMoi = phongDaoImpl.findById(maPhongMoi);
        if (phongMoi == null) {
            throw new RuntimeException("Phòng mới không tồn tại!");
        }

        ct.setPhong(phongMoi);  // ✅ set PhongDTO vào ChiTietPhieuDatPhongDTO
        ctDao.update(ct);
    }

    @Override
    public List<PhongDTO> getAllBookedRooms() {
        return ctDao.getAll()
                .stream()
                .map(ChiTietPhieuDatPhong::getPhong)
                .map(Mapper::map)
                .collect(Collectors.collectingAndThen(
                        Collectors.toMap(
                                PhongDTO::getMaPhong,
                                p -> p,
                                (a, b) -> a
                        ),
                        map -> new ArrayList<>(map.values())
                ));
    }

    @Override
    public double tinhPhiChenhLech(String maPhongCu, String maPhongMoi) {
        Phong cu = phongDaoImpl.findById(maPhongCu);
        Phong moi = phongDaoImpl.findById(maPhongMoi);
        if (cu == null || moi == null)
            throw new RuntimeException("Không tìm thấy phòng");

        return moi.getLoaiPhong().getGia() - cu.getLoaiPhong().getGia();
    }

    @Override
    public String getMaPDPByPhong(String maPhong) {
        return ctDao.getAll().stream()
                .filter(ct -> ct.getPhong().getMaPhong().equals(maPhong))
                .map(ct -> ct.getPhieuDatPhong().getMaPhieuDatPhong())
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phiếu đặt phòng cho phòng " + maPhong));
    }
}