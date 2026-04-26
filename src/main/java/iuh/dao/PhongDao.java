package iuh.dao;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import iuh.db.JPAUtil;
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

            return em.createQuery(query, Phong.class)
                    .setParameter("keyword", keyword)
                    .getResultList();
        } catch (Exception e) {
            System.err.println("Lỗi tìm phòng theo từ khóa: " + e.getMessage());
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
            System.err.println("Lỗi sinh mã phòng: " + e.getMessage());
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

    // lây phòng troongs
    public List<Phong> getAvailableRooms() {
        return doInTransaction(em -> em.createQuery("""
                    SELECT p FROM Phong p
                    WHERE p.tinhTrang = "Trống"
                """, Phong.class).getResultList());
    }

    public List<Phong> findPhongByDate(LocalDateTime ngayNhanPhong, LocalDateTime ngayTraPhong) {
        try (EntityManager em = JPAUtil.getEntityManager()) {

            String query = """
                        SELECT p FROM Phong p
                        WHERE (p.tinhTrang = 'Trống' OR p.trangThai = 'Sẵn sàng')
                        AND NOT EXISTS (
                            SELECT 1 FROM ChiTietPhieuDatPhong ct
                            WHERE ct.phong.maPhong = p.maPhong
                            AND ct.thoiGianNhanPhong < :ngayTraPhong
                            AND ct.thoiGianTraPhong > :ngayNhanPhong
                        )
                        ORDER BY p.maPhong
                    """;

            return em.createQuery(query, Phong.class)
                    .setParameter("ngayNhanPhong", ngayNhanPhong)
                    .setParameter("ngayTraPhong", ngayTraPhong)
                    .getResultList();

        } catch (Exception e) {
            System.err.println("Lỗi tìm phòng trống: " + e.getMessage());
            return List.of();
        }
    }

    public List<Phong> getRoomsByStatus(String status) {
        return doInTransaction(em -> em.createQuery("""
                SELECT p FROM Phong p
                WHERE p.tinhTrang = :status """ , Phong.class).setParameter("status", status).getResultList());
    }

    public boolean updateStatusRoom(String maPhong, String trangThai) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();

            String query = """
                UPDATE Phong p
                SET p.trangThai = :status
                WHERE p.maPhong = :maPhong
        """;

            int updatedRows = em.createQuery(query)
                    .setParameter("status", trangThai)
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
}
