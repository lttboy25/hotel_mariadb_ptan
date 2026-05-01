package iuh.network;

import iuh.dto.LoginRequest;
import iuh.dto.NhanVienDTO;
import iuh.service.impl.NhanVienServiceImpl;
import lombok.RequiredArgsConstructor;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

@RequiredArgsConstructor
public class ClientHanler implements Runnable{
    private final Socket socket;
    private final NhanVienServiceImpl nhanVienServiceImpl;

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
                }
                out.writeObject(response);
                out.flush();
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
