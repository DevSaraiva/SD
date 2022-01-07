package Server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import Model.Info;

public class Server {
    public static void main(String[] args) throws IOException {

        ServerSocket serverSocket = new ServerSocket(8888);
        Info info = new Info();

        while (true) {
            Socket socket = serverSocket.accept();
            new Thread(new ServerConnection(new TaggedConnection(socket), info)).start();
        }

    }

}
