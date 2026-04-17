package iuh.dao;

import java.util.List;
import java.util.Optional;

import iuh.db.JPAUtil;
import iuh.entity.Phong;
import jakarta.persistence.EntityManager;

public class PhongDao {

    public PhongDao() {

    }

    public List<Phong> findAll() {
        try (EntityManager em = JPAUtil.getEntityManager()) {
            return em.createQuery("SELECT p FROM Phong p ORDER BY p.maPhong", Phong.class).getResultList();
        }
    }

    public Optional<Phong> findById(String maPhong) {
        try (EntityManager em = JPAUtil.getEntityManager()) {
            String query = "SELECT p FROM Phong p WHERE p.maPhong = :maPhong";
            Phong phong = em.createQuery(query, Phong.class)
                    .setParameter("maPhong", maPhong)
                    .getResultStream()
                    .findFirst()
                    .orElse(null);

            return Optional.ofNullable(phong);
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public List<Phong> findByKeyword(String keyword) {
        try (EntityManager em = JPAUtil.getEntityManager()) {

            String query = """
                    SELECT p FROM Phong p
                    WHERE LOWER(p.maPhong) LIKE LOWER(CONCAT('%', :keyword, '%'))
                    OR LOWER(p.soPhong) LIKE LOWER(CONCAT('%', :keyword, '%'))
                    OR LOWER(p.loaiPhong.tenLoaiPhong) LIKE LOWER(CONCAT('%', :keyword, '%'))
                    ORDER BY p.maPhong
                                """;

            List<Phong> result = em.createQuery(query, Phong.class)
                    .setParameter("keyword", keyword)
                    .getResultList();

            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

}
