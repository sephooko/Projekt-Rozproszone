package server;

import java.net.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.*;

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
            List mainList = new ArrayList<>();
            File usersFile = new File("users.txt");
            String filePath = "C:\\Users\\yello\\OneDrive\\Pulpit\\Projekt-szko-a-master\\Server\\src\\server\\users.txt";
            FileWriter fileWriter = new FileWriter(usersFile);
            BufferedReader fileReader = null;
            Scanner fileScan = new Scanner(usersFile);
            while(fileScan.hasNext()){
                Dane tmp = new Dane();
                tmp.accNum = fileScan.nextInt();
                tmp.imie = fileScan.next();
                tmp.nazwisko = fileScan.next();
                tmp.pesel = fileScan.nextLong();
                tmp.money = fileScan.nextInt();
                mainList.add(tmp);
            }
            while (true) {
                String login = "Podaj swój login";
                out.writeBytes(login + "\n\r");
                out.flush();
                login = brinp.readLine();
                try{
                    fileReader = new BufferedReader(new FileReader(filePath));
                } finally {
                if (fileReader != null) {
                    fileReader.close();
                }
            }
                if ("Admin".equals(login)) {
                    String accNum = "Witaj w Terminalu Bankiera. Podaj numer konta, które chcesz modyfikować lub dodać.";
                    out.writeBytes(accNum + "\n");
                    out.flush();

                    BufferedReader br=new BufferedReader(new FileReader(filePath));
                    String st = br.readLine();
                    while(st.length()==0 && st!=null){
                        st = br.readLine();
                    }
                    if (accNum.equals(st)){
                        String accExist = "Konto istnieje.Wybierz:\n1.Aby zmienić imie. 2.Aby zmienic nazwisko. 3.Aby zmienic pesel.";
                        out.writeBytes(accExist + "\n");
                        out.flush();
                        if("1".equals(accExist)){
                            String oldName = "Wprowadx stare imie:";
                            out.writeBytes(oldName + "\n");
                            out.flush();
                            for (int i = 0; i < mainList.size(); i++) {
                                if (mainList.get(i).equals(oldName)) {
                                    String newName = "Wprowadz nowe imie:";
                                    out.writeBytes(oldName + "\n");
                                    out.flush();
                                    mainList.set(i, newName);
                                    break;
                                }
                            }
                            Files.write(Path.of(filePath), mainList, StandardCharsets.UTF_8);
                        }
                        else if ("2".equals(accExist)){

                        }
                        else if ("3".equals(accExist)){

                        }
                    } else {
                        String accNExist = "Konto nie istnieje. Czy chcesz dodać nowe? [yes/no]";
                        out.writeBytes(accNExist + "\n");
                        out.flush();
                        if ("yes".equals(accNExist)){


                        }
                        else {
                            break;
                        }
                    }
                } else {
                String accnum = "Podaj nr.konta";
//                out.writeBytes(accnum + '\n');
//                out.flush();
//                accnum = brinp.readLine();

                String tast = "Co chcesz zrobic: 1. Sprawdzić stan konta 2. Wypłacić środki 3. Wpłacić środki  4. zrobić przelew ";
                out.writeBytes(tast + "\n\r");
                out.flush();
                tast = brinp.readLine();

                FileReader fr = new FileReader(usersFile);
                BufferedReader br = new BufferedReader(fr);
                FileWriter fstream = new FileWriter("TempFile.txt", true);
                BufferedWriter save = new BufferedWriter(fstream);
                String moneyF = "Ile pieniędzy wpłacić?";

                int tastReply = Integer.parseInt(tast);
//                tastReply = brinp.read();
//                out.flush();

                int count = tastReply;
                String s;
                String[] words;
                switch (count) {
                    case 1: 
                        while ((s=br.readLine())!=null) {
                            words=s.split(" ");
                            for (String word : words) {
                                if (word.equals(accnum)){
                                    System.out.println("Stan konta" + words[4]);
                                }
                            }
                        }
                        break;

                    case 2:
                        String moneyT = "Ile pieniędzy wypłacić?";
                        out.writeBytes(moneyT + '\n');
                        out.flush();
                        moneyT = brinp.readLine();
                        while ((s=br.readLine())!=null) {
                            words=s.split(" ");
                                if (words[3].equals(accnum)){
                                    String tmp = words[4];
                                    tmp = tmp + moneyT;
                                    words[4] = tmp;
                                }

                            for (int i = 0; i < words.length; i++) {
                                save.write(words[i] + " ");
                            }   
                            save.newLine();   
                            }
                        save.flush();
                        save.close();
                        break;

                    case 3:


                    out.writeBytes(moneyF + '\n');
                    out.flush();
                    moneyF = brinp.readLine();
                    while ((s=br.readLine())!=null) {
                        words=s.split(" ");
                            if (words[3].equals(accnum)){
                                String tmp = words[4];
                                tmp = (tmp + moneyF);
                                words[4] = tmp;
                            }
                        
                        for (int i = 0; i < words.length; i++) {
                            save.write(words[i] + " ");
                            }   
                        save.newLine();   
                        }
                        save.flush();
                        save.close();
                        break;


                    case 4:

                    String przelew = "Ile pieniędzy przelać?";
                    out.writeBytes(przelew + '\n');
                    out.flush();
                    przelew = brinp.readLine();
                    String komu = "Ile pieniędzy przelać (nr.konta) ?";
                    out.writeBytes(komu + '\n');
                    out.flush();
                    komu = brinp.readLine();
                    while ((s=br.readLine())!=null) {
                        words=s.split(" ");
                        if (words[3].equals(accnum)){
                            String tmp = words[4];
                            tmp = tmp + moneyF;
                            words[4] = tmp;
                        }
                        else if (words[3].equals(komu)) {
                            String tmp = words[4];
                            tmp = tmp + moneyF;
                            words[4] = tmp;
                        }

                    for (int i = 0; i < words.length; i++) {
                        save.write(words[i] + " ");
                        }
                    }
                    save.flush();
                    save.close();
                        break;

                    default:
                        String info = "Nie wybrano poprawnej operacji";
                        out.writeBytes(info + '\n');
                        out.flush();

                    }     

                    File old = new File("users.txt");
                    old.delete();
                    File New = new File("TempFile.txt");
                    New.renameTo(old);

                       
                }                        
                try {
                    String line = brinp.readLine();
                    System.out.println("Odczytano linię: " + line);
                    if (line == null || "quit".equals(line)) {
                        socket.close();
                        System.out.println("Zakończenie pracy z klientem...");
                        break;
                    }
                    out.writeBytes(line + "\n\r");
                    System.out.println("Wysłano linię: " + line);
                } catch (IOException e) {
                    System.out.println("Błąd wejścia-wyjścia: " + e);
                    break;
                }
            }
        }
    }
}
