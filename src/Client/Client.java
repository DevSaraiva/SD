package Client;

import java.net.*;

import Model.Frame;
import Model.Frame.Tag;
import Server.TaggedConnection;
import jdk.swing.interop.SwingInterOpUtils;

import java.io.*;

import java.nio.charset.StandardCharsets;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class Client {

// ver o IOException
    public static void main(String[] args) throws IOException, InterruptedException {

        Socket s = null;
        Demultiplexer dm = null;
        try {
            s = new Socket("localhost", 8888);
            dm = new Demultiplexer(new TaggedConnection(s));
        } catch (IOException e) {
            System.out.println(Colors.ANSI_RED + "O Servidor está indisponivel!" + Colors.ANSI_RESET);
        }



        boolean admin = false;

        dm.start();

        BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));

        boolean authenticated = false;

        while (!authenticated) {

            System.out.print(Colors.ANSI_BLUE + "\n***Reserva de Voos***\n" + Colors.ANSI_RESET
                    + "\n"
                    + "1) Efectuar Registo como Utilizador.\n"
                    + "2) Efectuar Login como Utilizador/Admin.\n"
                    + "\n"
                    + "0) Sair.\n");

            int optionAuthenticated = -1;
            while (optionAuthenticated == -1) { // enquanto a opcao introduzida for invalida
                System.out.println(Colors.ANSI_WHITE + "\nInsira o valor correspondente à operação desejada: " + Colors.ANSI_RESET);
                optionAuthenticated = readOptionInt(2, stdin);
            }
            String username = null;
            String password = null;
            Frame fs = null;
            String res = null;
            String send = null;


            switch (optionAuthenticated) {
                case 1:
                    // caso de SIGNUP Utilizador
                    System.out.print(Colors.ANSI_YELLOW + "\nIntroduza o username\n" + Colors.ANSI_RESET);
                    username = stdin.readLine();
                    System.out.print(Colors.ANSI_YELLOW + "\nIntroduza o password\n" + Colors.ANSI_RESET);
                    password = stdin.readLine();


                    send = username + "/" + password;
                    fs = new Frame(Tag.SIGNUP, send.getBytes());
                    dm.send(fs);

                    res = new String(dm.receive(Tag.SIGNUP));

                    if(res.equals("REGISTADO")){
                        System.out.println(Colors.ANSI_GREEN + "Conta Criada com sucesso.\n" + Colors.ANSI_RESET);
                        authenticated = true;
                    }else{
                        System.out.println(Colors.ANSI_RED + "O username já existe.\n" + Colors.ANSI_RESET);
                    }

                    break;

                case 2:
                    // caso de login Utilizador
                    System.out.print(Colors.ANSI_YELLOW + "\nIntroduza o username\n" + Colors.ANSI_RESET);
                    username = stdin.readLine();
                    System.out.print(Colors.ANSI_YELLOW + "\nIntroduza o password\n" + Colors.ANSI_RESET);
                    password = stdin.readLine();

                    send = username + "/" + password;
                    fs = new Frame(Tag.LOGIN, send.getBytes());
                    dm.send(fs);

                    res = new String(dm.receive(Tag.LOGIN));

                    if(res.equals("USER")){
                        authenticated = true;
                        admin = false;
                    }else{
                        if(res.equals(("ADMIN"))){
                            authenticated = true;
                            admin = true;
                        }else{
                            if (res.equals("NOTFOUND")) System.out.println("Username não existe");
                            else System.out.println(Colors.ANSI_RED + "Palavra-Passe errada" + Colors.ANSI_RESET);
                        }
                    }

                    break;

            }
        }

        boolean exit = false;
        while (!exit) {
            System.out.print(Colors.ANSI_BLUE + "\n***Reserva de Voos***\n\n" + Colors.ANSI_RESET);
            int option = -1;
            if (admin) {
                System.out.println("Insira o valor correspondente à operação desejada: \n"
                        + "1) Inserir informação sobre voos , introduzindo Origem, Destino e Capacidade\n"
                        + "2) Encerramento de um dia, impedindo novas reservas e cancelamentos de reservas para esse mesmo dia\n"
                        + "3) Encerrar servidor\n"
                        + "\n"
                        + "0) Sair.\n");

                while (option == -1) { // enquanto a opcao introduzida for invalida
                    System.out.println(Colors.ANSI_YELLOW + "\nInsira o valor correspondente à operação desejada: \n" + Colors.ANSI_RESET);
                    option = readOptionInt(3, stdin);
                }

            } else { // typeUser == "user"
                System.out.println("Insira o valor correspondente à operação desejada: \n"
                        + "1) Reservar viagem, indicando o percurso completo com todas as escalas e um intervalo de datas possíveis, deixando ao \n"
                        + "serviço a escolha de uma data em que a viagem seja possível\n"
                        + "2) Cancelar reserva de uma viagem, indicando o código de reserva \n"
                        + "3) Oter lista de todas os voos existentes (lista de pares origem → destino) \n"
                        + "4) Obtenção de uma lista com todos os percursos possíveis para viajar entre uma origem e um destino, limitados a duas escalas (três voos) \n"
                        +"\n"
                        + "0) Sair.\n");

                while (option == -1) { // enquanto a opcao introduzida for invalida
                    System.out.println(Colors.ANSI_YELLOW + "\nInsira o valor correspondente à operação desejada: \n" + Colors.ANSI_RESET);
                    option = readOptionInt(4, stdin);
                }
                if (option != 0) { // se for o sair nao soma 3
                    option = option + 3; // 1 2 3 4 passa a ser opcoes 4 5 6 7
                }
            }



            Frame fs = null;
            String sendMessage;
            String receiveMessage;
            Frame fr = null; // frame received

            String send = null;
            switch (option) {

                case 1:
                    // admin-> Inserir informação sobre voos , introduzindo Origem, Destino e Capacidade
                    System.out.println(Colors.ANSI_YELLOW + "\nInsira a Origem:" + Colors.ANSI_RESET);
                    String origin = stdin.readLine();
                    System.out.println(Colors.ANSI_YELLOW + "\nInsira a Destino:" + Colors.ANSI_RESET);
                    String destination = stdin.readLine();
                    int capacity = -1;
                    while (capacity == -1) { // enquanto a opcao introduzida for invalida
                        System.out.println(Colors.ANSI_YELLOW + "\nInsira o valor correspondente à capacidade: " + Colors.ANSI_RESET);
                        capacity = readOptionInt(1000,stdin);
                    }
                    sendMessage = origin + "/" + destination + "/" + capacity;

                    dm.send(new Frame(Tag.INSERT_FLY,sendMessage.getBytes()));

                    receiveMessage = new String(dm.receive(Tag.INSERT_FLY)); // FIXME mudei aqui para ser Tag.INSERT_FLY (tava login)

                    if(receiveMessage.equals("INSERTED")){
                        System.out.println(Colors.ANSI_GREEN + "Voo adicionado com sucesso" + Colors.ANSI_RESET);
                    }else{
                        System.out.println(Colors.ANSI_GREEN + "Capacidade do voo atualizada" + Colors.ANSI_RESET);
                    }


                    break;

                case 2:
                    // admin-> Encerramento de um dia, impedindo novas reservas e cancelamentos de reservas para esse mesmo dia


                    System.out.println("Insira a data do dia a encerrar");
                    LocalDate date = readDate(stdin);
                    send = date.getDayOfMonth() + "-" + date.getMonthValue() + "-" + date.getYear();
                    fs = new Frame(Tag.CLOSE_DAY,send.getBytes());
                    dm.send(fs);

                    receiveMessage = new String(dm.receive(Tag.CLOSE_DAY));
                    if (receiveMessage.equals("ALREADY_CLOSED")) {
                        System.out.println(Colors.ANSI_RED + "O dia que indicou já se encontrava fechado." + Colors.ANSI_RESET);
                    }else {
                        System.out.println(Colors.ANSI_GREEN + "O dia " + date.toString() + " foi encerrado." + Colors.ANSI_RESET);
                    }
                    break;

                case 3:
                    // admin-> Encerrar servidor

                    dm.send(new Frame(Tag.CLOSE_SERVICE,new byte[0]));


                    receiveMessage = new String(dm.receive(Tag.CLOSE_SERVICE));

                    if(receiveMessage.equals("CLOSING")) System.out.println(Colors.ANSI_GREEN + "Servidor irá ser fechado assim que possivel" + Colors.ANSI_RESET);


                    break;

                case 4:
                    // user-> Reservar viagem
                    System.out.println(Colors.ANSI_YELLOW + "\nIntroduza o percurso completo com todas as escalas no formato Origem-Escala-...-Destino" + Colors.ANSI_RESET);
                    String route = stdin.readLine();
                    if (route.split("-").length <= 1) {
                        System.out.println(Colors.ANSI_RED + "Formato inválido." + Colors.ANSI_RESET);
                        break;
                    }
                    System.out.println(Colors.ANSI_YELLOW + "\nIntroduza agora o intervalo de datas que pretende fazer a viagem começando por indicar a data de inicio no formato D/M/A:" + Colors.ANSI_RESET);
                    LocalDate startDate = readDate(stdin);
                    System.out.println(Colors.ANSI_YELLOW + "\nIntroduza agora a data final do intervalo no formato D/M/A:" + Colors.ANSI_RESET);
                    LocalDate endDate = readDate(stdin);
                    String limitInfDate = startDate.getDayOfMonth() + "-" + startDate.getMonthValue() + "-" + startDate.getYear();
                    String limitSupDate = endDate.getDayOfMonth() + "-" + endDate.getMonthValue() + "-" + endDate.getYear();
                    send = route + "/" +  limitInfDate + "/" + limitSupDate;
                    fs = new Frame(Tag.BOOK_TRIP,send.getBytes());
                    dm.send(fs);

                    receiveMessage = new String(dm.receive(Tag.BOOK_TRIP));
                    if (receiveMessage.equals("ROUTE_NOT_POSSIBLE")) {
                        System.out.println(Colors.ANSI_RED + "O percurso que introduziu não é possivel." + Colors.ANSI_RESET);
                    }
                    else if(receiveMessage.equals("NO_POSSIBLE")) {
                        System.out.println(Colors.ANSI_RED + "Não é possível efectuar a viagem no intervalo de datas indicado." + Colors.ANSI_RESET);
                    } else {
                        String[] msg = receiveMessage.split("/");
                        System.out.println(Colors.ANSI_GREEN + "A viagem ficou reservada para dia " + msg[1] + " . O código da reserva é " + msg[0] + "." + Colors.ANSI_RESET);
                    }

                    break;

                case 5:
                    // user-> Cancelar reserva de uma viagem, indicando o código de reserva
                    System.out.println(Colors.ANSI_YELLOW + "Indique o id da reserva que pretende cancelar:" + Colors.ANSI_RESET);
                    int id = readOptionInt(1000,stdin);
                    send = Integer.toString(id);

                    fs = new Frame(Tag.CANCEL_FLIGHT,send.getBytes());
                    dm.send(fs);

                    receiveMessage = new String(dm.receive(Tag.CANCEL_FLIGHT));

                    if(receiveMessage.equals("CANCELED")){
                        System.out.println(Colors.ANSI_GREEN + "Reserva cancelada com sucesso" + Colors.ANSI_RESET);
                    }else if (receiveMessage.equals("NOT_EXIST")){
                        System.out.println(Colors.ANSI_RED + "Não possui nenhuma reserva com o id indicado" + Colors.ANSI_RESET);
                        } else {
                            System.out.println(Colors.ANSI_RED + "A Reserva já se encontra cancelada" + Colors.ANSI_RESET);
                        }

                    break;

                case 6:
                    // user -> Oter lista de todas os voos existentes (lista de pares origem → destino)

                    fs = new Frame(Tag.GET_FLIGHTS_LIST,new byte[0]);
                    dm.send(fs);

                    receiveMessage = new String(dm.receive(Tag.GET_FLIGHTS_LIST));
                    if (receiveMessage.equals("NO_FLIGHTS")) {
                        System.out.println(Colors.ANSI_RED + "Não existem voos." + Colors.ANSI_RESET);
                    } else {
                        String[] flights = receiveMessage.split("/");
                        System.out.println("\nLista de todos os voos existentes: ");
                        for (String f : flights) {
                            String[] oriDest = f.split("-");
                            System.out.println("\t" + oriDest[0] + " -> " + oriDest[1]);
                        }
                    }

                    break;

                case 7:
                   // Obtenção de uma lista com todos os percursos possíveis para viajar entre uma origem e um destino, limitados a duas escalas (três voos)
                    System.out.println("Introduza a origem");
                    String originCity = stdin.readLine();
                    System.out.println("Introduza o destino");
                    String destinationCity = stdin.readLine();
                    send = originCity + "/" + destinationCity;
                    fs = new Frame(Tag.GET_ALL_ROUTES,send.getBytes());
                    dm.send(fs);

                    receiveMessage = new String(dm.receive(Tag.GET_ALL_ROUTES));
                    if (receiveMessage.equals("ORIGIN_NOT_EXIST")) {
                        System.out.println("Não existe nenhum voo partindo da origem indicada.");
                    } else if (receiveMessage.equals("ROUTE_2SCALES_NOT_POSSIBLE")) {
                        System.out.println("Não existe nenhum percurso para viajar entre a origem e o destino indicados com menos de duas escalas (três voos).");
                        } else {
                            String[] resList = receiveMessage.split("/");
                            for (String routeP : resList) {
                                System.out.println(routeP);
                            }
                        }
                    break;

                case 0:
                    System.out.println("Até à próxima... :)");
                    fs = new Frame(Tag.LOGOUT,new byte[0]);
                    dm.send(fs);
                    exit = true;
                    break;
            }
        }
    }

    public static int readOptionInt(int opcoes, BufferedReader stdin) {
        int op = -1;

            System.out.print("Opção: ");
            try {
                String line = stdin.readLine();

                op = Integer.parseInt(line);
            } catch (NumberFormatException | IOException e) { // Não foi escrito um int
                op = -1;
            }
            if (opcoes == 1000) { // 1000 é especifico para ilimitado e  neste caso so verifica se é > 0
                if (op < 0) {
                    System.out.println("Opção Inválida!!!");
                }
            } else {
                if (op < 0 || op > opcoes) {
                    System.out.println("Opção Inválida!!!");
                    op = -1;
                }
            }

        return op;

    }

    public static LocalDate readDate(BufferedReader stdin) {
        LocalDate date = null;
        while (date == null) {
            String data = null;
            try {
                data = stdin.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }

            String[] date_parse = data.split("/");
            if (date_parse.length == 3) {
                date = verifyDate(date_parse[0],date_parse[1],date_parse[2]);
            } else {
                System.out.println("Data inserida está no formato inválido. Certifique-se que introduz a data no formato Dia/Mês/Ano.");
            }
            if (date == null) {
                System.out.println("\nInsira o dia que pretende no formato D/M/A");
            }

        }
        return date;
    }

    public static LocalDate verifyDate(String day, String month, String year) {
        int dayInt, monthInt, yearInt;
        try {
            dayInt = Integer.parseInt(day);
            monthInt = Integer.parseInt(month);
            yearInt = Integer.parseInt(year);
        } catch (NumberFormatException e){
            System.out.println("Introduziu alguns dos valores Dia/Mês/Ano têm de ser inteiros!");
            return null;
        }

        LocalDate res = null;
        try {
            res = LocalDate.of(yearInt,monthInt,dayInt);
        } catch (DateTimeException e) {
            System.out.println("A Data que inseriu não é válida.");
        }

        return res;
    }

}