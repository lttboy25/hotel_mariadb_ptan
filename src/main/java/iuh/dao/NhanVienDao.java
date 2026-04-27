/*
 * @ (#) NhanVienDao.java       1.0
 *
 * Copyright (c) 2026 IUH. All rights reserved
 */
package iuh.dao;


import iuh.db.JPAUtil;
import iuh.entity.KhuyenMai;
import iuh.entity.NhanVien;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/*
 * @description:
 * @author: Duc Thuan
 * @date: 16/4/2026
 * @version:    1.0
 * @created:
 */
public class NhanVienDao extends AbstractGenericDaoImpl<NhanVien, String>{
    public NhanVienDao() {
        super(NhanVien.class);
    }

    public NhanVienDao(Class entityClass) {
        super(entityClass);
    }

    public List<NhanVien> findAll(){
        return loadAll();
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
        return create(nhanVien);
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

    public List<NhanVien> search(String keyword) {
        EntityManager em = JPAUtil.getEntityManager();

        return em.createQuery(
                        "SELECT n FROM NhanVien n WHERE n.maNhanVien LIKE :kw OR n.tenNhanVien LIKE :kw",
                        NhanVien.class
                )
                .setParameter("kw", "%" + keyword + "%")
                .getResultList();
    }

    public String generateMaNV() {
        List<String> allCodes = doInTransaction(em ->
                em.createQuery("select n.maNhanVien from NhanVien n", String.class)
                        .getResultList()
        );

        Set<String> existingCodes = allCodes.stream()
                .filter(code -> code != null && !code.isBlank())
                .collect(Collectors.toSet());

        int nextNumber = allCodes.stream()
                .map(this::extractNumber)
                .max(Integer::compareTo)
                .orElse(0) + 1;

        String candidate = formatMaNV(nextNumber);

        while (existingCodes.contains(candidate)) {
            nextNumber++;
            candidate = formatMaNV(nextNumber);
        }

        return candidate;
    }

    private int extractNumber(String code) {
        if (code == null) return 0;

        try {
            return Integer.parseInt(code.replaceAll("\\D+", ""));
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private String formatMaNV(int number) {
        return String.format("NV%03d", number);
    }

    public boolean existsById(String maNhanVien) {
        return findById(maNhanVien).isPresent();
    }


    public NhanVien login(String maNV, String matKhau) {
        try (EntityManager em = JPAUtil.getEntityManager()) {
            return em.createQuery(
                            "SELECT nv FROM NhanVien nv JOIN nv.taiKhoan tk " +
                                    "WHERE nv.maNhanVien = :maNV AND tk.matKhau = :mk",
                            NhanVien.class)
                    .setParameter("maNV", maNV)
                    .setParameter("mk", matKhau)
                    .getSingleResult();

        } catch (NoResultException e) {
            return null;
        }
    }
}
