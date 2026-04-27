/*
 * @ (#) KhuyenMaiDao.java     1.0    4/16/2026
 *
 * Copyright (c) 2026 IUH. All rights reserved.
 */
package iuh.dao;

import iuh.entity.KhuyenMai;
import iuh.entity.TrangThai;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class KhuyenMaiDao extends AbstractGenericDaoImpl<KhuyenMai, String> {

    private static final Pattern MA_KHUYEN_MAI_PATTERN = Pattern.compile("^KM(\\d+)$");

    public KhuyenMaiDao() {
        super(KhuyenMai.class);
    }

    public KhuyenMaiDao(Class<KhuyenMai> entityClass) {
        super(entityClass);
    }

    public List<KhuyenMai> findAll() {
        return loadAll();
    }

    public Optional<KhuyenMai> findById(String maKhuyenMai) {
        if (maKhuyenMai == null || maKhuyenMai.isBlank()) {
            return Optional.empty();
        }
        return doInTransaction(em -> Optional.ofNullable(em.find(KhuyenMai.class, maKhuyenMai)));
    }

    public boolean existsById(String maKhuyenMai) {
        return findById(maKhuyenMai).isPresent();
    }

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

    private int extractNumber(String maKhuyenMai) {
        if (maKhuyenMai == null) {
            return 0;
        }
        Matcher matcher = MA_KHUYEN_MAI_PATTERN.matcher(maKhuyenMai.trim().toUpperCase());
        if (!matcher.matches()) {
            return 0;
        }
        return Integer.parseInt(matcher.group(1));
    }

    private String formatCode(int number) {
        return String.format("KM%03d", number);
    }

    public KhuyenMai save(KhuyenMai khuyenMai) {
        return create(khuyenMai);
    }

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
