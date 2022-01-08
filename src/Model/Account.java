package Model;

import Exceptions.ReservationAlreadyCanceledException;
import Exceptions.ReservationNotExistException;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Account implements Serializable {

    String userID;
    String password;
    Map<String, Reservation> reservations;

    public Account (String userID, String password) {
        this.userID = userID;
        this.password = password;
        this.reservations = new HashMap<>();
    }

    public String getUserID() {
        return this.userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void addReservation (String id , Reservation r) {
        reservations.put(id,r);
    }

    public boolean cancelReservation (String codReservation) throws ReservationAlreadyCanceledException, ReservationNotExistException {
        if (!this.reservations.containsKey(codReservation)) {
            new ReservationNotExistException(codReservation);
        } else {
            Reservation r = this.reservations.get(codReservation);
            if (r.isCancel()) {
                new ReservationAlreadyCanceledException(codReservation);
            }
            else {
                r.setCancel(true);
            }
        }
    }
}