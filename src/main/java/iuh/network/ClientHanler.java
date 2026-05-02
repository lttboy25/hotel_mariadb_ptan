package iuh.network;

import iuh.dto.LoaiPhongDTO;
import iuh.dto.LoginRequest;
import iuh.dto.NhanVienDTO;
import iuh.dto.PhongDTO;
import iuh.entity.LoaiPhong;
import iuh.service.impl.LoaiPhongServiceImpl;
import iuh.service.impl.NhanVienServiceImpl;
import iuh.service.impl.PhongServiceImpl;
import lombok.RequiredArgsConstructor;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.List;

@RequiredArgsConstructor
public class ClientHanler implements Runnable{
    private final Socket socket;
    private final NhanVienServiceImpl nhanVienServiceImpl;
    private final PhongServiceImpl phongServiceImpl;
    private final LoaiPhongServiceImpl loaiPhongServiceImpl;

    @Override
    public void run() {
        try(
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                ) {
            while(true){
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
                        response = Response.builder()
                                .object(rs)
                                .build();
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
                    case CREATE_PHONG ->  {
                        PhongDTO phongMoi = (PhongDTO) request.getObject();
                        PhongDTO phongDaLuu = phongServiceImpl.createPhong(phongMoi);
                        response = Response.builder().object(phongDaLuu).build();
                    }
                    case UPDATE_PHONG ->  {
                        PhongDTO phongMoi = (PhongDTO) request.getObject();
                        PhongDTO phongDaLuu = phongServiceImpl.updatePhong(phongMoi);
                        response = Response.builder().object(phongDaLuu).build();
                    }
                    case DELETE_PHONG ->  {
                        String maPhong =  (String) request.getObject();
                        boolean rs = phongServiceImpl.deletePhong(maPhong);
                        response = Response.builder().object(rs).build();
                    }
                    case GET_ALL_NHAN_VIEN -> {
                        List<NhanVienDTO> rs = nhanVienServiceImpl.getAllNhanVien();
                        response = Response.builder().object(rs).build();
                    }
                    case SEARCH_NHAN_VIEN -> {
                        String keyword = (String) request.getObject();
                        List<NhanVienDTO> rs = nhanVienServiceImpl.searchNhanVien(keyword);
                        response = Response.builder().object(rs).build();
                    }
                    case GET_NHAN_VIEN_BY_ID -> {
                        String maNV = (String) request.getObject();
                        NhanVienDTO rs = nhanVienServiceImpl.getNhanVienDTOById(maNV);
                        response = Response.builder().object(rs).build();
                    }


                }
                out.writeObject(response);
                out.flush();
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
