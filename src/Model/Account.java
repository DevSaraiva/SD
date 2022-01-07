package Model;

import java.io.Serializable;
import java.util.Map;

public class Account implements Serializable {

    String userID;
    String password;
    Map<String, Reserva> reservas;

}