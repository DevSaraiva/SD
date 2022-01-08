package Server;


import java.io.IOException;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


import Model.*;

public class ServerConnection implements Runnable {
    private final TaggedConnection tC;
    private final Info info;
    private String username;
    private boolean loggedIn;
    private boolean online;

    public ServerConnection(TaggedConnection tCG, Info info) {
        this.tC = tCG;
        this.info = info;
        this.username = null;
        this.loggedIn = false;
        this.online = true;
    }

    @Override

    public void run() {

        while (this.online) {
            Frame frame;

            try {
                frame = this.tC.receive();
                switch (frame.tag) {

                    case SIGNUP:
                        signup(frame);
                        break;
                    case LOGIN:
                        login(frame);
                        break;
                    case LOGOUT:
                        logout();
                        break;
                    default:
                        break;
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    private void signup(Frame frame) {

        String username = frame.username;
        String password = new String(frame.data);
        
        System.out.println(username);
        System.out.println(password);


    }

    private void login(Frame frame) {

        String username = frame.username;
        String password = new String(frame.data);

        System.out.println(username);
        System.out.println(password);

    }

    private void logout() {

    }

}
