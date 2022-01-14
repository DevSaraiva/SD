package Client;

import java.net.*;

import Model.Frame;
import Model.Frame.Tag;
import Server.TaggedConnection;

import java.io.*;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class Client {


    public static void main(String[] args)  {

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
                optionAuthenticated = readOptionInt(3, stdin);
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
            String send = null;
            switch (option) {

                case 1:
                    // admin-> Inserir informação sobre voos , introduzindo Origem, Destino e Capacidade
                    System.out.println("\nInsira a Origem:");
                    String origin = stdin.readLine();
                    System.out.println("\nInsira a Destino:");
                    String destination = stdin.readLine();
                    System.out.println("\nInsira a capacidade:");
                    int capacity = readOptionInt(1000,stdin);

                    //String send ;
                    //fs = new Frame(Tag.INSERT_FLY,"");

                    break;

                case 2:
                    // admin-> Encerramento de um dia, impedindo novas reservas e cancelamentos de reservas para esse mesmo dia
                    // FIXME posteriormente receber boolean para o caso se o dia ja estava encerrado ou nao ???

                    LocalDate date = readDate(stdin);
                    send = date.getDayOfMonth() + "-" + date.getMonthValue() + "-" + date.getYear();
                    fs = new Frame(Tag.CLOSE_DAY,send.getBytes()); // FIXME FALTA FAZER NO SERVIDOR
                    dm.send(fs);
                    break;

                case 3:
                    // admin-> Encerrar servidor
                    break;

                case 4:
                    // user-> Reservar viagem
                    // FIXME acho que devia receber se o percurso é possivel ou nao e recber tambem no caso de nao haver voos disponiveis para as datas que ele deu

                    System.out.println("\nIntroduza o percurso completo com todas as escalas no formato Origem-Escala-...-Destino");
                    String route = stdin.readLine();
                    System.out.println("\nIntroduza agora o intervalo de datas que pretende fazer a viagem começando por indicar a data de inicio:");
                    LocalDate startDate = readDate(stdin);
                    System.out.println("\nIntroduza agora a data final do intervalo:");
                    LocalDate endDate = readDate(stdin);
                    String limitInfDate = startDate.getDayOfMonth() + "-" + startDate.getMonthValue() + "-" + startDate.getYear();
                    String limitSupDate = endDate.getDayOfMonth() + "-" + endDate.getMonthValue() + "-" + endDate.getYear();
                    send = route + "/" +  limitInfDate + "/" + limitSupDate;
                    fs = new Frame(Tag.BOOK_TRIP,send.getBytes()); // FIXME FALTA FAZER NO SERVIDOR
                    dm.send(fs);

                    break;

                case 5:
                    // user-> Cancelar reserva de uma viagem, indicando o código de reserva
                    // FIXME TALVEZ RECEBER SE A RESERVA FOI EFECTUADO COM SUCESSO OU SE JA SE ENCONTRAVA CANCELADA (E TAMBEM CASO DO ID NAO EXISTIR)
                    //
                    System.out.println("Indique o id da reserva que pretende cancelar:");
                    int id = readOptionInt(1000,stdin);
                    send = Integer.toString(id);
                    fs = new Frame(Tag.CANCEL_FLIGHT,send.getBytes()); // FIXME FALTA FAZER NO SERVIDOR
                    dm.send(fs);

                    break;

                case 6:
                    // user -> Oter lista de todas os voos existentes (lista de pares origem → destino)

                    // aqui nao manda nada ver se null funciona depois
                    fs = new Frame(Tag.GET_FLIGHTS_LIST,null); // FIXME FALTA FAZER NO SERVIDOR
                    dm.send(fs);

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
            System.out.println("\nInsira o dia que pretende encerrar no formato D/M/A");

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