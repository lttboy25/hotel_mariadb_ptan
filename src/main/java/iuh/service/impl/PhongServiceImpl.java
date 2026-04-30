package iuh.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import iuh.dao.impl.PhongDaoImpl;
import iuh.entity.Phong;
import iuh.entity.TinhTrangPhong;
import iuh.entity.TrangThaiPhong;

public class PhongServiceImpl implements iuh.service.PhongService {
    private PhongDaoImpl phongDaoImpl = new PhongDaoImpl();

    @Override
    public List<Phong> getAllRoom() {
        return phongDaoImpl.findAll();
    }

    @Override
    public Optional<Phong> getRoomById(String maPhong) {
        return phongDaoImpl.findById(maPhong);
    }

    @Override
    public List<Phong> getRoomByKeyword(String keyword) {
        return phongDaoImpl.findByKeyword(keyword);
    }

    @Override
    public Phong createPhong(Phong phong) {
        return phongDaoImpl.save(phong);
    }

    @Override
    public Phong updatePhong(Phong phong) {
        return phongDaoImpl.updateRoom(phong);
    }

    @Override
    public boolean deletePhong(String maPhong) {
        return phongDaoImpl.deleteRoom(maPhong);
    }

    @Override
    public boolean checkNull(Phong phong) {
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
                .map(Phong::getTinhTrang)
                .distinct()
                .toList();
    }

    @Override
    public List<TrangThaiPhong> getAllTrangThai() {
        return getAllRoom()
                .stream()
                .map(Phong::getTrangThai)
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
