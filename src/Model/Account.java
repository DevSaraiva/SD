package Model;

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
}