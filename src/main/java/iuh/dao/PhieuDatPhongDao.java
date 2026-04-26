package iuh.dao;

import iuh.db.JPAUtil;
import iuh.entity.ChiTietPhieuDatPhong;
import iuh.entity.PhieuDatPhong;
import iuh.entity.Phong;
import jakarta.persistence.EntityManager;

import java.util.List;

public class PhieuDatPhongDao extends AbstractGenericDaoImpl<PhieuDatPhong, String> {

    public PhieuDatPhongDao() {super(PhieuDatPhong.class);}

    public List<PhieuDatPhong> getAll() {
        return doInTransaction(em ->
                em.createQuery("SELECT ct FROM PhieuDatPhong ct", PhieuDatPhong.class)
                        .getResultList()
        );
    }

    public List<PhieuDatPhong> getPhieuDatPhongByStatus(String status) {
        return doInTransaction(em -> em.createQuery("""
                SELECT pdp FROM PhieuDatPhong pdp
                WHERE pdp.trangThai = :status """
                , PhieuDatPhong.class).setParameter("status", status).getResultList());
    }

    public PhieuDatPhong getPhieuDatPhongByCode(String maPhieu) {
        return doInTransaction(em -> em.createQuery("""
                SELECT pdp FROM PhieuDatPhong pdp
                WHERE pdp.maPhieuDatPhong = :maPhieu """
                , PhieuDatPhong.class).setParameter("maPhieu", maPhieu).getSingleResultOrNull());
    }

    public boolean updateStatusBookingTicket(String maPhieu, String trangThai) {
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
}
