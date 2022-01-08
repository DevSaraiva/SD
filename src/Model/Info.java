package Model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Info {

    private Map<String, List<Flight>> flightsMap;   //key is the origin; value is the list of the flights with departure from that origin
    private Map<LocalDate, Boolean> closedScheduleMap;
    private Map<String, Account> accountsMap;

    private static int idCounter = 0;

    public Info() {

        this.flightsMap = new HashMap<>();
        this.accountsMap = new HashMap<>();
        this.closedScheduleMap = new HashMap<>();

        // MÉTODOS PARA A PERSISTENCIA DE DADOS
        try {
            File accounts = new File("saves/accounts.txt");
            if (accounts.exists()) {
                loadAccounts("saves");
            } else {
                this.accountsMap = new HashMap<>();
            }

            File flights = new File("saves/flights.txt");
            if (flights.exists()) {
                loadFlights("saves");
            } else {
                this.flightsMap = new HashMap<>();
            }

            File closedSchedule = new File("saves/closedSchedules.txt");
            if (closedSchedule.exists()) {
                loadClosedSchedules("saves");
            } else {
                this.accountsMap = new HashMap<>();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // MÉTODOS DE MANIPULAR O CONJUNTO DE TODOS OS DADOS
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

    // MÉTODOS PARA MANIPULAÇÃO DE FLIGHTS (serialize & deserialize)
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

    // MÉTODOS PARA MANIPULAÇÃO DE ACCOUNTS (serialize & deserialize)
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

    // MÉTODOS PARA MANIPULAÇÃO DE CLOSEDSCHEDULE (serialize & deserialize)
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


    // Encerramento de um dia
    // posteriormente devolver boolean para o caso se o dia ja estava encerrado ou nao ???
    public void closeDay(LocalDate date) {
       this.closedScheduleMap.put(date,true);
    }

    // recebe lista com o percurso completo como uma lista de String de todas as cidades por onde passa
    // primeira é a origem a ultima é o destino
    // e um intervalo de datas
    public String bookTrip(String acountID, List<String> route,LocalDate startDate, LocalDate endDate) {
            String codReserve = null;
            LocalDate testDate = startDate;
            boolean found = false;
            LocalDate foundDate = null;
            while(!found && (testDate.isBefore(endDate) || testDate.isEqual(endDate))) {
                    if (verifyCloseDay(testDate)){
                        // se o verify der true ou seja o dia esta encerrado
                        testDate = testDate.plusDays(1);

                    } else {
                        // se o dia nao estiver encerrado
                        boolean possible = true;
                        for (int i = 0; (i < route.size() - 1) && possible; i++) {
                            String originCity = route.get(i);
                            String destinationCity = route.get(i+1);
                            possible = checkFlightDate(originCity,destinationCity,testDate); // se nao for possivel fica false e salta fora do for
                        }
                        if (possible) {
                            foundDate = testDate;
                            found = true;
                        } else {
                            testDate.plusDays(1);
                        }

                    }
            }
            if (found) {
                 codReserve = registerFlight(acountID,route,foundDate);
            }
            else  {
                 codReserve = "Não é possível efectuar a viagem no intervalo de datas indicado";
            }
            return codReserve;
    }

    public boolean verifyCloseDay (LocalDate date){
        return this.closedScheduleMap.get(date);
    }

    public boolean checkFlightDate(String originCity,String destinationCity,LocalDate date) {
        boolean r = false;
        List<Flight> destinations = this.flightsMap.get(originCity);
        Flight destination = getFlightFromList(destinations,destinationCity);
        if (destination != null) {
            // FIXME precisa de lock ?? pq se tiverem dois ao mesmo tempo a perguntar e so tiver um lugar vao os dois registar e so ha um lugar
            if (destination.seatsLeft(date) > 0) r = true;
        }
        return r;
    }

    public Flight getFlightFromList(List<Flight> flights,String destination){
        Flight res = null;
        for (Flight f : flights) {
            System.out.println("ola");
            if (f.getDestination().compareTo(destination) == 0) {
                res = f;
                break;
            }
        }
        return res;
    }

    // regista o voo quando ja sabe que é possivel nesta data
    public String registerFlight (String acountId,List<String> route, LocalDate date) {
        for (int i = 0; (i < route.size() - 1); i++) {
            String originCity = route.get(i);
            String destinationCity = route.get(i+1);
            Flight f = getFlightFromList(this.flightsMap.get(originCity),destinationCity);
            int newOcupation = f.getOccupationDate(date) + 1;
            f.setOccupationDate(date,newOcupation);
        }

        String idReservation = Integer.toString(idCounter);
        idCounter++;
        Reservation res = new Reservation(idReservation,date,route);
        Account acc = this.accountsMap.get(acountId);
        acc.addReservation(idReservation,res);
        return idReservation;
    }


}
