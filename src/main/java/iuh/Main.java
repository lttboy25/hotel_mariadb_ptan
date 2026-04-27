package iuh;

import iuh.db.JPAUtil;
import jakarta.persistence.EntityManager;

public class Main {
    public static void main(String[] args) {
        EntityManager em = null;
        try {

            em = JPAUtil.getEntityManager();
            Object result = em.createNativeQuery("SELECT 1").getSingleResult();
            System.out.println("Ket noi DB thanh cong, SELECT 1 = " + result);
        } catch (Exception e) {
            System.out.println("Ket noi DB that bai: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
            if (JPAUtil.getFactory().isOpen()) {
                JPAUtil.getFactory().close();
            }
        }
    }
}