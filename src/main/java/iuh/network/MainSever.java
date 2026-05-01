package iuh.network;

import iuh.service.impl.NhanVienServiceImpl;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainSever {
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(5000);) {
            ExecutorService executorService = Executors.newFixedThreadPool(10);
            while (true) {
                Socket client = serverSocket.accept();
                executorService.execute(new ClientHanler(client, new NhanVienServiceImpl()));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
