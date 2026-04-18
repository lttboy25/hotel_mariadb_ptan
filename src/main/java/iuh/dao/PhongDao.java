package iuh.dao;

import java.util.List;
import java.util.Optional;

import iuh.db.JPAUtil;
import iuh.entity.Phong;
import iuh.entity.Phong;
import jakarta.persistence.EntityManager;

public class PhongDao extends AbstractGenericDaoImpl<Phong, String> {

    public PhongDao() {
        super(Phong.class);
    }

    public List<Phong> findAll() {
        return loadAll();
    }

    public Optional<Phong> findById(String maPhong) {
        if (maPhong == null || maPhong.isEmpty()) {
            return Optional.empty();
        }
        try (EntityManager em = JPAUtil.getEntityManager()) {
            return Optional.ofNullable(em.find(Phong.class, maPhong));
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

    public String generateMaPhong(int tang) {
        try (EntityManager em = JPAUtil.getEntityManager()) {

            String query = """
                        SELECT p.maPhong FROM Phong p
                        WHERE p.tang = :tang
                        ORDER BY p.maPhong DESC
                    """;

            List<String> list = em.createQuery(query, String.class)
                    .setParameter("tang", tang)
                    .setMaxResults(1)
                    .getResultList();

            int nextNumber = 1;

            if (!list.isEmpty()) {
                String maPhongCuoiCung = list.get(0);

                String numberPart = maPhongCuoiCung.substring(2);
                nextNumber = Integer.parseInt(numberPart) + 1;
            }

            return String.format("P%d%02d", tang, nextNumber);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Phong save(Phong phong) {
        String maPhong = generateMaPhong(phong.getTang());
        phong.setMaPhong(maPhong);
        phong.setSoPhong(generateSoPhong(maPhong));
        return create(phong);
    }

    public String generateSoPhong(String maPhong) {
        return maPhong.substring(1);
    }

    public Phong updateRoom(Phong Phong) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            Phong merged = em.merge(Phong);
            em.getTransaction().commit();
            return merged;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    public boolean deleteRoom(String maPhong) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            Phong entity = em.find(Phong.class, maPhong);
            if (entity == null) {
                return false;
            }
            em.getTransaction().begin();
            em.remove(entity);
            em.getTransaction().commit();
            return true;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

}
