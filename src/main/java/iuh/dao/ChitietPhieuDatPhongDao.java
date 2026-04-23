/*
 * @ (#) ChitietPhieuDatPhongDao.java       1.0
 *
 * Copyright (c) 2026 IUH. All rights reserved
 */
package iuh.dao;


import iuh.entity.ChiTietPhieuDatPhong;

import java.time.LocalDateTime;
import java.util.List;

/*
 * @description:
 * @author: Duc Thuan
 * @date: 23/4/2026
 * @version:    1.0
 * @created:
 */
public class ChitietPhieuDatPhongDao extends AbstractGenericDaoImpl<ChiTietPhieuDatPhong, Long>{
        public ChitietPhieuDatPhongDao() {
            super(ChiTietPhieuDatPhong.class);
        }

    //lay phong theo ma phieu dat phong
    public List<ChiTietPhieuDatPhong> getByMaPhieuDatPhong(String maPDP){
        return doInTransaction(em ->
            em.createQuery("""
                SELECT ct FROM ChiTietPhieuDatPhong ct
                WHERE ct.phieuDatPhong.maPhieuDatPhong = :ma
            """, ChiTietPhieuDatPhong.class)
                    .setParameter("ma", maPDP)
                    .getResultList()
        );
    }

    //kiem tra trung lich
    public boolean isRoomAvailable(String maPhong, LocalDateTime checkIn, LocalDateTime checkOut){
            Long count = doInTransaction(em -> em.createQuery("""
                    SELECT COUNT(ct) FROM ChiTietPhieuDatPhong ct
                    WHERE ct.phong.maPhong = :maPhong
                    AND ct.thoiGianNhanPhong < :checkOut
                    AND ct.thoiGianTraPhong > :checkIn
               """, Long.class)
                    .setParameter("maPhong", maPhong)
                    .setParameter("checkIn", checkIn)
                    .setParameter("checkOut", checkOut)
                    .getSingleResult()
            );
            return count == 0;
    }

    public List<ChiTietPhieuDatPhong> getAll() {
        return doInTransaction(em ->
                em.createQuery("SELECT ct FROM ChiTietPhieuDatPhong ct", ChiTietPhieuDatPhong.class)
                        .getResultList()
        );
    }
}
