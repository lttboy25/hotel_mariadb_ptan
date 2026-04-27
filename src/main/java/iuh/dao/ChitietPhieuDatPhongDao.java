/*
 * @ (#) ChitietPhieuDatPhongDao.java       1.0
 *
 * Copyright (c) 2026 IUH. All rights reserved
 */
package iuh.dao;


import iuh.db.JPAUtil;
import iuh.entity.ChiTietPhieuDatPhong;
import iuh.entity.PhieuDatPhong;
import jakarta.persistence.EntityManager;

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

    public ChiTietPhieuDatPhong findChiTietPhieuDatPhongByMaPhong(String maPhong){
        EntityManager em = JPAUtil.getEntityManager();
            try {
               String query = """
                       SELECT ctpdp 
                       FROM ChiTietPhieuDatPhong ctpdp
                       WHERE ctpdp.phong.maPhong = :maPhong
                       """;

               return(
                       doInTransaction(entityManager ->
                                       entityManager.createQuery(query,  ChiTietPhieuDatPhong.class)
                                       .setParameter("maPhong", maPhong)
                                       .getSingleResultOrNull()
                               )
                       );
           } catch (Exception e) {
               throw new RuntimeException(e);
           }
    }

    public boolean updateStatusDetailTicketByRoomCode(String maPhong, String status) {
        EntityManager em = JPAUtil.getEntityManager();

        try {
            em.getTransaction().begin();

            String query = """
                UPDATE ChiTietPhieuDatPhong ctpdp
                SET ctpdp.trangThai = :status
                WHERE ctpdp.phong.maPhong = :maPhong
        """;

            int updatedRows = em.createQuery(query)
                    .setParameter("status", status)
                    .setParameter("maPhong", maPhong)
                    .executeUpdate();

            em.getTransaction().commit();

            return updatedRows > 0;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException(e);
        } finally {
            em.close();
        }
    }

    public List<ChiTietPhieuDatPhong> getChiTietPhieuDatPhongByToPayment(String statusTicket, String statusDetail, String cccd) {
        return doInTransaction(em -> em.createQuery("""
                SELECT ctpdp FROM ChiTietPhieuDatPhong ctpdp
                WHERE ctpdp.phieuDatPhong.trangThai = :statusTicket
                       AND ctpdp.phieuDatPhong.khachHang.CCCD = :cccd 
                       AND ctpdp.trangThai = :statusDetail"""
                        , ChiTietPhieuDatPhong.class)
                .setParameter("statusTicket", statusTicket)
                .setParameter("cccd", cccd)
                .setParameter("statusDetail", statusDetail)
                .getResultList());
    }
}
