package Server;


import java.io.IOException;

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

        String username = frame.username;
        String password = new String(frame.data);

        boolean created = info.createAccount(username,password,false);

        String res;
        if(created){
            res = "REGISTADO";
        }else {
            res = "USER-EXISTS";
        }

        tC.send(new Frame(Tag.SIGNUP,username,res.getBytes()));

    }

    private void login(Frame frame) throws IOException {

        String username = frame.username;
        String password = new String(frame.data);

        int logged = info.verifyLogin(username,password);
        String res = null;

        switch (logged){

            case 0 :
                res = "PASSWORD";
                break;
            case 1 :
                res = "USER";
                break;

            case 2 :
                res = "ADMIN";
                break;

            case 4 :
                res = "NOTFOUND";

        }

        tC.send(new Frame(Tag.LOGIN,username,res.getBytes()));

    }

    private void logout() throws IOException {
        this.tC.close();
        this.online = false;
    }

}
