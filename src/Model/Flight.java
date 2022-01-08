package Model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class Flight implements Serializable {

    String destination;
    int capacity;
    Map<LocalDate, Integer> occupations;

    public String getDestino() {
        return destination;
    }

    public int getCapacidade() {
        return capacity;
    }

    public Map<LocalDate, Integer> getOccupations () {
        Map<LocalDate, Integer> res = new HashMap<>();
        for(var entry : occupations.entrySet()) {
            res.put(entry.getKey(), entry.getValue());
        }
        return res;
    }
}