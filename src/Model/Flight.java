package Model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Map;

public class Flight implements Serializable {

    String destiny;
    int capacity;
    Map<LocalDate, Integer> occupations;

    public String getDestino() {
        return destiny;
    }

    public int getCapacidade() {
        return capacity;
    }
}