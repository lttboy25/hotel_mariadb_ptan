package iuh.dao.impl;

import iuh.entity.ChiTietPhieuDatPhong;
import iuh.entity.PhieuDatPhong;
import iuh.entity.PhieuHuyPhong;
import iuh.entity.TinhTrangPhong;
import iuh.entity.TrangThaiChiTietPhieuDatPhong;
import iuh.entity.TrangThaiPhieuDatPhong;
import jakarta.persistence.EntityManager;

import java.util.List;

public class PhieuHuyPhongDaoImpl extends AbstractGenericDaoImpl<PhieuHuyPhong, Long>
        implements iuh.dao.PhieuHuyPhongDao {

    public PhieuHuyPhongDaoImpl() {
        super(PhieuHuyPhong.class);
    }

    @Override
    public boolean huyNhieuPhongNghiepVu(List<PhieuHuyPhong> listPhieuHuy, double tienHoan) {
        return doInTransaction(em -> {
            try {
                for (PhieuHuyPhong phieuHuy : listPhieuHuy) {
                    em.persist(phieuHuy);

                    ChiTietPhieuDatPhong ct = phieuHuy.getChiTietPhieuDatPhong();
                    ChiTietPhieuDatPhong managedCt = em.find(ChiTietPhieuDatPhong.class, ct.getId());
                    managedCt.setTrangThai(TrangThaiChiTietPhieuDatPhong.DA_HUY);
                    em.merge(managedCt);

                    iuh.entity.Phong phong = managedCt.getPhong();
                    iuh.entity.Phong managedPhong = em.find(iuh.entity.Phong.class, phong.getMaPhong());
                    managedPhong.setTinhTrang(TinhTrangPhong.TRONG);
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

    private void capNhatTrangThaiPhieuDatPhong(EntityManager em, PhieuDatPhong phieuDatPhong) {
        if (phieuDatPhong == null)
            return;

        PhieuDatPhong managedPdp = em.find(PhieuDatPhong.class, phieuDatPhong.getMaPhieuDatPhong());
        if (managedPdp == null)
            return;

        boolean tatCaDaHuy = managedPdp.getDsachPhieuDatPhong().stream()
                .allMatch(ct -> TrangThaiChiTietPhieuDatPhong.DA_HUY.equals(ct.getTrangThai()));

        if (tatCaDaHuy) {
            managedPdp.setTrangThai(TrangThaiPhieuDatPhong.DA_HUY);
            em.merge(managedPdp);
        }
    }
}
