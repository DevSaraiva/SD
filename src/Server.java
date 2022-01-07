
import java.util.List;
import java.io.*;
import java.net.ServerSocket;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class Server {

    ServerSocket ss;
    private Map<String, List<Flight>> flightsMap;
    private Map<LocalDate, Boolean> closedScheduleMap;
    private Map<String, Account> accountsMap;



    public Server() {

        this.flightsMap = new HashMap<>();
        this.accountsMap = new HashMap<>();
        this.closedScheduleMap = new HashMap<>();


        
        //MÉTODOS PARA A PERSISTENCIA DE DADOS
        try {
            File accounts = new File("saves/accounts.txt");
            if (accounts.exists()) {
                loadAccounts("saves");
            }
            else {
                this.accountsMap = new HashMap<>();
            }

            File flights = new File("saves/flights.txt");
            if (flights.exists()) {
                loadFlights("saves");
            }
            else {
                this.flightsMap = new HashMap<>();
            }

            File closedSchedule = new File("saves/closedSchedules.txt");
            if (closedSchedule.exists()) {
                loadClosedSchedules("saves");
            }
            else {
                this.accountsMap = new HashMap<>();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            this.ss = new ServerSocket(8888);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }



    //MÉTODOS DE MANIPULAR O CONJUNTO DE TODOS OS DADOS
    public void loadData(String pasta) throws IOException, ClassNotFoundException {
        loadFlights(pasta);
        loadAccounts(pasta);
        loadClosedSchedules(pasta);
    }

    public void saveData(String pasta) {
        File folder = new File(pasta);
        if (!folder.exists())
            folder.mkdir();

        try {
            saveAccounts(pasta);
            saveFlights(pasta);
            saveClosedSchedules(pasta);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    //MÉTODOS PARA MANIPULAÇÃO DE FLIGHTS (serialize & deserialize)
    public void loadFlights(String folder) throws IOException, ClassNotFoundException {
        File toRead = new File(folder + "/flights.txt");
        FileInputStream fis = new FileInputStream(toRead);
        ObjectInputStream ois = new ObjectInputStream(fis);
        this.flightsMap = (HashMap<String, List<Flight>>) ois.readObject();
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



    //MÉTODOS PARA MANIPULAÇÃO DE ACCOUNTS (serialize & deserialize)
    public void loadAccounts(String folder) throws IOException, ClassNotFoundException {
        File toRead = new File(folder + "/accounts.txt");
        FileInputStream fis = new FileInputStream(toRead);
        ObjectInputStream ois = new ObjectInputStream(fis);
        this.accountsMap = (HashMap<String, Account>) ois.readObject();
        ois.close();
        fis.close();
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



    //MÉTODOS PARA MANIPULAÇÃO DE CLOSEDSCHEDULE (serialize & deserialize)
    public void loadClosedSchedules(String folder) throws IOException, ClassNotFoundException {
        File toRead = new File(folder + "/closedSchedules.txt");
        FileInputStream fis = new FileInputStream(toRead);
        ObjectInputStream ois = new ObjectInputStream(fis);
        this.closedScheduleMap = (HashMap<LocalDate, Boolean>) ois.readObject();
        ois.close();
        fis.close();
    }

    public void saveClosedSchedules(String folder) throws IOException {
        File file = new File(folder + "/closedSchedules.txt");
        if (!file.exists())
            file.createNewFile();
        FileOutputStream fos = new FileOutputStream(file);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(this.closedScheduleMap);
        oos.flush();
        oos.close();
        fos.close();
    }

    public static void main(String[] args) {

        Server server = new Server();

        try {

            boolean running = true;

            while (running) {

                ClientHandler ch = new ClientHandler(server.ss.accept());
                Thread t = new Thread(ch);
                t.start();
            }

            server.ss.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
