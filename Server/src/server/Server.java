package server;

import java.net.*;

import java.io.*;
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
            while (true) {
                File usersFile = new File("users.txt");
                String filePath = "users.txt";
                FileWriter fileWriter = null;

                String login = "Podaj swój login";
                out.writeBytes(login + "\n\r");
                out.flush();
                login = brinp.readLine();

                try{
                    fileWriter = new FileWriter(filePath);
                    fileWriter.write(login);
                } finally {
                if (fileWriter != null) {
                    fileWriter.close();
                }
            }
                if ("Admin".equals(login)) {
                    String wtd = "Witaj w panelu administacyjnym. Co chcesz zrobić?";
                    out.writeBytes(wtd + '\n');
                    out.flush();
                    brinp.readLine();

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

                    String moneyF = "Ile pieniędzy wpłacić?";
                    out.writeBytes(moneyF + '\n');
                    out.flush();
                    moneyF = brinp.readLine();
                    while ((s=br.readLine())!=null) {
                        words=s.split(" ");
                            if (words[3].equals(accnum)){
                                String tmp = words[4];
                                tmp = tmp + moneyF;
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


//                    case 4:
//
//                    String przelew = "Ile pieniędzy przelać?";
//                    out.writeBytes(przelew + '\n');
//                    out.flush();
//                    przelew = brinp.readLine();
//                    String komu = "Ile pieniędzy przelać (nr.konta) ?";
//                    out.writeBytes(komu + '\n');
//                    out.flush();
//                    komu = brinp.readLine();
//                    while ((s=br.readLine())!=null) {
//                        words=s.split(" ");
//                        if (words[3].equals(accnum)){
//                            String tmp = words[4];
//                            tmp = tmp + moneyF;
//                            words[4] = tmp;
//                        }
//                        else if (words[3].equals(komu)) {
//                            String tmp = words[4];
//                            tmp = tmp + moneyF;
//                            words[4] = tmp;
//                        }
//
//                    for (int i = 0; i < words.length; i++) {
//                        save.write(words[i] + " ");
//                        }
//                    }
//                    save.flush();
//                    save.close();
//                        break;

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
