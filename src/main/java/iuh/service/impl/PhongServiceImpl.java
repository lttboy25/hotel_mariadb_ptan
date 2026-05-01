package iuh.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import iuh.dao.impl.PhongDaoImpl;
import iuh.dto.PhongDTO;
import iuh.entity.Phong;
import iuh.entity.TinhTrangPhong;
import iuh.entity.TrangThaiPhong;
import iuh.mapper.Mapper;

public class PhongServiceImpl implements iuh.service.PhongService {
    private PhongDaoImpl phongDaoImpl = new PhongDaoImpl();
    private Mapper mapper = new Mapper();

    @Override
    public List<PhongDTO> getAllRoom() {

        return phongDaoImpl.findAll().stream().map(e ->
                PhongDTO.builder()
                        .maPhong(e.getMaPhong())
                        .soPhong(e.getSoPhong())
                        .loaiPhong(e.getLoaiPhong())
                        .trangThai(e.getTrangThai())
                        .tang(e.getTang())
                        .tinhTrang(e.getTinhTrang())
                        .moTa(e.getMoTa())
                        .build())
                .toList();
    }

    @Override
    public PhongDTO getRoomById(String maPhong) {
        return mapper.map(phongDaoImpl.findById(maPhong));
    }

    @Override
    public List<PhongDTO> getRoomByKeyword(String keyword) {
        return phongDaoImpl.findByKeyword(keyword)
                .stream()
                .map(e ->
                    PhongDTO.builder()
                            .maPhong(e.getMaPhong())
                            .soPhong(e.getSoPhong())
                            .loaiPhong(e.getLoaiPhong())
                            .trangThai(e.getTrangThai())
                            .tang(e.getTang())
                            .tinhTrang(e.getTinhTrang())
                            .moTa(e.getMoTa())
                            .build())
                .toList();
    }

    @Override
    public PhongDTO createPhong(PhongDTO phong) {
        return mapper.map(phongDaoImpl.save(mapper.map(phong)) );
    }

    @Override
    public PhongDTO updatePhong(PhongDTO dto) {
        Phong phong = mapper.map(dto);
        PhongDTO phongDTOUpdate = mapper.map(phongDaoImpl.updateRoom(phong));
        return phongDTOUpdate;
    }

    @Override
    public boolean deletePhong(String maPhong) {
        return phongDaoImpl.deleteRoom(maPhong);
    }

    @Override
    public boolean checkNull(PhongDTO phong) {
        if (phong.getTang() == 0 || phong.getLoaiPhong() == null || phong.getTinhTrang() == null
                || phong.getTrangThai() == null) {
            return false;
        }
        return true;
    }

    @Override
    public List<Integer> getAllTang() {
        return getAllRoom()
                .stream()
                .map(p -> p.getTang())
                .distinct()
                .toList();
    }

    @Override
    public List<TinhTrangPhong> getAllTinhTrang() {
        return getAllRoom()
                .stream()
                .map(PhongDTO::getTinhTrang)
                .distinct()
                .toList();
    }

    @Override
    public List<TrangThaiPhong> getAllTrangThai() {
        return getAllRoom()
                .stream()
                .map(PhongDTO::getTrangThai)
                .distinct()
                .toList();
    }

    @Override
    public List<Phong> getPhongByDate(LocalDateTime ngayNhan, LocalDateTime ngayTra) {

        if (ngayNhan == null || ngayTra == null) {
            throw new IllegalArgumentException("Ngày nhận/trả không được null");
        }

        if (!ngayNhan.isBefore(ngayTra)) {
            throw new IllegalArgumentException("Ngày nhận phải trước ngày trả");
        }

        return phongDaoImpl.findPhongByDate(ngayNhan, ngayTra);
    }

    @Override
    public List<Phong> getRoomsByStatus(TinhTrangPhong status) {
        List<Phong> ketqua = phongDaoImpl.getRoomsByStatus(status);

        if (ketqua == null || ketqua.isEmpty()) {
            throw new RuntimeException("Không tìm thấy phòng ứng với tình trạng trên");
        }

        return ketqua;
    }

    @Override
    public boolean updateStatusRoom(String maPhong, TrangThaiPhong trangThai, TinhTrangPhong tinhTrang) {
        return phongDaoImpl.updateStatusRoom(maPhong, trangThai, tinhTrang);
    }
}
