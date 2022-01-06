import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.Map;

public class Server {
    private Map<String, Flight> flightsMap;
    private Map<String, Client> clientMap;


    public Server() {
        this.flightsMap = new HashMap<>();
        this.clientMap = new HashMap<>();

        try {

            File clients = new File("saves/clients.txt");
            if (clients.exists()) loadFlights("saves");
            else this.clientMap = new HashMap<>();


            File flights = new File("saves/flights.txt");
            if (flights.exists()) loadFlights("saves");
            else this.flightsMap = new HashMap<>();

        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public void loadFlights(String folder) throws IOException, ClassNotFoundException {
        File toRead = new File(folder + "/flights.txt");
        FileInputStream fis = new FileInputStream(toRead);
        ObjectInputStream ois = new ObjectInputStream(fis);

        this.flightsMap = (HashMap<String,Flight>) ois.readObject();

        ois.close();
        fis.close();

    }

    public void loadClients(String folder) throws IOException, ClassNotFoundException {
        File toRead = new File(folder + "/flights.txt");
        FileInputStream fis = new FileInputStream(toRead);
        ObjectInputStream ois = new ObjectInputStream(fis);

        this.clientMap = (HashMap<String,Client>) ois.readObject();

        ois.close();
        fis.close();

    }




}