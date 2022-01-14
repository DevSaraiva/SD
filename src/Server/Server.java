package Server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import Model.Info;

public class Server {

    ReentrantLock l = new ReentrantLock();
    Condition c = l.newCondition();



    public static void main(String[] args) throws IOException {

        ServerSocket serverSocket = new ServerSocket(8888);
        Info info = new Info();
        boolean closed = false;

        while (!closed) {
            Socket socket = serverSocket.accept();
            new Thread(new ServerConnection(new TaggedConnection(socket), info)).start();
        }






        info.saveData("saves");

    }

}
