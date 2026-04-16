/*
 * @ (#) NhanVienDao.java       1.0
 *
 * Copyright (c) 2026 IUH. All rights reserved
 */
package iuh.dao;


import iuh.db.JPAUtil;
import iuh.entity.NhanVien;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Optional;

/*
 * @description:
 * @author: Duc Thuan
 * @date: 16/4/2026
 * @version:    1.0
 * @created:
 */
public class NhanVienDao {
    public List<NhanVien> findAll(){
        try(EntityManager em = JPAUtil.getEntityManager()){
            return em.createQuery("select n from NhanVien n order by n.tenNhanVien asc ", NhanVien.class).getResultList();
        }
    }

    public Optional<NhanVien> findById(String maNhanVien){
        if(maNhanVien == null || maNhanVien.isEmpty()){
            return Optional.empty();
        }
        try(EntityManager em = JPAUtil.getEntityManager()){
            return Optional.ofNullable(em.find(NhanVien.class, maNhanVien));
        }
    }

    public List<NhanVien> findByName(String name){
        try(EntityManager em = JPAUtil.getEntityManager()){
            String q = name == null ? "" : name.trim().toLowerCase();
            return em.createQuery("select n from NhanVien n WHERE LOWER(n.tenNhanVien) LIKE :name ", NhanVien.class)
                    .setParameter("name", "%" + q + "%").getResultList();
        }
    }

    public NhanVien save(NhanVien nhanVien){
        EntityManager em = JPAUtil.getEntityManager();
        try{
            em.getTransaction().begin();
            em.persist(nhanVien);
            em.getTransaction().commit();
            return nhanVien;
        }catch(Exception e){
            if(em.getTransaction().isActive()){
                em.getTransaction().rollback();
            }
            throw e;
        }finally{
            em.close();
        }
    }

    public NhanVien update(NhanVien nhanVien){
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            NhanVien merged = em.merge(nhanVien);
            em.getTransaction().commit();
            return merged;
        }catch(Exception e){
            if(em.getTransaction().isActive()){
                em.getTransaction().rollback();
            }
            throw e;
        }finally{
            em.close();
        }
    }

    public boolean delete(String maNhanVien){
        EntityManager em = JPAUtil.getEntityManager();
        try{
            NhanVien entity = em.find(NhanVien.class, maNhanVien);
            if(entity == null){
                return false;
            }
            em.getTransaction().begin();
            em.remove(entity);
            em.getTransaction().commit();
            return true;
        }catch(Exception e){
            if(em.getTransaction().isActive()){
                em.getTransaction().rollback();
            }
            throw e;
        }finally{
            em.close();
        }
    }
}
