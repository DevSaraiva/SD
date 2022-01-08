package Model;

import java.time.LocalDate;
import java.util.List;


public class Reservation {

    String idReservation;
    LocalDate day;
    List<String> route;

    public Reservation (String idReservation,LocalDate date, List<String> route) {
        this.idReservation = idReservation;
        this.day = date;
        this.route = route;
    }

    public String getIdReservation() {
        return this.idReservation;
    }

    public void setIdReservation(String idReservation) {
        this.idReservation = idReservation;
    }

    public LocalDate getDay() {
        return this.day;
    }

    public void setDay(LocalDate day) {
        this.day = day;
    }

    public List<String> getRoute() {
        return this.route;
    }

    public void setRoute(List<String> route) {
        this.route = route;
    }

}
