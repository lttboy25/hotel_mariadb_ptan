package iuh.service;

import iuh.entity.ChiTietPhieuDatPhong;
import iuh.entity.PhieuDatPhong;

import java.util.List;

public class NhanPhongService {
    private ChiTietPhieuDatPhongService chiTietPhieuDatPhongService = new ChiTietPhieuDatPhongService();
    private PhongService phongService = new PhongService();
    private PhieuDatPhongService phieuDatPhongService = new PhieuDatPhongService();

    public List<ChiTietPhieuDatPhong> getDanhSachPhongDaDatByCCCD(String cccd) {
        return chiTietPhieuDatPhongService.getPhongDeNhanByCCCD(cccd);
    }

    public boolean nhanPhong(List<ChiTietPhieuDatPhong> listNhanPhong) {

        if (listNhanPhong == null || listNhanPhong.isEmpty()) return false;

        PhieuDatPhong phieuDatPhong = listNhanPhong.get(0).getPhieuDatPhong();

        for (ChiTietPhieuDatPhong ctpdp : listNhanPhong) {
            if (!"Chưa thanh toán".equalsIgnoreCase(ctpdp.getTrangThai())) {
                throw new RuntimeException(
                        "Phòng " + ctpdp.getPhong().getMaPhong() + " không ở trạng thái hợp lệ để nhận!"
                );
            }

            // Đổi trạng thái chi tiết phiếu → Đã nhận phòng
            boolean ktraChiTietPhieu = chiTietPhieuDatPhongService
                    .updateTrangThaiByMaPhong(ctpdp.getPhong().getMaPhong(), "Đã nhận phòng");
            if (!ktraChiTietPhieu) {
                throw new RuntimeException(
                        "Lỗi cập nhật chi tiết phiếu đặt phòng: " + ctpdp.getPhong().getMaPhong()
                );
            }

            // Đổi trạng thái phòng → Đang ở
            boolean ktraPhong = phongService
                    .updateStatusRoom(ctpdp.getPhong().getMaPhong(), "Đã đặt", "Đang ở");
            if (!ktraPhong) {
                throw new RuntimeException(
                        "Lỗi cập nhật trạng thái phòng: " + ctpdp.getPhong().getMaPhong()
                );
            }
        }

        // Kiểm tra xem tất cả chi tiết trong phiếu đã nhận hết chưa
        // Nếu còn phòng chưa nhận (cùng phiếu nhưng chưa được chọn) thì KHÔNG cập nhật phiếu
        List<ChiTietPhieuDatPhong> dsAll = chiTietPhieuDatPhongService
                .getChiTietPhieuDatPhongByMaPDP(phieuDatPhong.getMaPhieuDatPhong());

        boolean tatCaDaNhan = true;
        for (ChiTietPhieuDatPhong ct : dsAll) {
            if (!"Đã nhận phòng".equalsIgnoreCase(ct.getTrangThai())) {
                tatCaDaNhan = false;
                break;
            }
        }

        // Đổi trạng thái phiếu đặt phòng → Đã nhận phòng (chỉ khi tất cả phòng đã nhận)
        if (tatCaDaNhan) {
            phieuDatPhongService.updateTrangThai(
                    phieuDatPhong.getMaPhieuDatPhong(), "Đã nhận phòng"
            );
        }

        return true;
    }
}
