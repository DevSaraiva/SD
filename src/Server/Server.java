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




    public void wakeup() {
        this.c.signal();
    }


    public static void main(String[] args) throws IOException, InterruptedException {


        Server s = new Server();

        ServerSocket serverSocket = new ServerSocket(8888);
        Info info = new Info();

        while (info.isOnline()) {

            Socket socket = serverSocket.accept();
            new Thread(new ServerConnection(new TaggedConnection(socket), info)).start();
        }

     s.l.lock();

     try{
         while(info.getUsersLogged() != 0){

             s.c.await();
         }
     }

     finally {
         s.l.unlock();
     }


        info.saveData("saves");


        System.out.println("Servidor Encerrado");

    }

}
