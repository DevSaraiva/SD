package Model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class Flight implements Serializable {

    String destination;
    int capacity;
    Map<LocalDate, Integer> occupations;


//CONSTRUCTORS
    public Flight (String destination, int capacity, Map<LocalDate, Integer> occupations) {
        this.destination = destination;
        this.capacity = capacity;
        setOccupations(occupations);
    }


//GETTERS AND SETTERS
    public String getDestination() {
        return destination;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public Map<LocalDate, Integer> getOccupations () {
        Map<LocalDate, Integer> res = new HashMap<>();
        for(var entry : occupations.entrySet()) {
            res.put(entry.getKey(), entry.getValue());
        }
        return res;
    }

    public void setOccupations (Map<LocalDate, Integer> inOccupations) {
        for(var entry : inOccupations.entrySet()) {
            this.occupations.put(entry.getKey(), entry.getValue());
        }
    }
}