import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Client {

    public static void main(String[] args) throws Exception {

        BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));

        boolean authenticated = false;
        while(!authenticated) {
            System.out.print("\n***Reserva de Voos***\n"
                    + "\n"
                    + "Insira o valor correspondente à operação desejada: "
                    + "1) Efectuar Registo como Utilizador.\n"
                    + "2) Efectuar Login como Utilizador.\n"
                    + "3) Efectuar Login como Administrador.\n");

            String optionAuthenticated = stdin.readLine();
            switch (optionAuthenticated) {
                case "1" :
                    // caso de efectuar registo
                    break;

                case "2" :
                    // caso de login do utilizador
                    System.out.print("\nIntroduza o username\n");
                    String username = stdin.readLine();
                    System.out.print("\nIntroduza o password\n");
                    String password = stdin.readLine();
                    /*
                    if (verificaLoginUtilizador(username,password)) {
                     // meter print a dizeer que login foi feito com sucesso ????
                        optionAuthenticated = true;
                    } else {
                        System.out.println("\nO username e/ou password inseridas estão incorretas.\n" +
                                "Tente novamente\n");
                    }
                    */
                    break;

                case "3": // caso do admin
                    // caso de login
                    System.out.print("\nIntroduza o username\n");
                    username = stdin.readLine();
                    System.out.print("\nIntroduza o password\n");
                    password = stdin.readLine();
                    /*
                    if (verificaLoginAdmin(username,password)) {
                     // meter print a dizeer que login foi feito com sucesso ????
                        optionAuthenticated = true;
                    } else {
                        System.out.println("\nO username e/ou password inseridas estão incorretas.\n" +
                                "Tente novamente\n");
                    }
                    */
                    break;
            }
        }


    }

}