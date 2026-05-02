/*
 * @ (#) NhanVienDaoImpl.java       1.0
 *
 * Copyright (c) 2026 IUH. All rights reserved
 */
package iuh.dao.impl;


import iuh.db.JPAUtil;
import iuh.entity.NhanVien;
import iuh.entity.TaiKhoan;
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
public class NhanVienDaoImpl extends AbstractGenericDaoImpl<NhanVien, String> implements iuh.dao.NhanVienDao {
    public NhanVienDaoImpl() {
        super(NhanVien.class);
    }

    public NhanVienDaoImpl(Class entityClass) {
        super(entityClass);
    }

    @Override
    public List<NhanVien> findAll(){
        return loadAll();
    }

    @Override
    public Optional<NhanVien> findById(String maNhanVien){
        if(maNhanVien == null || maNhanVien.isEmpty()){
            return Optional.empty();
        }
        try(EntityManager em = JPAUtil.getEntityManager()){
            return Optional.ofNullable(em.find(NhanVien.class, maNhanVien));
        }
    }

    @Override
    public List<NhanVien> findByName(String name){
        try(EntityManager em = JPAUtil.getEntityManager()){
            String q = name == null ? "" : name.trim().toLowerCase();
            return em.createQuery("select n from NhanVien n WHERE LOWER(n.tenNhanVien) LIKE :name ", NhanVien.class)
                    .setParameter("name", "%" + q + "%").getResultList();
        }
    }

    @Override
    public NhanVien save(NhanVien nhanVien){
        return create(nhanVien);
    }

    @Override
    public NhanVien update(NhanVien nhanVien){
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            NhanVien managed = em.find(NhanVien.class, nhanVien.getMaNhanVien());
            if (managed == null) {
                throw new IllegalArgumentException("Khong tim thay nhan vien: " + nhanVien.getMaNhanVien());
            }

            managed.setTenNhanVien(nhanVien.getTenNhanVien());
            managed.setCCCD(nhanVien.getCCCD());
            managed.setGioiTinh(nhanVien.isGioiTinh());
            managed.setNgaySinh(nhanVien.getNgaySinh());
            managed.setEmail(nhanVien.getEmail());
            managed.setSoDienThoai(nhanVien.getSoDienThoai());
            managed.setNgayBatDau(nhanVien.getNgayBatDau());
            managed.setDiaChi(nhanVien.getDiaChi());
            managed.setTrangThai(nhanVien.getTrangThai());

            em.getTransaction().commit();
            return managed;
        }catch(Exception e){
            if(em.getTransaction().isActive()){
                em.getTransaction().rollback();
            }
            throw e;
        }finally{
            em.close();
        }
    }

    @Override
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

    @Override
    public List<NhanVien> search(String keyword) {
        EntityManager em = JPAUtil.getEntityManager();

        return em.createQuery(
                        "SELECT n FROM NhanVien n WHERE n.maNhanVien LIKE :kw OR n.tenNhanVien LIKE :kw",
                        NhanVien.class
                )
                .setParameter("kw", "%" + keyword + "%")
                .getResultList();
    }

    @Override
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

    @Override
    public boolean existsById(String maNhanVien) {
        return findById(maNhanVien).isPresent();
    }


    @Override
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

    @Override
    public boolean doiMatKhau(String maNV, String matKhauCu, String matKhauMoi) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            TaiKhoan taiKhoan = em.find(TaiKhoan.class, maNV);
            if (taiKhoan == null) {
                em.getTransaction().rollback();
                return false;
            }
            if (!taiKhoan.getMatKhau().equals(matKhauCu)) {
                em.getTransaction().rollback();
                return false;
            }

            taiKhoan.setMatKhau(matKhauMoi);
            em.merge(taiKhoan);
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

    @Override
    public String layMatKhauTheoMaNhanVien(String maNV) {
        try (EntityManager em = JPAUtil.getEntityManager()) {
            TaiKhoan taiKhoan = em.find(TaiKhoan.class, maNV);
            return taiKhoan != null ? taiKhoan.getMatKhau() : null;
        }
    }
}
