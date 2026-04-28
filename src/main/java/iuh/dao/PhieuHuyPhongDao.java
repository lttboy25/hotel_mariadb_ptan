package iuh.dao;

import iuh.entity.ChiTietPhieuDatPhong;
import iuh.entity.PhieuDatPhong;
import iuh.entity.PhieuHuyPhong;

import java.util.List;

public class PhieuHuyPhongDao extends AbstractGenericDaoImpl<PhieuHuyPhong, Long> {

    public PhieuHuyPhongDao() {
        super(PhieuHuyPhong.class);
    }

    public boolean huyNhieuPhongNghiepVu(List<PhieuHuyPhong> listPhieuHuy, double tienHoan) {
        return doInTransaction(em -> {
            try {
                for (PhieuHuyPhong phieuHuy : listPhieuHuy) {
                    em.persist(phieuHuy);

                    ChiTietPhieuDatPhong ct = phieuHuy.getChiTietPhieuDatPhong();
                    ChiTietPhieuDatPhong managedCt = em.find(ChiTietPhieuDatPhong.class, ct.getId());
                    managedCt.setTrangThai("Đã hủy");
                    em.merge(managedCt);

                    iuh.entity.Phong phong = managedCt.getPhong();
                    iuh.entity.Phong managedPhong = em.find(iuh.entity.Phong.class, phong.getMaPhong());
                    managedPhong.setTinhTrang("Trống");
                    em.merge(managedPhong);

                    capNhatTrangThaiPhieuDatPhong(em, managedCt.getPhieuDatPhong());
                }

                if (tienHoan > 0 && !listPhieuHuy.isEmpty()) {
                    PhieuDatPhong pdp = listPhieuHuy.get(0).getChiTietPhieuDatPhong().getPhieuDatPhong();
                    if (pdp != null) {
                        PhieuDatPhong managedPdp = em.find(PhieuDatPhong.class, pdp.getMaPhieuDatPhong());
                        double cocMoi = managedPdp.getTienDatCoc() - tienHoan;
                        managedPdp.setTienDatCoc((long) Math.max(cocMoi, 0));
                        em.merge(managedPdp);
                    }
                }
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        });
    }

    private void capNhatTrangThaiPhieuDatPhong(jakarta.persistence.EntityManager em, PhieuDatPhong phieuDatPhong) {
        if (phieuDatPhong == null) {
            return;
        }

        Long tongChiTiet = em.createQuery("""
                SELECT COUNT(ct)
                FROM ChiTietPhieuDatPhong ct
                WHERE ct.phieuDatPhong.maPhieuDatPhong = :maPhieuDatPhong
                """, Long.class)
                .setParameter("maPhieuDatPhong", phieuDatPhong.getMaPhieuDatPhong())
                .getSingleResult();

        Long tongChiTietDaHuy = em.createQuery("""
                SELECT COUNT(ct)
                FROM ChiTietPhieuDatPhong ct
                WHERE ct.phieuDatPhong.maPhieuDatPhong = :maPhieuDatPhong
                  AND ct.trangThai = 'Đã hủy'
                """, Long.class)
                .setParameter("maPhieuDatPhong", phieuDatPhong.getMaPhieuDatPhong())
                .getSingleResult();

        if (tongChiTiet != null && tongChiTiet > 0
                && tongChiTietDaHuy != null
                && tongChiTiet.longValue() == tongChiTietDaHuy.longValue()) {
            PhieuDatPhong managedPdp = em.find(PhieuDatPhong.class, phieuDatPhong.getMaPhieuDatPhong());
            if (managedPdp != null) {
                managedPdp.setTrangThai("Đã hủy");
                em.merge(managedPdp);
            }
        }
    }
}
