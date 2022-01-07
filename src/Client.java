import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Client {


    public static void main(String[] args) throws Exception {
        String typeUser = "";

        BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));

        boolean authenticated = false;
        while(!authenticated) {
            System.out.print("\n***Reserva de Voos***\n"
                    + "\n"
                    + "1) Efectuar Registo como Utilizador.\n"
                    + "2) Efectuar Login como Utilizador.\n"
                    + "3) Efectuar Login como Administrador.\n"
                    + "\n"
                    + "0) Sair.");


            int optionAuthenticated = -1;
            while (optionAuthenticated == -1) { // enquanto a opcao introduzida for invalida
                System.out.println("Insira o valor correspondente à operação desejada: \n");
                optionAuthenticated = readOptionInt(3,stdin);
            }
            switch (optionAuthenticated) {
                case 1 :
                    // caso de efectuar registo
                    break;

                case 2 :
                    // caso de login Utilizador
                    System.out.print("\nIntroduza o username\n");
                    String username = stdin.readLine();
                    System.out.print("\nIntroduza o password\n");
                    String password = stdin.readLine();
                    /*
                    if (verificaLoginUtilizador(username,password)) {
                     // meter print a dizeer que login foi feito com sucesso ????
                        optionAuthenticated = true;
                        typeUser = "user";
                    } else {
                        System.out.println("\nO username e/ou password inseridas estão incorretas.\n" +
                                "Tente novamente\n");
                    }
                    */
                    break;

                case 3:
                    // caso de login Administrador
                    System.out.print("\nIntroduza o username\n");
                    username = stdin.readLine();
                    System.out.print("\nIntroduza o password\n");
                    password = stdin.readLine();
                    /*
                    if (verificaLoginAdmin(username,password)) {
                     // meter print a dizeer que login foi feito com sucesso ????
                        optionAuthenticated = true;
                        typeUser = "admin";
                    } else {
                        System.out.println("\nO username e/ou password inseridas estão incorretas.\n" +
                                "Tente novamente\n");
                    }
                    */
                    break;
            }
        }



        boolean exit = false;
        while (!exit) {
            System.out.print("\n***Reserva de Voos***\n\n");
            int option = -1;
            if (typeUser.compareTo("admin") == 0){
                System.out.println("Insira o valor correspondente à operação desejada: "
                                    + "1) Inserir informação sobre voos , introduzindo Origem, Destino e Capacidade\n"
                                    + "2) Encerramento de um dia, impedindo novas reservas e cancelamentos de reservas para esse mesmo dia\n"
                                    + "\n"
                                    + "0) Sair.");

                while (option == -1) { // enquanto a opcao introduzida for invalida
                    System.out.println("Insira o valor correspondente à operação desejada: \n");
                    option = readOptionInt(2,stdin);
                }

            } else { //typeUser == "user"
                System.out.println("Insira o valor correspondente à operação desejada: "
                        + "1) Reservar viagem, indicando o percurso completo com todas as escalas e um intervalo de datas possíveis, deixando ao " +
                        "serviço a escolha de uma data em que a viagem seja possível\n"
                        + "2) Cancelar reserva de uma viagem, indicando o código de reserva \n"
                        + "3) Oter lista de todas os voos existentes (lista de pares origem → destino) \n"
                        + "\n"
                        + "0) Sair.");


                while (option == -1) { // enquanto a opcao introduzida for invalida
                    System.out.println("Insira o valor correspondente à operação desejada: \n");
                    option = readOptionInt(3,stdin);
                }
                option = option + 2; // passa a ser opcoes 3 4 5
            }


            switch (option) {
                case 0:
                    // exit
                    exit = true;
                    break;

                case 1:
                    // admin-> Inserir informação sobre voos , introduzindo Origem, Destino e Capacidade
                    break;

                case 2:
                    // admin-> Encerramento de um dia, impedindo novas reservas e cancelamentos de reservas para esse mesmo dia
                    break;

                case 3:
                    // user-> Reservar viagem
                    break;

                case 4:
                    // user-> Cancelar reserva de uma viagem, indicando o código de reserva
                    break;

                case 5:
                    // user -> Oter lista de todas os voos existentes (lista de pares origem → destino)
                    break;
            }
        }
    }

    public static int readOptionInt(int opcoes, BufferedReader stdin)  {
        int op = -1;

        while(op == -1){

            System.out.print("Opção: ");
            try {
                String line = stdin.readLine();
                op = Integer.parseInt(line);
            }
            catch (NumberFormatException | IOException e) { // Não foi escrito um int
                op = -1;
            }
            if (op<0 || op> opcoes) {
                System.out.println("Opção Inválida!!!");
                op = -1;
            }
        }
        return op;

    }

}