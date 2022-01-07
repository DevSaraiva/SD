import java.io.*;
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

    public void loadData(String pasta) throws IOException, ClassNotFoundException {
        loadFlights(pasta);
        loadClients(pasta);
    }


    public void saveData(String pasta){
        File folder = new File(pasta);
        if(!folder.exists()) folder.mkdir();

        try {
            saveClients(pasta);
            saveFlights(pasta);
        } catch (IOException e) {
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
        File toRead = new File(folder + "/client.txt");
        FileInputStream fis = new FileInputStream(toRead);
        ObjectInputStream ois = new ObjectInputStream(fis);
        this.clientMap = (HashMap<String,Client>) ois.readObject();
        ois.close();
        fis.close();
    }


    public void saveFlights(String folder) throws IOException {
        File file = new File(folder + "/flights.txt");
        if (!file.exists()) file.createNewFile();
        FileOutputStream fos = new FileOutputStream(file);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(this.flightsMap);
        oos.flush();
        oos.close();
        fos.close();
    }


    public void saveClients(String folder) throws IOException {
        File file = new File(folder + "/clients.txt");
        if (!file.exists()) file.createNewFile();
        FileOutputStream fos=new FileOutputStream(file);
        ObjectOutputStream oos=new ObjectOutputStream(fos);
        oos.writeObject(this.clientMap);
        oos.flush();
        oos.close();
        fos.close();


    }


}