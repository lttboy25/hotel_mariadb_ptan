/*
 * @ (#) DoiPhongService.java       1.0
 *
 * Copyright (c) 2026 IUH. All rights reserved
 */
package iuh.service;


import iuh.dao.ChitietPhieuDatPhongDao;
import iuh.dao.PhongDao;
import iuh.entity.ChiTietPhieuDatPhong;
import iuh.entity.Phong;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/*
 * @description:
 * @author: Duc Thuan
 * @date: 23/4/2026
 * @version:    1.0
 * @created:
 */
public class DoiPhongService {
    private PhongDao phongDao = new PhongDao();
    private ChitietPhieuDatPhongDao ctDao = new ChitietPhieuDatPhongDao();

    //lay phong da dat
    public List<Phong> getBookedRooms(String maPDP){
        return ctDao.getByMaPhieuDatPhong(maPDP)
                .stream()
                .map(ChiTietPhieuDatPhong::getPhong)
                .collect(Collectors.toList());
    }

    //lay phong trong
    public List<Phong> getAvailableRooms() {
        // Lấy danh sách mã phòng đang được đặt
        List<String> bookedIds = ctDao.getAll()
                .stream()
                .map(ct -> ct.getPhong().getMaPhong())
                .collect(Collectors.toList());

        // Lọc ra những phòng trống và không nằm trong danh sách đã đặt
        return phongDao.getAvailableRooms()
                .stream()
                .filter(p -> !bookedIds.contains(p.getMaPhong()))
                .collect(Collectors.toList());
    }

    // doi phong
    public void doiPhong(String maPDP, String maPhongCu, String maPhongMoi){
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
        if(!isAvailable){
            throw new RuntimeException("Phòng mới đã có người đặt!");
        }
        //lay phong moi
        Phong phongMoi = phongDao.findById(maPhongMoi).get();

        if(phongMoi == null){
            throw new RuntimeException("Phòng mới không tồn tại!");
        }

        ct.setPhong(phongMoi);
        ctDao.update(ct);
    }

    public List<Phong> getAllBookedRooms() {
        return ctDao.getAll()
                .stream()
                .map(ChiTietPhieuDatPhong::getPhong)
                .collect(Collectors.collectingAndThen(
                        Collectors.toMap(
                                Phong::getMaPhong,
                                p -> p,
                                (a, b) -> a
                        ),
                        map -> new ArrayList<>(map.values())
                ));
    }

    public double tinhPhiChenhLech(String maPhongCu, String maPhongMoi) {
        Phong cu  = phongDao.findById(maPhongCu).orElseThrow();
        Phong moi = phongDao.findById(maPhongMoi).orElseThrow();
        return moi.getLoaiPhong().getGia() - cu.getLoaiPhong().getGia();
    }

    // DoiPhongService.java
    public String getMaPDPByPhong(String maPhong) {
        return ctDao.getAll().stream()
                .filter(ct -> ct.getPhong().getMaPhong().equals(maPhong))
                .map(ct -> ct.getPhieuDatPhong().getMaPhieuDatPhong())
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phiếu đặt phòng cho phòng " + maPhong));
    }
}
