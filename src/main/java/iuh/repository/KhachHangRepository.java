package iuh.repository;

import iuh.db.JPAUtil;
import iuh.entity.KhachHang;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.time.LocalDate;
import java.util.List;

public class KhachHangRepository {

    public KhachHang findById(String maKhachHang) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.find(KhachHang.class, maKhachHang);
        } finally {
            em.close();
        }
    }

    public List<KhachHang> findAll() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery("SELECT kh FROM KhachHang kh", KhachHang.class).getResultList();
        } finally {
            em.close();
        }
    }

    public boolean save(KhachHang khachHang) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            em.persist(khachHang);
            tx.commit();
            return true;
        } catch (Exception ex) {
            if (tx.isActive()) {
                tx.rollback();
            }
            return false;
        } finally {
            em.close();
        }
    }

    public static void main(String[] args) {
        KhachHangRepository repository = new KhachHangRepository();

        String maKhachHangTest = "KH_TEST_002";
        if (repository.findById(maKhachHangTest) != null) {
            System.out.println("Khach hang da ton tai: " + maKhachHangTest);
            return;
        }

        KhachHang khachHang = new KhachHang(
                maKhachHangTest,
                "075205007678",
                "Nguyen Van Test",
                "0909123456",
                "khachhang.test@example.com",
                LocalDate.now());

        boolean isSaved = repository.save(khachHang);
        if (isSaved) {
            System.out.println("Them khach hang thanh cong: " + khachHang.getMaKhachHang());
        } else {
            System.out.println("Them khach hang that bai");
        }
    }
}
