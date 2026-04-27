package iuh.dao;

import iuh.entity.ChiTietPhieuDatPhong;
import iuh.entity.PhieuHuyPhong;
import iuh.entity.Phong;

import java.util.List;

public class PhieuHuyPhongDao extends AbstractGenericDaoImpl<PhieuHuyPhong, Long> {

    public PhieuHuyPhongDao() {
        super(PhieuHuyPhong.class);
    }

//    public boolean huyPhongNghiepVu(PhieuHuyPhong phieuHuy) {
//        // Dùng doInTransaction giống hệt như DatPhongDao để gom chung 1 luồng xử lý
//        return doInTransaction(em -> {
//            try {
//                // 1. Lưu phiếu hủy vào CSDL
//                em.persist(phieuHuy);
//
//                // 2. Cập nhật trạng thái Chi Tiết Phiếu Đặt -> 'Đã hủy'
//                ChiTietPhieuDatPhong ct = phieuHuy.getChiTietPhieuDatPhong();
//                ChiTietPhieuDatPhong managedCt = em.find(ChiTietPhieuDatPhong.class, ct.getId());
//                managedCt.setTrangThai("Đã hủy");
//                em.merge(managedCt);
//
//                // 3. Giải phóng Phòng -> 'Trống' để khách khác có thể đặt
//                Phong phong = managedCt.getPhong();
//                Phong managedPhong = em.find(Phong.class, phong.getMaPhong());
//                managedPhong.setTinhTrang("Trống");
//                em.merge(managedPhong);
//
//                return true;
//            } catch (Exception e) {
//                e.printStackTrace();
//                // doInTransaction sẽ tự động rollback nếu gặp lỗi
//                return false;
//            }
//        });
//    }

    // Xử lý hủy nhiều phòng cùng lúc VÀ trừ tiền cọc
    public boolean huyNhieuPhongNghiepVu(List<PhieuHuyPhong> listPhieuHuy, double tienHoan) {
        return doInTransaction(em -> {
            try {
                // 1. Lưu các phiếu hủy và giải phóng phòng (Giữ nguyên)
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
                }

                // 2. Trừ tiền cọc ở Phiếu Đặt Phòng gốc
                if (tienHoan > 0 && !listPhieuHuy.isEmpty()) {
                    iuh.entity.PhieuDatPhong pdp = listPhieuHuy.get(0).getChiTietPhieuDatPhong().getPhieuDatPhong();
                    if (pdp != null) {
                        iuh.entity.PhieuDatPhong managedPdp = em.find(iuh.entity.PhieuDatPhong.class, pdp.getMaPhieuDatPhong());
                        double cocMoi = managedPdp.getTienDatCoc() - tienHoan;
                        // Đảm bảo cọc không bị rớt xuống số âm
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
}