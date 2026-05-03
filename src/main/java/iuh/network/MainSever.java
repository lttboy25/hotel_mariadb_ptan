package iuh.network;

import iuh.service.impl.*;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainSever {
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(5000);) {
            ExecutorService executorService = Executors.newFixedThreadPool(10);
            while (true) {
                System.out.println("Sever running...");
                Socket client = serverSocket.accept();
                executorService.execute(new ClientHanler(
                        client,
                        new NhanVienServiceImpl(),
                        new PhongServiceImpl(),
                        new LoaiPhongServiceImpl(),
                        new DatPhongServiceImpl(),
                        new KhachHangServiceImpl(),
                        new ThongKeServiceImpl(),
                        new KhuyenMaiServiceImpl(),
                        new CaLamViecNhanVienServiceImpl(),
                        new DoiPhongServiceImpl(),
                        new ChiTietPhieuDatPhongServiceImpl()
                ));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
