package server;
import java.net.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.*;



class users {
    String Name;
    String Surname;
    long PESEL;
    int AccNum;
    float Balance;
    
/*    users(String Name, String Surname, long PESEL, int AccNum, float Balance) {
        this.Name = Name;
        this.Surname = Surname;
        this.PESEL = PESEL;
        this.AccNum = AccNum;
        this.Balance = Balance;
    } */
}

public class Server {

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = null;
        Socket socket = null;
        BufferedReader brinp = null;
        DataOutputStream out = null;
        try {
            serverSocket = new ServerSocket(6666);
        } catch (IOException e) {
            System.out.println(
                    "Błąd przy tworzeniu gniazda serwerowego.");
            System.exit(-1);
        }
        System.out.println("Inicjalizacja gniazda zakończona...");
        System.out.println("Parametry gniazda: " + serverSocket);
        while (true) {
            try {
                System.out.println("Trwa oczekiwanie na połączenie...");
                socket = serverSocket.accept();
            } catch (IOException e) {
                System.out.println(e);
                System.exit(-1);
            }
            System.out.println("Nadeszło połączenie...");
            System.out.println("Parametry połączenia: " + socket);
            try {
                System.out.println("Inicjalizacja strumieni...");
                brinp = new BufferedReader(
                        new InputStreamReader(
                                socket.getInputStream()
                        )
                );
                out = new DataOutputStream(socket.getOutputStream());
            } catch (IOException e) {
                System.out.println("Błąd przy tworzeniu strumieni.");
                System.exit(-1);
            }
            System.out.println("Zakończona inicjalizacja strumieni...");
            System.out.println("Rozpoczęcie pętli głównej...");
            while (true) {

                String login = "Podaj swój login";
                out.writeBytes(login + "\n\r");
                out.flush();
                login = brinp.readLine();


                ArrayList<users> users = null;
                if (login == null) {
                    String accnum = "Podaj nr.konta";
                    out.writeBytes(accnum + '\n');
                    out.flush();
                    accnum = brinp.readLine();

                    String tast = "Co chcesz zrobic: 1. Sprawdzić stan konta 2. Wypłacić środki 3. Wpłacić środki  4. zrobić przelew ";
                    out.writeBytes(tast + "\n\r");
                    out.flush();
                    tast = brinp.readLine();

                    Scanner scanner = new Scanner(new File("users.txt"));
                    int line = 1;

                    users = new ArrayList<>();

                    while (scanner.hasNext()) {

                        users user = new users();

                        user.Name = scanner.next();
                        user.Surname = scanner.next();
                        user.PESEL = scanner.nextLong();
                        user.AccNum = scanner.nextInt();
                        user.Balance = scanner.nextFloat();

                        users.add(user);

/*                while(scanner.hasNext()) {

                    users user = new users();

                    user.Name = scanner.next();
                    user.Surname = scanner.next();
                    user.PESEL = scanner.nextLong();
                    user.AccNum = scanner.nextInt();
                    user.Balance = scanner.nextFloat();

                    users.add(user);
                } */


//                users user = new users();
//                user.Name = "Mateusz";
//                user.Surname = "Sierski";
//                user.PESEL = 002202200;
//                user.AccNum = 8888;
//                user.Balance = 2000;
//
//                users.add(user);


                        int AccNumb = Integer.parseInt(accnum);


                        if ("1".equals(tast)) {

                            for (int i = 0; i < users.size(); i++) {

                                if (users.get(i).AccNum == AccNumb) {
                                    System.out.println("Stan konta " + users.get(i).Balance);
                                    String money = "Stan konta " + users.get(i).Balance;
                                    out.writeBytes(money + "\n\r");
                                    out.flush();
                                    break;

                                }
                            }
                        } else if ("2".equals(tast)) {

                            String moneyT = "Ile pieniędzy wypłacić?";
                            out.writeBytes(moneyT + '\n');
                            out.flush();
                            moneyT = brinp.readLine();

                            int toWithdraw = Integer.parseInt(moneyT);

                            for (int i = 0; i < users.size(); i++) {

                                if (users.get(i).AccNum == AccNumb) {
                                    float tmp = users.get(i).Balance;
                                    tmp = tmp - toWithdraw;
                                    users.get(i).Balance = tmp;
                                    break;

                                }
                            }

                        } else if ("3".equals(tast)) {

                            String moneyF = "Ile pieniędzy wpłacić?";
                            out.writeBytes(moneyF + '\n');
                            out.flush();
                            moneyF = brinp.readLine();

                            int toDeposit = Integer.parseInt(moneyF);

                            for (int i = 0; i < users.size(); i++) {

                                if (users.get(i).AccNum == AccNumb) {
                                    float tmp = users.get(i).Balance;
                                    tmp = tmp + toDeposit;
                                    users.get(i).Balance = tmp;
                                    break;
                                }
                            }


                        } else if ("4".equals(tast)) {

                            String przelew = "Ile pieniędzy przelać?";
                            out.writeBytes(przelew + '\n');
                            out.flush();
                            przelew = brinp.readLine();
                            String komu = "Ile pieniędzy przelać (nr.konta) ?";
                            out.writeBytes(komu + '\n');
                            out.flush();
                            komu = brinp.readLine();

                            int To = Integer.parseInt(komu);
                            int transfer = Integer.parseInt(przelew);

                            for (int i = 0; i < users.size(); i++) {

                                if (users.get(i).AccNum == AccNumb) {
                                    float tmp = users.get(i).Balance;
                                    tmp = tmp - transfer;
                                    users.get(i).Balance = tmp;
                                } else if (users.get(i).AccNum == To) {
                                    float tmp = users.get(i).Balance;
                                    tmp = tmp + transfer;
                                    users.get(i).Balance = tmp;
                                }


                            }

                        }

                        FileOutputStream stream = new FileOutputStream("users.txt", false);
                        PrintWriter save = new PrintWriter(stream);

                        for (int i = 0; i < users.size(); i++) {
                            save.print(users.get(i).Name + " ");
                            save.print(users.get(i).Surname + " ");
                            save.print(users.get(i).PESEL + " ");
                            save.print(users.get(i).AccNum + " ");
                            save.println(users.get(i).Balance + " ");
                        }
                        save.close();


                        try {
                            System.out.println("Odczytano linię: " + tast);
                            if (tast == null || "quit".equals(tast)) {
                                socket.close();
                                System.out.println("Zakończenie pracy z klientem...");
                                break;
                            }
                        } catch (IOException e) {
                            System.out.println("Błąd wejścia-wyjścia: " + e);
                            break;
                        }
                    }
                } else if ("Admin".equals(login)) {
                    String insertAccN = "Witaj w Terminalu Bankiera. Podaj numer konta, które chcesz modyfikować lub dodać.";
                    out.writeBytes(insertAccN + "\n");
                    out.flush();
                    brinp.readLine();

                    BufferedReader br = new BufferedReader(new FileReader("users.txt"));
                    String st = br.readLine();
                    ArrayList<users> userList = new ArrayList<>(users);
                    while (st.length() == 0) {
                        st = br.readLine();
                    }
                    if (insertAccN.equals(st)) {
                        String accExist = "Konto istnieje.Wybierz:\n1.Aby zmienić imie. 2.Aby zmienic nazwisko. 3.Aby zmienic pesel.";
                        out.writeBytes(accExist + "\n");
                        out.flush();
                        if ("1".equals(accExist)) {
//                            String oldName = "Wprowadx stare imie:";
//                            out.writeBytes(oldName + "\n");
//                            out.flush();
                            String change = brinp.readLine();
                            Scanner fileScan = new Scanner("users.txt");
                            fileScan.hasNext();
                            String dataSplit = fileScan.next();
                            String[] result = dataSplit.split("");
                            result[3].equals(change);

                            String line = "Nowe imie: ";
                            out.writeBytes(line + "\n\r");
                            out.flush();
                            int numberOfPara = 0;
                            String save = brinp.readLine();
                            userList.get(numberOfPara).Name = save.substring(0, 1).toUpperCase()+save.substring(1).toLowerCase();

//                            Files.write(Path.of("users.txt"), (Iterable<? extends CharSequence>) users, StandardCharsets.UTF_8);
                        } else if ("2".equals(accExist)) {
                            String line = "Nowe nazwisko: ";
                            out.writeBytes(line + "\n\r");
                            out.flush();
                            int numberOfPara = 0;
                            String save = brinp.readLine();
                            userList.get(numberOfPara).Surname = save.substring(0, 1).toUpperCase()+save.substring(1).toLowerCase();
                        } else if ("3".equals(accExist)) {
                            String line = "Nowy pesel: ";
                            out.writeBytes(line + "\n\r");
                            out.flush();
                            int numberOfPara = 0;
                            String save = brinp.readLine();
                            userList.get(numberOfPara).PESEL = Long.parseLong(save);
                        }
                    } else {
                        String accNExist = "Konto nie istnieje. Czy chcesz dodać nowe? [yes/no]";
                        out.writeBytes(accNExist + "\n");
                        out.flush();
                        if ("yes".equals(accNExist)) {
                            
                        } else {
                            break;
                        }
                    }
                }
            }
    }}}
