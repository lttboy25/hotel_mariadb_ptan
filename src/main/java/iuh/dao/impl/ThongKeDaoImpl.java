/*
 * @ (#) ThongKeDaoImpl.java     1.0    4/28/2026
 *
 * Copyright (c) 2026 IUH. All rights reserved.
 */
package iuh.dao.impl;

import iuh.db.JPAUtil;
import iuh.dto.ThongKeDTO;
import jakarta.persistence.EntityManager;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ThongKeDaoImpl implements iuh.dao.ThongKeDao {

    @Override
    public long demTongSoPhong() {
        try (EntityManager em = JPAUtil.getEntityManager()) {
            Long result = em.createQuery("SELECT COUNT(p) FROM Phong p", Long.class)
                    .getSingleResult();
            return result == null ? 0L : result;
        }
    }

    @Override
    public long demPhongDangSuDung(LocalDateTime thoiDiem) {
        try (EntityManager em = JPAUtil.getEntityManager()) {
            Long result = em.createQuery("""
                    SELECT COUNT(DISTINCT ct.phong.maPhong)
                    FROM ChiTietPhieuDatPhong ct
                    WHERE ct.trangThai = 'Đã nhận phòng'
                      AND ct.thoiGianTraPhong >= :thoiDiem
                    """, Long.class)
                    .setParameter("thoiDiem", thoiDiem)
                    .getSingleResult();
            return result == null ? 0L : result;
        }
    }

    @Override
    public long demKhachDangLuuTru(LocalDateTime thoiDiem) {
        try (EntityManager em = JPAUtil.getEntityManager()) {
            Long result = em.createQuery("""
                    SELECT SUM(ct.soNguoi)
                    FROM ChiTietPhieuDatPhong ct
                    WHERE ct.trangThai = 'Đã nhận phòng'
                      AND ct.thoiGianTraPhong >= :thoiDiem
                    """, Long.class)
                    .setParameter("thoiDiem", thoiDiem)
                    .getSingleResult();
            return result == null ? 0L : result;
        }
    }

    @Override
    public long demDatPhongTrongKhoang(LocalDate tuNgay, LocalDate denNgay) {
        try (EntityManager em = JPAUtil.getEntityManager()) {
            Long result = em.createQuery("""
                    SELECT COUNT(pdp)
                    FROM PhieuDatPhong pdp
                    WHERE pdp.ngayTao BETWEEN :tuNgay AND :denNgay
                    """, Long.class)
                    .setParameter("tuNgay", tuNgay)
                    .setParameter("denNgay", denNgay)
                    .getSingleResult();
            return result == null ? 0L : result;
        }
    }

    @Override
    public long demHuyPhongTrongKhoang(LocalDate tuNgay, LocalDate denNgay) {
        try (EntityManager em = JPAUtil.getEntityManager()) {
            Long result = em.createQuery("""
                    SELECT COUNT(DISTINCT ct.phieuDatPhong.maPhieuDatPhong)
                    FROM PhieuHuyPhong php
                    JOIN php.chiTietPhieuDatPhong ct
                    WHERE php.ngayHuy >= :tuNgay
                      AND php.ngayHuy < :denNgay
                    """, Long.class)
                    .setParameter("tuNgay", tuNgay.atStartOfDay())
                    .setParameter("denNgay", denNgay.plusDays(1).atStartOfDay())
                    .getSingleResult();
            return result == null ? 0L : result;
        }
    }

    @Override
    public double tinhDoanhThuTrongKhoang(LocalDateTime tuNgay, LocalDateTime denNgay) {
        try (EntityManager em = JPAUtil.getEntityManager()) {
            Double result = em.createQuery("""
                    SELECT COALESCE(SUM(hd.tongTien), 0)
                    FROM HoaDon hd
                    WHERE COALESCE(hd.ngayTao, hd.ngayDat) >= :tuNgay
                      AND COALESCE(hd.ngayTao, hd.ngayDat) < :denNgay
                      AND hd.trangThai = 'Đã thanh toán'
                    """, Double.class)
                    .setParameter("tuNgay", tuNgay)
                    .setParameter("denNgay", denNgay)
                    .getSingleResult();
            return result == null ? 0d : result;
        }
    }

    @Override
    public List<ThongKeDTO.DuLieuCotDTO> thongKeDatPhongTheo12ThangGanNhat(LocalDate denNgay) {
        YearMonth current = YearMonth.from(denNgay);
        YearMonth start = current.minusMonths(11);
        LocalDate tuNgay = start.atDay(1);

        Map<YearMonth, Double> data = new LinkedHashMap<>();
        YearMonth cursor = start;
        while (!cursor.isAfter(current)) {
            data.put(cursor, 0d);
            cursor = cursor.plusMonths(1);
        }

        try (EntityManager em = JPAUtil.getEntityManager()) {
            List<Object[]> rows = em.createQuery("""
                    SELECT FUNCTION('year', pdp.ngayTao),
                           FUNCTION('month', pdp.ngayTao),
                           COUNT(pdp)
                    FROM PhieuDatPhong pdp
                    WHERE pdp.ngayTao BETWEEN :tuNgay AND :denNgay
                    GROUP BY FUNCTION('year', pdp.ngayTao), FUNCTION('month', pdp.ngayTao)
                    ORDER BY FUNCTION('year', pdp.ngayTao), FUNCTION('month', pdp.ngayTao)
                    """, Object[].class)
                    .setParameter("tuNgay", tuNgay)
                    .setParameter("denNgay", denNgay)
                    .getResultList();

            for (Object[] row : rows) {
                YearMonth ym = YearMonth.of(((Number) row[0]).intValue(), ((Number) row[1]).intValue());
                data.put(ym, ((Number) row[2]).doubleValue());
            }
        }

        List<ThongKeDTO.DuLieuCotDTO> result = new ArrayList<>();
        for (Map.Entry<YearMonth, Double> entry : data.entrySet()) {
            result.add(ThongKeDTO.DuLieuCotDTO.builder()
                    .nhan(entry.getKey().format(MONTH_LABEL_FORMAT))
                    .giaTri(entry.getValue())
                    .build());
        }
        return result;
    }

    @Override
    public List<ThongKeDTO.DuLieuNgayDTO> thongKeDoanhThuTheoNgay(LocalDate tuNgay, LocalDate denNgay) {
        Map<LocalDate, Double> data = new LinkedHashMap<>();
        for (LocalDate d = tuNgay; !d.isAfter(denNgay); d = d.plusDays(1)) {
            data.put(d, 0d);
        }

        try (EntityManager em = JPAUtil.getEntityManager()) {
            List<Object[]> rows = em.createQuery("""
                    SELECT FUNCTION('year', COALESCE(hd.ngayTao, hd.ngayDat)),
                           FUNCTION('month', COALESCE(hd.ngayTao, hd.ngayDat)),
                           FUNCTION('day', COALESCE(hd.ngayTao, hd.ngayDat)),
                           COALESCE(SUM(hd.tongTien), 0)
                    FROM HoaDon hd
                    WHERE COALESCE(hd.ngayTao, hd.ngayDat) >= :tuNgay
                      AND COALESCE(hd.ngayTao, hd.ngayDat) < :denNgay
                      AND hd.trangThai = 'Đã thanh toán'
                    GROUP BY FUNCTION('year', COALESCE(hd.ngayTao, hd.ngayDat)),
                             FUNCTION('month', COALESCE(hd.ngayTao, hd.ngayDat)),
                             FUNCTION('day', COALESCE(hd.ngayTao, hd.ngayDat))
                    ORDER BY FUNCTION('year', COALESCE(hd.ngayTao, hd.ngayDat)),
                             FUNCTION('month', COALESCE(hd.ngayTao, hd.ngayDat)),
                             FUNCTION('day', COALESCE(hd.ngayTao, hd.ngayDat))
                    """, Object[].class)
                    .setParameter("tuNgay", tuNgay.atStartOfDay())
                    .setParameter("denNgay", denNgay.plusDays(1).atStartOfDay())
                    .getResultList();

            for (Object[] row : rows) {
                LocalDate ngay = LocalDate.of(
                        ((Number) row[0]).intValue(),
                        ((Number) row[1]).intValue(),
                        ((Number) row[2]).intValue()
                );
                data.put(ngay, ((Number) row[3]).doubleValue());
            }
        }

        List<ThongKeDTO.DuLieuNgayDTO> result = new ArrayList<>();
        for (Map.Entry<LocalDate, Double> entry : data.entrySet()) {
            result.add(ThongKeDTO.DuLieuNgayDTO.builder()
                    .ngay(entry.getKey())
                    .giaTri(entry.getValue())
                    .build());
        }
        return result;
    }

    @Override
    public List<ThongKeDTO.DuLieuTyTrongDTO> thongKeTrangThaiDatPhong(LocalDate tuNgay, LocalDate denNgay) {
        List<ThongKeDTO.DuLieuTyTrongDTO> result = new ArrayList<>();
        try (EntityManager em = JPAUtil.getEntityManager()) {
            List<Object[]> rows = em.createQuery("""
                    SELECT pdp.trangThai, COUNT(pdp)
                    FROM PhieuDatPhong pdp
                    WHERE pdp.ngayTao BETWEEN :tuNgay AND :denNgay
                    GROUP BY pdp.trangThai
                    ORDER BY COUNT(pdp) DESC
                    """, Object[].class)
                    .setParameter("tuNgay", tuNgay)
                    .setParameter("denNgay", denNgay)
                    .getResultList();

            for (Object[] row : rows) {
                result.add(ThongKeDTO.DuLieuTyTrongDTO.builder()
                        .nhan(row[0] == null ? "Chưa xác định" : row[0].toString())
                        .giaTri(((Number) row[1]).doubleValue())
                        .build());
            }
        }
        return result;
    }

    @Override
    public List<ThongKeDTO.DuLieuTyTrongDTO> thongKeLoaiPhong(LocalDate tuNgay, LocalDate denNgay) {
        List<ThongKeDTO.DuLieuTyTrongDTO> result = new ArrayList<>();
        try (EntityManager em = JPAUtil.getEntityManager()) {
            List<Object[]> rows = em.createQuery("""
                    SELECT lp.tenLoaiPhong, COUNT(ct)
                    FROM ChiTietPhieuDatPhong ct
                    JOIN ct.phong p
                    JOIN p.loaiPhong lp
                    WHERE ct.phieuDatPhong.ngayTao BETWEEN :tuNgay AND :denNgay
                      AND ct.trangThai <> 'Đã hủy'
                    GROUP BY lp.tenLoaiPhong
                    ORDER BY COUNT(ct) DESC
                    """, Object[].class)
                    .setParameter("tuNgay", tuNgay)
                    .setParameter("denNgay", denNgay)
                    .getResultList();

            for (Object[] row : rows) {
                result.add(ThongKeDTO.DuLieuTyTrongDTO.builder()
                        .nhan(row[0] == null ? "Khác" : row[0].toString())
                        .giaTri(((Number) row[1]).doubleValue())
                        .build());
            }
        }
        return result;
    }
}
