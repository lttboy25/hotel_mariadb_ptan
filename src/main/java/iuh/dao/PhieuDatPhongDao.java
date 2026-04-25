package iuh.dao;

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
}
