/*
 * @ (#) DoiPhongServiceImpl.java       1.0
 *
 * Copyright (c) 2026 IUH. All rights reserved
 */
package iuh.service.impl;


import iuh.dao.impl.ChitietPhieuDatPhongDaoImpl;
import iuh.dao.impl.PhongDaoImpl;
import iuh.entity.ChiTietPhieuDatPhong;
import iuh.entity.Phong;
import iuh.service.DoiPhongService;

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
public class DoiPhongServiceImpl implements DoiPhongService {
    private PhongDaoImpl phongDaoImpl = new PhongDaoImpl();
    private ChitietPhieuDatPhongDaoImpl ctDao = new ChitietPhieuDatPhongDaoImpl();

    //lay phong da dat
    @Override
    public List<Phong> getBookedRooms(String maPDP){
        return ctDao.getByMaPhieuDatPhong(maPDP)
                .stream()
                .map(ChiTietPhieuDatPhong::getPhong)
                .collect(Collectors.toList());
    }

    //lay phong trong
    @Override
    public List<Phong> getAvailableRooms() {
        // Lấy danh sách mã phòng đang được đặt
        List<String> bookedIds = ctDao.getAll()
                .stream()
                .map(ct -> ct.getPhong().getMaPhong())
                .collect(Collectors.toList());

        // Lọc ra những phòng trống và không nằm trong danh sách đã đặt
        return phongDaoImpl.getAvailableRooms()
                .stream()
                .filter(p -> !bookedIds.contains(p.getMaPhong()))
                .collect(Collectors.toList());
    }

    // doi phong
    @Override
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
        Phong phongMoi = phongDaoImpl.findById(maPhongMoi).get();

        if(phongMoi == null){
            throw new RuntimeException("Phòng mới không tồn tại!");
        }

        ct.setPhong(phongMoi);
        ctDao.update(ct);
    }

    @Override
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

    @Override
    public double tinhPhiChenhLech(String maPhongCu, String maPhongMoi) {
        Phong cu  = phongDaoImpl.findById(maPhongCu).orElseThrow();
        Phong moi = phongDaoImpl.findById(maPhongMoi).orElseThrow();
        return moi.getLoaiPhong().getGia() - cu.getLoaiPhong().getGia();
    }

    // DoiPhongServiceImpl.java
    @Override
    public String getMaPDPByPhong(String maPhong) {
        return ctDao.getAll().stream()
                .filter(ct -> ct.getPhong().getMaPhong().equals(maPhong))
                .map(ct -> ct.getPhieuDatPhong().getMaPhieuDatPhong())
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phiếu đặt phòng cho phòng " + maPhong));
    }
}
