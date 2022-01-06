
import java.util.List;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Server {
    private Map<String, List<Flight>> flightsMap;
    private Map<String, Account> accountsMap;

    public Server() {
        this.flightsMap = new HashMap<>();
        this.accountsMap = new HashMap<>();

        try {
            File clients = new File("saves/clients.txt");
            if (clients.exists())
                loadFlights("saves");
            else
                this.accountsMap = new HashMap<>();

            File flights = new File("saves/flights.txt");
            if (flights.exists())
                loadFlights("saves");
            else
                this.flightsMap = new HashMap<>();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadData(String pasta) throws IOException, ClassNotFoundException {
        loadFlights(pasta);
        loadAccounts(pasta);
    }

    public void saveData(String pasta) {
        File folder = new File(pasta);
        if (!folder.exists())
            folder.mkdir();

        try {
            saveAccounts(pasta);
            saveFlights(pasta);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadFlights(String folder) throws IOException, ClassNotFoundException {
        File toRead = new File(folder + "/flights.txt");
        FileInputStream fis = new FileInputStream(toRead);
        ObjectInputStream ois = new ObjectInputStream(fis);
        this.flightsMap = (HashMap<String, List<Flight>>) ois.readObject();
        ois.close();
        fis.close();

    }

    public void loadAccounts(String folder) throws IOException, ClassNotFoundException {
        File toRead = new File(folder + "/accounts.txt");
        FileInputStream fis = new FileInputStream(toRead);
        ObjectInputStream ois = new ObjectInputStream(fis);
        this.accountsMap = (HashMap<String, Account>) ois.readObject();
        ois.close();
        fis.close();

    }

    public void saveFlights(String folder) throws IOException {
        File file = new File(folder + "/flights.txt");
        if (!file.exists())
            file.createNewFile();
        FileOutputStream fos = new FileOutputStream(file);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(this.flightsMap);
        oos.flush();
        oos.close();
        fos.close();
    }

    public void saveAccounts(String folder) throws IOException {
        File file = new File(folder + "/accounts.txt");
        if (!file.exists())
            file.createNewFile();
        FileOutputStream fos = new FileOutputStream(file);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(this.accountsMap);
        oos.flush();
        oos.close();
        fos.close();

    }

}
