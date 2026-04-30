package iuh.dao.impl;

import iuh.db.JPAUtil;
import iuh.entity.PhieuDatPhong;
import iuh.entity.TrangThaiPhieuDatPhong;
import jakarta.persistence.EntityManager;

import java.util.List;

public class PhieuDatPhongDaoImpl extends AbstractGenericDaoImpl<PhieuDatPhong, String>
        implements iuh.dao.PhieuDatPhongDao {

    public PhieuDatPhongDaoImpl() {
        super(PhieuDatPhong.class);
    }

    @Override
    public List<PhieuDatPhong> getAll() {
        return doInTransaction(em -> em.createQuery("SELECT ct FROM PhieuDatPhong ct", PhieuDatPhong.class)
                .getResultList());
    }

    @Override
    public List<PhieuDatPhong> getPhieuDatPhongByStatus(TrangThaiPhieuDatPhong status) {
        return doInTransaction(em -> em.createQuery("""
                SELECT pdp FROM PhieuDatPhong pdp
                WHERE pdp.trangThai = :status """, PhieuDatPhong.class).setParameter("status", status).getResultList());
    }

    @Override
    public PhieuDatPhong getPhieuDatPhongByCode(String maPhieu) {
        return doInTransaction(em -> em.createQuery("""
                SELECT pdp FROM PhieuDatPhong pdp
                WHERE pdp.maPhieuDatPhong = :maPhieu """, PhieuDatPhong.class).setParameter("maPhieu", maPhieu)
                .getSingleResultOrNull());
    }

    @Override
    public boolean updateStatusBookingTicket(String maPhieu, TrangThaiPhieuDatPhong trangThai) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();

            String query = """
                            UPDATE PhieuDatPhong pdp
                            SET pdp.trangThai = :status
                            WHERE pdp.maPhieuDatPhong = :maPhieu
                    """;

            int updatedRows = em.createQuery(query)
                    .setParameter("status", trangThai)
                    .setParameter("maPhieu", maPhieu)
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

    @Override
    public List<PhieuDatPhong> getPhieuDatPhongByToPayment(TrangThaiPhieuDatPhong status, String cccd) {
        return doInTransaction(em -> em.createQuery("""
                SELECT pdp FROM PhieuDatPhong pdp
                WHERE pdp.trangThai = :status
                       AND pdp.khachHang.CCCD = :cccd""", PhieuDatPhong.class)
                .setParameter("status", status)
                .setParameter("cccd", cccd)
                .getResultList());
    }
}
