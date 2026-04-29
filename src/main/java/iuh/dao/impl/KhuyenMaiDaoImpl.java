/*
 * @ (#) KhuyenMaiDaoImpl.java     1.0    4/16/2026
 *
 * Copyright (c) 2026 IUH. All rights reserved.
 */
package iuh.dao.impl;

import iuh.dao.KhuyenMaiDao;
import iuh.entity.KhuyenMai;
import iuh.entity.TrangThai;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class KhuyenMaiDaoImpl extends AbstractGenericDaoImpl<KhuyenMai, String> implements iuh.dao.KhuyenMaiDao, KhuyenMaiDao {

    public KhuyenMaiDaoImpl() {
        super(KhuyenMai.class);
    }

    public KhuyenMaiDaoImpl(Class<KhuyenMai> entityClass) {
        super(entityClass);
    }

    @Override
    public List<KhuyenMai> findAll() {
        return loadAll();
    }

    @Override
    public Optional<KhuyenMai> findById(String maKhuyenMai) {
        if (maKhuyenMai == null || maKhuyenMai.isBlank()) {
            return Optional.empty();
        }
        return doInTransaction(em -> Optional.ofNullable(em.find(KhuyenMai.class, maKhuyenMai)));
    }

    @Override
    public boolean existsById(String maKhuyenMai) {
        return findById(maKhuyenMai).isPresent();
    }

    @Override
    public String generateNextMaKhuyenMai() {
        List<String> allCodes = doInTransaction(em -> em.createQuery("select k.maKhuyenMai from KhuyenMai k", String.class)
                .getResultList());

        Set<String> existingCodes = allCodes.stream()
                .filter(code -> code != null && !code.isBlank())
                .collect(Collectors.toSet());

        int nextNumber = allCodes.stream()
                .map(this::extractNumber)
                .max(Integer::compareTo)
                .orElse(0) + 1;

        String candidate = formatCode(nextNumber);
        while (existingCodes.contains(candidate)) {
            nextNumber++;
            candidate = formatCode(nextNumber);
        }
        return candidate;
    }

    @Override
    public KhuyenMai save(KhuyenMai khuyenMai) {
        return create(khuyenMai);
    }

    @Override
    public List<KhuyenMai> findByKeyword(String keyword) {
        String key = keyword == null ? "" : keyword.trim().toLowerCase();
        return doInTransaction(em -> em.createQuery("""
                        select k from KhuyenMai k
                        where lower(k.maKhuyenMai) like :keyword
                           or lower(k.tenKhuyenMai) like :keyword
                        order by k.ngayBatDau desc
                        """, KhuyenMai.class)
                .setParameter("keyword", "%" + key + "%")
                .getResultList());
    }

    @Override
    public List<KhuyenMai> findByTrangThai(TrangThai trangThai) {
        if (trangThai == null) {
            return findAll();
        }
        return doInTransaction(em -> em.createQuery("""
                        select k from KhuyenMai k
                        where k.trangThai = :trangThai
                        order by k.ngayBatDau desc
                        """, KhuyenMai.class)
                .setParameter("trangThai", trangThai)
                .getResultList());
    }
}
