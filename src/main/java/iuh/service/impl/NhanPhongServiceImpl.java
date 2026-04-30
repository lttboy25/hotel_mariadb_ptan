package iuh.service.impl;

import iuh.entity.ChiTietPhieuDatPhong;
import iuh.entity.PhieuDatPhong;
import iuh.entity.TinhTrangPhong;
import iuh.entity.TrangThaiChiTietPhieuDatPhong;
import iuh.entity.TrangThaiPhieuDatPhong;
import iuh.entity.TrangThaiPhong;

import java.util.List;

public class NhanPhongServiceImpl implements iuh.service.NhanPhongService {
    private ChiTietPhieuDatPhongServiceImpl chiTietPhieuDatPhongServiceImpl = new ChiTietPhieuDatPhongServiceImpl();
    private PhongServiceImpl phongServiceImpl = new PhongServiceImpl();
    private PhieuDatPhongServiceImpl phieuDatPhongServiceImpl = new PhieuDatPhongServiceImpl();

    @Override
    public List<ChiTietPhieuDatPhong> getDanhSachPhongDaDatByCCCD(String cccd) {
        return chiTietPhieuDatPhongServiceImpl.getPhongDeNhanByCCCD(cccd);
    }

    @Override
    public boolean nhanPhong(List<ChiTietPhieuDatPhong> listNhanPhong) {

        if (listNhanPhong == null || listNhanPhong.isEmpty())
            return false;

        PhieuDatPhong phieuDatPhong = listNhanPhong.get(0).getPhieuDatPhong();

        for (ChiTietPhieuDatPhong ctpdp : listNhanPhong) {
            if (!TrangThaiChiTietPhieuDatPhong.CHUA_THANH_TOAN.equals(ctpdp.getTrangThai())) {
                throw new RuntimeException(
                        "Phòng " + ctpdp.getPhong().getMaPhong() + " không ở trạng thái hợp lệ để nhận!");
            }

            // Đổi trạng thái chi tiết phiếu → Đã nhận phòng
            boolean ktraChiTietPhieu = chiTietPhieuDatPhongServiceImpl
                    .updateTrangThaiByMaPhong(ctpdp.getPhong().getMaPhong(), TrangThaiChiTietPhieuDatPhong.NHAN_PHONG);
            if (!ktraChiTietPhieu) {
                throw new RuntimeException(
                        "Lỗi cập nhật chi tiết phiếu đặt phòng: " + ctpdp.getPhong().getMaPhong());
            }

            // Đổi trạng thái phòng → Đang ở
            boolean ktraPhong = phongServiceImpl
                    .updateStatusRoom(ctpdp.getPhong().getMaPhong(), TrangThaiPhong.TOT, TinhTrangPhong.DANG_O);
            if (!ktraPhong) {
                throw new RuntimeException(
                        "Lỗi cập nhật trạng thái phòng: " + ctpdp.getPhong().getMaPhong());
            }
        }

        // Kiểm tra xem tất cả chi tiết trong phiếu đã nhận hết chưa
        // Nếu còn phòng chưa nhận (cùng phiếu nhưng chưa được chọn) thì KHÔNG cập nhật
        // phiếu
        List<ChiTietPhieuDatPhong> dsAll = chiTietPhieuDatPhongServiceImpl
                .getChiTietPhieuDatPhongByMaPDP(phieuDatPhong.getMaPhieuDatPhong());

        boolean tatCaDaNhan = true;
        for (ChiTietPhieuDatPhong ct : dsAll) {
            if (!TrangThaiChiTietPhieuDatPhong.NHAN_PHONG.equals(ct.getTrangThai())) {
                tatCaDaNhan = false;
                break;
            }
        }

        // Đổi trạng thái phiếu đặt phòng → Đã nhận phòng (chỉ khi tất cả phòng đã nhận)
        if (tatCaDaNhan) {
            phieuDatPhongServiceImpl.updateTrangThai(
                    phieuDatPhong.getMaPhieuDatPhong(), TrangThaiPhieuDatPhong.NHAN_PHONG);
        }

        return true;
    }
}
