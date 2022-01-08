package Model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Map;

public class Flight implements Serializable {

    String destination;
    int capacity;
    Map<LocalDate, Integer> occupations;

    public String getDestination() {
        return destination;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getOcupations (LocalDate date) {
        return occupations.get(date);
    }

    public void setOccupations (LocalDate date,int ocupation) {
        this.occupations.put(date,ocupation);
    }

    // FIXME adicionar lock depois pq se tiverem dois ao mesmo tempo podem ler 1 lugar disponivel os dois
    public int seatsLeft (LocalDate date){
        return (this.capacity - getOcupations(date));
    }
}