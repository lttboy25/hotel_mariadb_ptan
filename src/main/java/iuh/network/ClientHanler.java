package iuh.network;

import iuh.dto.*;
import iuh.entity.LoaiPhong;
import iuh.service.impl.*;
import lombok.RequiredArgsConstructor;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
public class ClientHanler implements Runnable {
    private final Socket socket;
    private final NhanVienServiceImpl nhanVienServiceImpl;
    private final PhongServiceImpl phongServiceImpl;
    private final LoaiPhongServiceImpl loaiPhongServiceImpl;
    private final DatPhongServiceImpl datPhongServiceImpl;
    private final KhachHangServiceImpl khachHangServiceImpl;
    private final ThongKeServiceImpl thongKeServiceImpl;
    private final KhuyenMaiServiceImpl khuyenMaiServiceImpl;
    private final CaLamViecNhanVienServiceImpl caLamViecNhanVienServiceImpl;
    private final ThanhToanServiceImpl thanhToanServiceImpl;
    private final PhieuHuyPhongServiceImpl phieuHuyPhongService;
    private final ChiTietPhieuDatPhongServiceImpl chiTietPhieuDatPhongService;

    @Override
    public void run() {
        try (
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
        ) {
            while (true) {
                Request request = (Request) in.readObject();
                Response response = null;

                switch (request.getCommandType()) {
                    case XAC_THUC_TAI_KHOAN -> {
                        LoginRequest loginRequest = (LoginRequest) request.getObject();
                        var rs = nhanVienServiceImpl.xacThucDangNhap(loginRequest.getMaNhanVien(), loginRequest.getMatKhau());
                        response = Response.builder().object((NhanVienDTO) rs).build();
                    }
                    case GET_ROOM_BY_ID -> {
                        String maPhong = (String) request.getObject();
                        var rs = phongServiceImpl.getRoomById(maPhong);
                        response = Response.builder().object((PhongDTO) rs).build();
                    }
                    case GET_ALL_ROOMS -> {
                        List<PhongDTO> rs = phongServiceImpl.getAllRoom();
                        response = Response.builder().object(rs).build();
                    }
                    case GET_ALL_TANG -> {
                        List<Integer> rs = phongServiceImpl.getAllTang();
                        response = Response.builder().object(rs).build();
                    }
                    case GET_ALL_LOAI_PHONG -> {
                        List<LoaiPhongDTO> rs = loaiPhongServiceImpl.getAll();
                        response = Response.builder().object(rs).build();
                    }
                    case GET_ROOMS_BY_KEYWORD -> {
                        String keyword = (String) request.getObject();
                        List<PhongDTO> ds = phongServiceImpl.getRoomByKeyword(keyword);
                        response = Response.builder().object(ds).build();
                    }
                    case CREATE_PHONG -> {
                        PhongDTO phongMoi = (PhongDTO) request.getObject();
                        PhongDTO phongDaLuu = phongServiceImpl.createPhong(phongMoi);
                        response = Response.builder().object(phongDaLuu).build();
                    }
                    case UPDATE_PHONG -> {
                        PhongDTO phongMoi = (PhongDTO) request.getObject();
                        PhongDTO phongDaLuu = phongServiceImpl.updatePhong(phongMoi);
                        response = Response.builder().object(phongDaLuu).build();
                    }
                    case DELETE_PHONG -> {
                        String maPhong = (String) request.getObject();
                        boolean rs = phongServiceImpl.deletePhong(maPhong);
                        response = Response.builder().object(rs).build();
                    }

                    // ===== ĐẶT PHÒNG =====
                    case GET_PHONG_TRONG -> {
                        LocalDateTime[] times = (LocalDateTime[]) request.getObject();
                        var ds = datPhongServiceImpl.getDsPhongTrong(times[0], times[1]);
                        response = Response.builder().object(ds).build();
                    }
                    case DAT_PHONG -> {
                        DatPhongRequestDTO dpr = (DatPhongRequestDTO) request.getObject();
                        var rs = datPhongServiceImpl.datPhong(dpr);
                        response = Response.builder().object(rs).build();
                    }
                    case TIM_KHACH_HANG_CCCD -> {
                        String cccd = (String) request.getObject();
                        var kh = khachHangServiceImpl.findByCCCD(cccd);
                        response = Response.builder().object(kh).build();
                    }
                    case THEM_KHACH_HANG -> {
                        KhachHangDTO kh = (KhachHangDTO) request.getObject();
                        boolean rs = khachHangServiceImpl.create(kh);
                        response = Response.builder().object(rs).build();
                    }
                    case CAP_NHAT_KHACH_HANG -> {
                        KhachHangDTO kh = (KhachHangDTO) request.getObject();
                        boolean rs = khachHangServiceImpl.update(kh);
                        response = Response.builder().object(rs).build();
                    }
                    case PHAT_SINH_MA_KHACH_HANG -> {
                        String code = khachHangServiceImpl.generateNextMaKH();
                        response = Response.builder().object(code).build();
                    }

                    // ===== THỐNG KÊ =====
                    case LAY_THONG_KE -> {
                        LocalDate[] dates = (LocalDate[]) request.getObject();
                        var tk = thongKeServiceImpl.layThongKe(dates[0], dates[1]);
                        response = Response.builder().object(tk).build();
                    }

                    // ===== KHUYẾN MÃI =====
                    case GET_ALL_KHUYEN_MAI -> {
                        var list = khuyenMaiServiceImpl.getAllKhuyenMai();
                        response = Response.builder().object(list).build();
                    }
                    case GET_KHUYEN_MAI_BY_ID -> {
                        String id = (String) request.getObject();
                        var km = khuyenMaiServiceImpl.getKhuyenMaiById(id).orElse(null);
                        response = Response.builder().object(km).build();
                    }
                    case GET_KHUYEN_MAI_BY_KEYWORD -> {
                        String kw = (String) request.getObject();
                        var list = khuyenMaiServiceImpl.searchKhuyenMai(kw);
                        response = Response.builder().object(list).build();
                    }
                    case CREATE_KHUYEN_MAI -> {
                        KhuyenMaiDTO dto = (KhuyenMaiDTO) request.getObject();
                        var rs = khuyenMaiServiceImpl.addKhuyenMaiAutoCode(dto);
                        response = Response.builder().object(rs).build();
                    }
                    case UPDATE_KHUYEN_MAI -> {
                        KhuyenMaiDTO dto = (KhuyenMaiDTO) request.getObject();
                        var rs = khuyenMaiServiceImpl.updateKhuyenMai(dto);
                        response = Response.builder().object(rs).build();
                    }
                    case DELETE_KHUYEN_MAI -> {
                        String id = (String) request.getObject();
                        boolean rs = khuyenMaiServiceImpl.deleteKhuyenMai(id);
                        response = Response.builder().object(rs).build();
                    }
                    case PHAT_SINH_MA_KHUYEN_MAI -> {
                        String code = khuyenMaiServiceImpl.generateNextMaKhuyenMai();
                        response = Response.builder().object(code).build();
                    }

                    // ===== CA LÀM VIỆC NHÂN VIÊN =====
                    case GET_ACTIVE_SHIFT -> {
                        String maNV = (String) request.getObject();
                        var shift = caLamViecNhanVienServiceImpl.getActiveShift(maNV);
                        response = Response.builder().object(shift).build();
                    }
                    case MO_CA -> {
                        Object[] params = (Object[]) request.getObject();
                        var shift = caLamViecNhanVienServiceImpl.openShift((String) params[0], (String) params[1], (Double) params[2]);
                        response = Response.builder().object(shift).build();
                    }
                    case KET_CA -> {
                        Object[] params = (Object[]) request.getObject();
                        var shift = caLamViecNhanVienServiceImpl.closeShift((String) params[0], (Double) params[1]);
                        response = Response.builder().object(shift).build();
                    }
                    case GET_SHIFT_HISTORY -> {
                        String maNV = (String) request.getObject();
                        var history = caLamViecNhanVienServiceImpl.getShiftHistory(maNV);
                        response = Response.builder().object(history).build();
                    }
                    case GET_DANH_SACH_DE_THANH_TOAN -> {
                        String cccd = (String) request.getObject();
                        List<ChiTietPhieuDatPhongDTO> ds = thanhToanServiceImpl.getDanhSachPhieuDatPhongDeThanhToan(cccd);
                        response = Response.builder().object(ds).build();
                    }
                    case CO_THE_THANH_TOAN -> {
                        Map<String, Object> params = (Map<String, Object>) request.getObject();
                        double tienKhachDua = (double) params.get("tienKhachDua");
                        double tongTien     = (double) params.get("tongTien");
                        response = Response.builder()
                                .object(thanhToanServiceImpl.coTheThanhToan(tienKhachDua, tongTien))
                                .build();
                    }

                    case GET_DS_PHONG_DE_HUY -> {
                        String cccd = (String) request.getObject();
                        List<ChiTietPhieuDatPhongDTO> ds = chiTietPhieuDatPhongService.getPhongDeHuyByCCCD(cccd);
                        response = Response.builder().object(ds).build();
                    }
                    case HUY_PHONG -> {
                        HuyPhongRequest huyPhongRequest = (HuyPhongRequest) request.getObject();
                        HuyPhongResultDTO result = phieuHuyPhongService.thucHienHuyNhieuPhong(huyPhongRequest);
                        response = Response.builder().object(result).build();
                    }
                }
                out.writeObject(response);
                out.flush();
            }

        } catch (Exception e) {
            System.err.println("Client handler error: " + e.getMessage());
        }
    }
}
