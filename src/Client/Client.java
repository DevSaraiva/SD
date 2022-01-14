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
            System.out.println("O Servidor está indisponivel!");
        }



        boolean admin = false;

        dm.start();

        BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));

        boolean authenticated = false;

        while (!authenticated) {

            System.out.print("\n***Reserva de Voos***\n"
                    + "\n"
                    + "1) Efectuar Registo como Utilizador.\n"
                    + "2) Efectuar Login como Utilizador/Admin.\n"
                    + "\n"
                    + "0) Sair.\n");

            int optionAuthenticated = -1;
            while (optionAuthenticated == -1) { // enquanto a opcao introduzida for invalida
                System.out.println("\nInsira o valor correspondente à operação desejada: \n");
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
                    System.out.print("\nIntroduza o username\n");
                    username = stdin.readLine();
                    System.out.print("\nIntroduza o password\n");
                    password = stdin.readLine();


                    send = username + "/" + password;
                    fs = new Frame(Tag.SIGNUP, send.getBytes());
                    dm.send(fs);

                    res = new String(dm.receive(Tag.SIGNUP));

                    if(res.equals("REGISTADO")){
                        System.out.println("Conta Criada com sucesso.");
                        authenticated = true;
                    }else{
                        System.out.println("O username já existe.");
                    }

                    break;

                case 2:
                    // caso de login Utilizador
                    System.out.print("\nIntroduza o username\n");
                    username = stdin.readLine();
                    System.out.print("\nIntroduza o password\n");
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
                            else System.out.println("Palavra-Passe errada");
                        }
                    }

                    break;

            }
        }

        boolean exit = false;
        while (!exit) {
            System.out.print("\n***Reserva de Voos***\n\n");
            int option = -1;
            if (admin) {
                System.out.println("Insira o valor correspondente à operação desejada: \n"
                        + "1) Inserir informação sobre voos , introduzindo Origem, Destino e Capacidade\n"
                        + "2) Encerramento de um dia, impedindo novas reservas e cancelamentos de reservas para esse mesmo dia\n"
                        + "3) Encerrar servidor\n"
                        + "\n"
                        + "0) Sair.\n");

                while (option == -1) { // enquanto a opcao introduzida for invalida
                    System.out.println("\nInsira o valor correspondente à operação desejada: \n");
                    option = readOptionInt(3, stdin);
                }

            } else { // typeUser == "user"
                System.out.println("Insira o valor correspondente à operação desejada: \n"
                        + "1) Reservar viagem, indicando o percurso completo com todas as escalas e um intervalo de datas possíveis, deixando ao \n"
                        + "serviço a escolha de uma data em que a viagem seja possível\n"
                        + "2) Cancelar reserva de uma viagem, indicando o código de reserva \n"
                        + "3) Oter lista de todas os voos existentes (lista de pares origem → destino) \n"
                        +"\n"
                        + "0) Sair.\n");

                while (option == -1) { // enquanto a opcao introduzida for invalida
                    System.out.println("\nInsira o valor correspondente à operação desejada: \n");
                    option = readOptionInt(3, stdin);
                }
                if (option != 0) { // se for o sair nao soma 3
                    option = option + 3; // 1 2 3 passa a ser opcoes 4 5 6
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
                    System.out.println("\nInsira a Origem:");
                    String origin = stdin.readLine();
                    System.out.println("\nInsira a Destino:");
                    String destination = stdin.readLine();
                    int capacity = -1;
                    while (capacity == -1) { // enquanto a opcao introduzida for invalida
                        System.out.println("\nInsira o valor correspondente à capacidade: \n");
                        capacity = readOptionInt(1000,stdin);
                    }
                    sendMessage = origin + "/" + destination + "/" + capacity;

                    dm.send(new Frame(Tag.INSERT_FLY,sendMessage.getBytes()));

                    receiveMessage = new String(dm.receive(Tag.INSERT_FLY)); // FIXME mudei aqui para ser Tag.INSERT_FLY (tava login)

                    if(receiveMessage.equals("INSERTED")){
                        System.out.println("Voo adicionado com sucesso");
                    }else{
                        System.out.println("Capacidade do voo atualizada");
                    }


                    break;

                case 2:
                    // admin-> Encerramento de um dia, impedindo novas reservas e cancelamentos de reservas para esse mesmo dia

                    LocalDate date = readDate(stdin);
                    send = date.getDayOfMonth() + "-" + date.getMonthValue() + "-" + date.getYear();
                    fs = new Frame(Tag.CLOSE_DAY,send.getBytes());
                    dm.send(fs);

                    receiveMessage = new String(dm.receive(Tag.CLOSE_DAY));
                    if (receiveMessage.equals("ALREADY_CLOSED")) {
                        System.out.println("O dia que indicou já se encontrava fechado.");
                    }else {
                        System.out.println("O dia " + date.toString() + " foi encerrado.");
                    }
                    break;

                case 3:
                    // admin-> Encerrar servidor

                    dm.send(new Frame(Tag.CLOSE_SERVICE,new byte[0]));


                    receiveMessage = new String(dm.receive(Tag.CLOSE_SERVICE));

                    if(receiveMessage.equals("CLOSING")) System.out.println("Servidor irá ser fechado assim que possivel");


                    break;

                case 4:
                    // user-> Reservar viagem
                    System.out.println("\nIntroduza o percurso completo com todas as escalas no formato Origem-Escala-...-Destino");
                    String route = stdin.readLine();
                    if (route.split("-").length <= 1) {
                        System.out.println("Formato inválido.");
                        break;
                    }
                    System.out.println("\nIntroduza agora o intervalo de datas que pretende fazer a viagem começando por indicar a data de inicio no formato D/M/A:");
                    LocalDate startDate = readDate(stdin);
                    System.out.println("\nIntroduza agora a data final do intervalo no formato D/M/A:");
                    LocalDate endDate = readDate(stdin);
                    String limitInfDate = startDate.getDayOfMonth() + "-" + startDate.getMonthValue() + "-" + startDate.getYear();
                    String limitSupDate = endDate.getDayOfMonth() + "-" + endDate.getMonthValue() + "-" + endDate.getYear();
                    send = route + "/" +  limitInfDate + "/" + limitSupDate;
                    fs = new Frame(Tag.BOOK_TRIP,send.getBytes());
                    dm.send(fs);

                    receiveMessage = new String(dm.receive(Tag.BOOK_TRIP));
                    if (receiveMessage.equals("ROUTE_NOT_POSSIBLE")) {
                        System.out.println("O percurso que introduziu não é possivel.");
                    }
                    else if(receiveMessage.equals("NO_POSSIBLE")) {
                        System.out.println("Não é possível efectuar a viagem no intervalo de datas indicado.");
                    } else {
                        String[] msg = receiveMessage.split("/");
                        System.out.println("A viagem ficoun reservada para dia " + msg[1] + " . O código da reserva é " + msg[0] + ".");
                    }

                    break;

                case 5:
                    // user-> Cancelar reserva de uma viagem, indicando o código de reserva
                    System.out.println("Indique o id da reserva que pretende cancelar:");
                    int id = readOptionInt(1000,stdin);
                    send = Integer.toString(id);

                    fs = new Frame(Tag.CANCEL_FLIGHT,send.getBytes());
                    dm.send(fs);

                    receiveMessage = new String(dm.receive(Tag.CANCEL_FLIGHT));

                    if(receiveMessage.equals("CANCELED")){
                        System.out.println("Reserva cancelada com sucesso");
                    }else if (receiveMessage.equals("NOT_EXIST")){
                        System.out.println("Não possui nenhuma reserva com o id indicado");
                        } else {
                            System.out.println("A Reserva já se encontra cancelada");
                        }

                    break;

                case 6:
                    // user -> Oter lista de todas os voos existentes (lista de pares origem → destino)

                    fs = new Frame(Tag.GET_FLIGHTS_LIST,new byte[0]);
                    dm.send(fs);

                    receiveMessage = new String(dm.receive(Tag.GET_FLIGHTS_LIST));
                    if (receiveMessage.equals("NO_FLIGHTS")) {
                        System.out.println("Não existem voos.");
                    } else {
                        String[] flights = receiveMessage.split("/");
                        System.out.println("\nLista de todos os voos existentes: ");
                        for (String f : flights) {
                            String[] oriDest = f.split("-");
                            System.out.println("\t" + oriDest[0] + " -> " + oriDest[1]);
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