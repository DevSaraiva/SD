package Client;

import java.net.*;

import Model.Frame;
import Model.Frame.Tag;
import Server.TaggedConnection;

import java.io.*;

public class Client {


    public static void main(String[] args) throws Exception {

        Socket s = new Socket("localhost", 8888);
        Demultiplexer dm = new Demultiplexer(new TaggedConnection(s));

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
                if (option != 0) { // se for o sair nao soma 2
                    option = option + 3; // 1 2 3 passa a ser opcoes 3 4 5
                }
            }



            Frame fs = null;

            switch (option) {

                case 1:
                    System.out.println("\nInsira a Origem:\n");
                    String origin = stdin.readLine();
                    System.out.println("Origem : " + origin);
                    System.out.println("\nInsira a Destino:\n");
                    String destination = stdin.readLine();
                    System.out.println("Destino : " + destination);
                    System.out.println("\nInsira a capacidade: \n");
                    int capacity = readOptionInt(500,stdin);
                    System.out.println("capacidade : " + capacity);
                    //fs = new Frame(Tag.INSERT_FLY,"")
                    // admin-> Inserir informação sobre voos , introduzindo Origem, Destino e
                    // Capacidade
                    break;

                case 2:
                    // admin-> Encerramento de um dia, impedindo novas reservas e cancelamentos de
                    // reservas para esse mesmo dia
                    break;

                case 3:
                    // admin-> Encerrar servidor
                    break;

                case 4:
                    // user-> Reservar viagem
                    break;

                case 5:
                    // user-> Cancelar reserva de uma viagem, indicando o código de reserva
                    break;

                case 6:
                    // user -> Oter lista de todas os voos existentes (lista de pares origem →
                    // destino)
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
            if (op < 0 || op > opcoes) {
                System.out.println("Opção Inválida!!!");
                op = -1;
            }
        return op;

    }

}