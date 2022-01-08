package Model;

import java.io.Serializable;
import java.util.Map;

public class Account implements Serializable {

    String userID;
    String password;
    boolean administrador;
    Map<String, Reserva> reservas;

}