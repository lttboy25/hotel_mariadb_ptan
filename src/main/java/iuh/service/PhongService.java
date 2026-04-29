package iuh.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import iuh.dao.impl.PhongDaoImpl;
import iuh.entity.Phong;

public class PhongService {
    private PhongDaoImpl phongDaoImpl = new PhongDaoImpl();

    public List<Phong> getAllRoom() {
        return phongDaoImpl.findAll();
    }

    public Optional<Phong> getRoomById(String maPhong) {
        return phongDaoImpl.findById(maPhong);
    }

    public List<Phong> getRoomByKeyword(String keyword) {
        return phongDaoImpl.findByKeyword(keyword);
    }

    public Phong createPhong(Phong phong) {
        return phongDaoImpl.save(phong);
    }

    public Phong updatePhong(Phong phong) {
        return phongDaoImpl.updateRoom(phong);
    }

    public boolean deletePhong(String maPhong) {
        return phongDaoImpl.deleteRoom(maPhong);
    }

    public boolean checkNull(Phong phong) {
        if (phong.getTang() == 0 || phong.getLoaiPhong() == null || phong.getTinhTrang() == null
                || phong.getTrangThai() == null) {
            return false;
        }
        return true;
    }

    public List<Integer> getAllTang() {
        return getAllRoom()
                .stream()
                .map(p -> p.getTang())
                .distinct()
                .toList();
    }

    public List<String> getAllTinhTrang() {
        return getAllRoom()
                .stream()
                .map(p -> p.getTinhTrang())
                .distinct()
                .toList();
    }

    public List<String> getAllTrangThai() {
        return getAllRoom()
                .stream()
                .map(p -> p.getTrangThai())
                .distinct()
                .toList();
    }

    public List<Phong> getPhongByDate(LocalDateTime ngayNhan, LocalDateTime ngayTra) {

        if (ngayNhan == null || ngayTra == null) {
            throw new IllegalArgumentException("Ngày nhận/trả không được null");
        }

        if (!ngayNhan.isBefore(ngayTra)) {
            throw new IllegalArgumentException("Ngày nhận phải trước ngày trả");
        }

        return phongDaoImpl.findPhongByDate(ngayNhan, ngayTra);
    }

    public List<Phong> getRoomsByStatus(String status) {
        List<Phong> ketqua = phongDaoImpl.getRoomsByStatus(status);

        if (ketqua == null || ketqua.isEmpty()) {
            throw new RuntimeException("Không tìm thấy phòng ứng với tình trạng trên");
        }

        return ketqua;
    }

    public boolean updateStatusRoom(String maPhong, String trangThai, String tinhTrang) {
        return phongDaoImpl.updateStatusRoom(maPhong, trangThai, tinhTrang);
    }
}
