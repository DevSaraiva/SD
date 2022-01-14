package Server;


import java.io.IOException;
import java.net.Socket;

import Model.*;

import Model.Frame.Tag;

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

                if(frame != null){

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
                        case INSERT_FLY:
                            insertFlight(frame);
                            break;

                        case CLOSE_SERVICE:
                            closeServer();
                            break;

                        default:
                            break;
                    }
                }else {
                    this.online = false;
                }


            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    private void signup(Frame frame) throws IOException {

        String[] received = new String(frame.data).split("/");

        String username = received[0];
        String password = received[1];

        boolean created = info.createAccount(username,password,false);

        String res;
        if(created){
            res = "REGISTADO";
            this.username = username;
            this.loggedIn = true;
        }else {
            res = "USER-EXISTS";
        }

        tC.send(new Frame(Tag.SIGNUP,res.getBytes()));

    }

    private void login(Frame frame) throws IOException {

        String[] received = new String(frame.data).split("/");

        String username = received[0];
        String password = received[1];

        int logged = info.verifyLogin(username,password);

        String res = switch (logged) {
            case 0 -> "PASSWORD";
            case 1 -> "USER";
            case 2 -> "ADMIN";
            case 3 -> "NOTFOUND";
            default -> null;
        };

        if(logged == 1 || logged == 2){
            this.username = username;
            this.loggedIn = true;
            this.info.increaseUsersLogged();
        }

        tC.send(new Frame(Tag.LOGIN,res.getBytes()));

        System.out.println(info.getUsersLogged());
    }


    private void logout() throws IOException {
        this.tC.close();
        this.info.decreaseUsersLogged();
        this.online = false;
        System.out.println(this.info.getUsersLogged());
    }


    private void  insertFlight(Frame frame) throws IOException {

        String[] received = new String(frame.data).split("/");

        String origin = received[0];
        String destination = received[1];
        int capcity = Integer.parseInt(received[2]);


        boolean inserted = info.insertFlight(origin,destination,capcity);

        String res = null;
        if(inserted){
            res = "INSERTED";
        }else{
            res = "UPDATED";
        }
        tC.send(new Frame(Tag.INSERT_FLY,res.getBytes()));

    }

    public void closeServer() throws IOException {

        this.info.closeServer();

        String res = "CLOSING";

        tC.send(new Frame(Tag.CLOSE_SERVICE,res.getBytes()));

    }

}
