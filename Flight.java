
import java.io.Serializable;

public class Flight implements Serializable {

    String destino;
    int capacidade;

    public String getDestino() {
        return destino;
    }

    public int getCapacidade() {
        return capacidade;
    }

    public void setCapacidade(int capacidade) {
        this.capacidade = capacidade;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }
}
