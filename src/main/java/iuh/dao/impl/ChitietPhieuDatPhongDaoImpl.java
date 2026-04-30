/*
 * @ (#) ChitietPhieuDatPhongDaoImpl.java       1.0
 *
 * Copyright (c) 2026 IUH. All rights reserved
 */
package iuh.dao.impl;

import iuh.db.JPAUtil;
import iuh.entity.ChiTietPhieuDatPhong;
import iuh.entity.TrangThaiChiTietPhieuDatPhong;
import iuh.entity.TrangThaiPhieuDatPhong;
import iuh.entity.TrangThaiPhong;
import jakarta.persistence.EntityManager;

import java.time.LocalDateTime;
import java.util.List;

/*
 * @description:
 * @author: Duc Thuan
 * @date: 23/4/2026
 * @version:    1.0
 * @created:
 */
public class ChitietPhieuDatPhongDaoImpl extends AbstractGenericDaoImpl<ChiTietPhieuDatPhong, Long>
        implements iuh.dao.ChitietPhieuDatPhongDao {
    public ChitietPhieuDatPhongDaoImpl() {
        super(ChiTietPhieuDatPhong.class);
    }

    // lay phong theo ma phieu dat phong
    @Override
    public List<ChiTietPhieuDatPhong> getByMaPhieuDatPhong(String maPDP) {
        return doInTransaction(em -> em.createQuery("""
                    SELECT ct FROM ChiTietPhieuDatPhong ct
                    WHERE ct.phieuDatPhong.maPhieuDatPhong = :ma
                """, ChiTietPhieuDatPhong.class)
                .setParameter("ma", maPDP)
                .getResultList());
    }

    // kiem tra trung lich
    @Override
    public boolean isRoomAvailable(String maPhong, LocalDateTime checkIn, LocalDateTime checkOut) {
        Long count = doInTransaction(em -> em.createQuery("""
                     SELECT COUNT(ct) FROM ChiTietPhieuDatPhong ct
                     WHERE ct.phong.maPhong = :maPhong
                     AND ct.thoiGianNhanPhong < :checkOut
                     AND ct.thoiGianTraPhong > :checkIn
                     AND ct.trangThai != :cancelledStatus
                """, Long.class)
                .setParameter("maPhong", maPhong)
                .setParameter("checkIn", checkIn)
                .setParameter("checkOut", checkOut)
                .setParameter("cancelledStatus", TrangThaiChiTietPhieuDatPhong.DA_HUY)
                .getSingleResult());
        return count == 0;
    }

    @Override
    public List<ChiTietPhieuDatPhong> getAll() {
        return doInTransaction(
                em -> em.createQuery("SELECT ct FROM ChiTietPhieuDatPhong ct", ChiTietPhieuDatPhong.class)
                        .getResultList());
    }

    @Override
    public ChiTietPhieuDatPhong findChiTietPhieuDatPhongByMaPhong(String maPhong) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            String query = """
                    SELECT ctpdp
                    FROM ChiTietPhieuDatPhong ctpdp
                    WHERE ctpdp.phong.maPhong = :maPhong
                    """;

            return (doInTransaction(entityManager -> entityManager.createQuery(query, ChiTietPhieuDatPhong.class)
                    .setParameter("maPhong", maPhong)
                    .getSingleResultOrNull()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean updateStatusDetailTicketByRoomCode(String maPhong, TrangThaiChiTietPhieuDatPhong status) {
        EntityManager em = JPAUtil.getEntityManager();

        try {
            em.getTransaction().begin();

            String query = """
                            UPDATE ChiTietPhieuDatPhong ctpdp
                            SET ctpdp.trangThai = :status
                            WHERE ctpdp.phong.maPhong = :maPhong
                    """;

            int updatedRows = em.createQuery(query)
                    .setParameter("status", status)
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

    @Override
    public List<ChiTietPhieuDatPhong> getChiTietPhieuDatPhongByToPayment(TrangThaiPhieuDatPhong statusTicket,
            TrangThaiChiTietPhieuDatPhong statusDetail,
            String cccd) {
        return doInTransaction(em -> em.createQuery("""
                SELECT ctpdp FROM ChiTietPhieuDatPhong ctpdp
                WHERE ctpdp.phieuDatPhong.trangThai = :statusTicket
                       AND ctpdp.phieuDatPhong.khachHang.CCCD = :cccd
                       AND ctpdp.trangThai = :statusDetail""", ChiTietPhieuDatPhong.class)
                .setParameter("statusTicket", statusTicket)
                .setParameter("cccd", cccd)
                .setParameter("statusDetail", statusDetail)
                .getResultList());
    }

    // Tìm các phòng đang ở trạng thái 'Đã đặt' dựa trên CCCD
    @Override
    public List<ChiTietPhieuDatPhong> getPhongDeHuyByCCCD(String cccd) {
        return doInTransaction(em -> em.createQuery("""
                SELECT ct FROM ChiTietPhieuDatPhong ct
                WHERE ct.phieuDatPhong.khachHang.CCCD = :cccd
                AND ct.phieuDatPhong.trangThai = :status
                AND ct.trangThai = :cancelledStatus
                """, ChiTietPhieuDatPhong.class)

                .setParameter("status", TrangThaiPhieuDatPhong.DA_DAT)
                .setParameter("cancelledStatus", TrangThaiChiTietPhieuDatPhong.DA_HUY)
                .setParameter("cccd", cccd.trim())
                .getResultList());
    }

    @Override
    public List<ChiTietPhieuDatPhong> getPhongDeNhanByCCCD(String cccd) {
        return doInTransaction(em -> em.createQuery("""
                SELECT ct FROM ChiTietPhieuDatPhong ct
                WHERE ct.phieuDatPhong.khachHang.CCCD = :cccd
                AND ct.phieuDatPhong.trangThai = :dadat
                """, ChiTietPhieuDatPhong.class)
                .setParameter("dadat", TrangThaiChiTietPhieuDatPhong.DA_DAT)
                .setParameter("cccd", cccd.trim())
                .getResultList());
    }

    /**
     * Lấy danh sách chi tiết phiếu đặt phòng đang thuê (Chưa thanh toán)
     * theo số điện thoại khách hàng.
     */
    @Override
    public List<ChiTietPhieuDatPhong> getDangThueBySDT(String soDienThoai) {
        return doInTransaction(em -> em.createQuery("""
                SELECT ct FROM ChiTietPhieuDatPhong ct
                JOIN ct.phieuDatPhong pdp
                JOIN pdp.khachHang kh
                WHERE kh.soDienThoai = :sdt
                  AND (ct.trangThai = :chuaTT OR ct.trangThai = :nhanPhong)
                """, ChiTietPhieuDatPhong.class)
                .setParameter("chuaTT", TrangThaiChiTietPhieuDatPhong.CHUA_THANH_TOAN)
                .setParameter("nhanPhong", TrangThaiChiTietPhieuDatPhong.NHAN_PHONG)
                .setParameter("sdt", soDienThoai)
                .getResultList());
    }

    /**
     * Cập nhật thời gian trả phòng mới và số giờ lưu trú cho một chi tiết phiếu.
     */
    @Override
    public boolean updateGiaHan(Long id, LocalDateTime thoiGianTraMoi, int soGioMoi) {
        return doInTransaction(em -> {
            int rows = em.createQuery("""
                    UPDATE ChiTietPhieuDatPhong ct
                    SET ct.thoiGianTraPhong = :thoiGianTraMoi,
                        ct.soGioLuuTru     = :soGioMoi
                    WHERE ct.id = :id
                    """)
                    .setParameter("thoiGianTraMoi", thoiGianTraMoi)
                    .setParameter("soGioMoi", soGioMoi)
                    .setParameter("id", id)
                    .executeUpdate();
            return rows > 0;
        });
    }

    @Override
    public ChiTietPhieuDatPhong findById(Long id) {
        return doInTransaction(em -> em.find(ChiTietPhieuDatPhong.class, id));
    }

    @Override
    public boolean isRoomAvailableForExtension(Long chiTietId, LocalDateTime newEndTime) {
        return doInTransaction(em -> {

            String jpql = """
                        SELECT COUNT(ct)
                        FROM ChiTietPhieuDatPhong ct
                        WHERE ct.phong.maPhong = (
                            SELECT c.phong.maPhong
                            FROM ChiTietPhieuDatPhong c
                            WHERE c.id = :chiTietId
                        )
                        AND ct.id <> :chiTietId
                        AND ct.trangThai != :cancelledStatus
                        AND ct.thoiGianNhanPhong < :newEndTime
                        AND ct.thoiGianTraPhong > (
                            SELECT c.thoiGianTraPhong
                            FROM ChiTietPhieuDatPhong c
                            WHERE c.id = :chiTietId
                        )
                    """;

            Long count = em.createQuery(jpql, Long.class)
                    .setParameter("chiTietId", chiTietId)
                    .setParameter("cancelledStatus", TrangThaiChiTietPhieuDatPhong.DA_HUY)
                    .setParameter("newEndTime", newEndTime)
                    .getSingleResult();

            return count == 0;
        });
    }

    @Override
    public List<ChiTietPhieuDatPhong> searchPhongDangThue(String keyword) {
        return doInTransaction(em -> {

            String jpql = """
                        SELECT ct FROM ChiTietPhieuDatPhong ct
                        JOIN ct.phieuDatPhong pdp
                        JOIN pdp.khachHang kh
                        JOIN ct.phong p
                        WHERE (ct.trangThai = :chuaTT OR ct.trangThai = :nhanPhong)
                        AND (
                            :kw IS NULL
                            OR kh.soDienThoai LIKE :kw
                            OR p.soPhong LIKE :kw
                        )
                    """;

            return em.createQuery(jpql, ChiTietPhieuDatPhong.class)
                    .setParameter("chuaTT", TrangThaiChiTietPhieuDatPhong.CHUA_THANH_TOAN)
                    .setParameter("nhanPhong", TrangThaiChiTietPhieuDatPhong.NHAN_PHONG)
                    .setParameter("kw", keyword == null ? null : "%" + keyword + "%")
                    .getResultList();
        });
    }
}
