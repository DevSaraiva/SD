package Server;

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
        try {
            while (this.online) {
                Frame command = this.tC.receive();
                List<byte[]> data = new ArrayList<>(); // data a enviar no frame de resposta
                try {
                    switch (command.tag) {
                        case SIGNUP -> signup(command.data);
                        case LOGIN -> login(command.data);
                        case LOGOUT -> logout();

                    }
              
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void signup(List<byte[]> commandData) {

    }

    private void login(List<byte[]> commandData) {

    }

    private void logout() {

    }

}