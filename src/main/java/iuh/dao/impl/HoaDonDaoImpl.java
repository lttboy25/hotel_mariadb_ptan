package iuh.dao.impl;

import iuh.db.JPAUtil;
import iuh.dto.HoaDonDTO;
import iuh.entity.HoaDon;
import iuh.enums.TrangThaiHoaDon;
import iuh.mapper.Mapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.time.LocalDateTime;
import java.util.List;

public class HoaDonDaoImpl extends AbstractGenericDaoImpl<HoaDon, String> implements iuh.dao.HoaDonDao {

    public HoaDonDaoImpl() {
        super(HoaDon.class);
    }

    @Override
    public String generateMaHoaDon() {
        EntityManager em = JPAUtil.getEntityManager();

        try {
            String homnay = java.time.LocalDate.now()
                    .format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd"));

            String query = """
                    SELECT h.maHoaDon FROM HoaDon h
                    WHERE h.maHoaDon LIKE :prefix
                    ORDER BY h.maHoaDon DESC
                    """;

            String maHoaDonCuoi = null;

            try {
                maHoaDonCuoi = em.createQuery(query, String.class)
                        .setParameter("prefix", "HD" + homnay + "%")
                        .setMaxResults(1)
                        .getSingleResult();
            } catch (jakarta.persistence.NoResultException e) {
            }

            int nextNumber = 1;

            if (maHoaDonCuoi != null) {
                String numberPart = maHoaDonCuoi.substring(maHoaDonCuoi.length() - 2);
                nextNumber = Integer.parseInt(numberPart) + 1;
            }

            return String.format("HD%s%02d", homnay, nextNumber);

        } catch (Exception e) {
            System.err.println("Lỗi sinh mã hóa đơn: " + e.getMessage());
            return null;
        } finally {
            em.close();
        }
    }

    @Override
    public HoaDon save(HoaDonDTO hoaDonDTO) {

        HoaDon hoaDon = Mapper.map(hoaDonDTO);
        if (hoaDon.getMaHoaDon() == null || hoaDon.getMaHoaDon().isBlank()) {
            hoaDon.setMaHoaDon(generateMaHoaDon());
        }

        return create(hoaDon);
    }

    @Override
    public double calculateRevenue(String maNhanVien, java.time.LocalDateTime start, java.time.LocalDateTime end) {
        return doInTransaction(em -> {
            Double total = em.createQuery(
                            "SELECT SUM(h.tongTien) FROM HoaDon h WHERE h.nhanVien.maNhanVien = :maNhanVien AND h.ngayTao BETWEEN :start AND :end", Double.class)
                    .setParameter("maNhanVien", maNhanVien)
                    .setParameter("start", start)
                    .setParameter("end", end)
                    .getSingleResult();
            return total != null ? total : 0.0;
        });
    }

    @Override
    public List<HoaDon> getAllHoaDon() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery("""
                            SELECT DISTINCT h FROM HoaDon h
                            LEFT JOIN FETCH h.khuyenMai
                            LEFT JOIN FETCH h.chiTietHoaDon ct
                            LEFT JOIN FETCH ct.phong p
                            LEFT JOIN FETCH p.loaiPhong
                            LEFT JOIN FETCH ct.chiTietPhieuDatPhong cdp
                            LEFT JOIN FETCH cdp.phieuDatPhong
                            ORDER BY h.ngayTao DESC
                            """, HoaDon.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public HoaDon getById(String maHoaDon) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            List<HoaDon> result = em.createQuery(
                    "SELECT h FROM HoaDon h LEFT JOIN FETCH h.chiTietHoaDon WHERE h.maHoaDon = :id",
                    HoaDon.class
            ).setParameter("id", maHoaDon).getResultList();
            return result.isEmpty() ? null : result.get(0);
        } finally {
            em.close();
        }
    }

    @Override
    public List<HoaDon> searchByKeyword(String keyword) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            String kw = "%" + keyword.toLowerCase() + "%";
            return em.createQuery("""
                            SELECT h FROM HoaDon h
                            LEFT JOIN FETCH h.khachHang kh
                            WHERE LOWER(h.maHoaDon) LIKE :kw
                               OR LOWER(kh.tenKhachHang) LIKE :kw
                            ORDER BY h.ngayTao DESC
                            """, HoaDon.class)
                    .setParameter("kw", kw)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<HoaDon> getByNgayTao(LocalDateTime tuNgay, LocalDateTime denNgay) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery("""
                            SELECT h FROM HoaDon h
                            WHERE h.ngayTao >= :tuNgay
                              AND h.ngayTao <= :denNgay
                            ORDER BY h.ngayTao DESC
                            """, HoaDon.class)
                    .setParameter("tuNgay", tuNgay)
                    .setParameter("denNgay", denNgay)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<HoaDon> getByTrangThai(TrangThaiHoaDon trangThai) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery("""
                            SELECT h FROM HoaDon h
                            WHERE h.trangThai = :trangThai
                            ORDER BY h.ngayTao DESC
                            """, HoaDon.class)
                    .setParameter("trangThai", trangThai)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<HoaDon> search(String keyword,
                               LocalDateTime tuNgay,
                               LocalDateTime denNgay,
                               TrangThaiHoaDon trangThai) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            StringBuilder jpql = new StringBuilder("""
                    SELECT DISTINCT h FROM HoaDon h
                    LEFT JOIN FETCH h.khuyenMai
                    LEFT JOIN FETCH h.khachHang
                    LEFT JOIN FETCH h.nhanVien
                    LEFT JOIN FETCH h.chiTietHoaDon ct
                    LEFT JOIN FETCH ct.phong p
                    LEFT JOIN FETCH p.loaiPhong
                    LEFT JOIN FETCH ct.chiTietPhieuDatPhong cdp
                    LEFT JOIN FETCH cdp.phieuDatPhong
                    WHERE 1=1
                    """);

            if (keyword != null && !keyword.isBlank())
                jpql.append(" AND (LOWER(h.maHoaDon) LIKE :kw OR LOWER(h.khachHang.tenKhachHang) LIKE :kw)");
            if (tuNgay != null)
                jpql.append(" AND h.ngayTao >= :tuNgay");
            if (denNgay != null)
                jpql.append(" AND h.ngayTao <= :denNgay");
            if (trangThai != null)
                jpql.append(" AND h.trangThai = :trangThai");

            jpql.append(" ORDER BY h.ngayTao DESC");

            TypedQuery<HoaDon> query = em.createQuery(jpql.toString(), HoaDon.class);

            if (keyword != null && !keyword.isBlank())
                query.setParameter("kw", "%" + keyword.toLowerCase() + "%");
            if (tuNgay != null)
                query.setParameter("tuNgay", tuNgay);
            if (denNgay != null)
                query.setParameter("denNgay", denNgay);
            if (trangThai != null)
                query.setParameter("trangThai", trangThai);

            return query.getResultList();
        } finally {
            em.close();
        }
    }

}
