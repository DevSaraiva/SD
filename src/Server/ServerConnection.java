package Server;


import java.io.IOException;
import java.util.List;
import java.util.Map;

import Exceptions.ReservationAlreadyCanceledException;
import Exceptions.ReservationNotExistException;
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

                            //  INSERT_FLY, CLOSE_DAY, CLOSE_SERVICE, BOOK_TRIP, CANCEL_FLIGHT, GET_FLIGHTS_LIST;

                        case INSERT_FLY:
                            insertFLY(frame);
                            break;

                        case CLOSE_DAY:
                            break;

                        case CLOSE_SERVICE:
                            break;

                        case BOOK_TRIP:
                            break;

                        case CANCEL_FLIGHT:
                            cancelFlight(frame);
                            break;

                        case GET_FLIGHTS_LIST:
                            getFLYlist();
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
        }

        tC.send(new Frame(Tag.LOGIN,res.getBytes()));

    }


    private void logout() throws IOException {
        this.tC.close();
        this.online = false;
    }


    private void  insertFLY(Frame frame) throws IOException {

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

    private void cancelFlight(Frame frame) throws IOException {
        String idReservation = new String(frame.data);


        String send = null;
        try {
            info.cancelFlight(username,idReservation);
            send = "CANCELED";
        } catch (ReservationNotExistException e) {
            send = "NOT_EXIST";
        } catch (ReservationAlreadyCanceledException e) {
            send = "ALREADY_EXIST";
        }
        tC.send(new Frame(Tag.CANCEL_FLIGHT,send.getBytes()));
    }

    private void getFLYlist() throws IOException {
        List<Map.Entry<String,String>> flights = info.getFlightsList();
        StringBuilder sb = new StringBuilder(); // controi do tipo origem1-Destino1/origem2-Destino2/
        for (Map.Entry<String,String> entry : flights) {
            sb.append(entry.getKey()).append("-").append(entry.getValue()).append("/");
        }
        sb.deleteCharAt(sb.length()-1); // apaga a ultima barra a mais

        tC.send(new Frame(Tag.GET_FLIGHTS_LIST,sb.toString().getBytes()));
    }

}
