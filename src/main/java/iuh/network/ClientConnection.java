package iuh.network;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientConnection {
    private static ClientConnection instance;

    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    private ClientConnection() {
        try {
            socket = new Socket("localhost", 5000);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
        } catch (Exception e) {
            throw new RuntimeException("Không thể kết nối đến server", e);
        }
    }

    public static ClientConnection getInstance() {
        if (instance == null) {
            instance = new ClientConnection();
        }
        return instance;
    }

    public synchronized Response sendRequest(Request request) {
        try {
            out.writeObject(request);
            out.flush();
            Response response = (Response) in.readObject();
            if (response != null && !response.isSuccess()) {
                throw new RuntimeException(response.getMessage() != null ? response.getMessage() : "Lỗi từ server");
            }
            return response;
        } catch (Exception e) {
            if (e instanceof RuntimeException) throw (RuntimeException) e;
            throw new RuntimeException("Lỗi giao tiếp với server", e);
        }
    }
}