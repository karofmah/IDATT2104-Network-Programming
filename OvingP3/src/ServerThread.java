import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerThread extends Thread {
    Socket forbindelse;
    public ServerThread(Socket forbindelse){
        this.forbindelse=forbindelse;
    }

    @Override
    public void run() {
        try{
        /* �pner str�mmer for kommunikasjon med klientprogrammet */
        InputStreamReader leseforbindelse = new InputStreamReader(forbindelse.getInputStream());
        BufferedReader leseren = new BufferedReader(leseforbindelse);
        PrintWriter skriveren = new PrintWriter(forbindelse.getOutputStream(), true);

        /* Sender innledning til klienten */
        skriveren.println("Hei, du har kontakt med tjenersiden!");
        skriveren.println("Skriv to tall som skal adderes eller subtraheres");

        /* Mottar data fra klienten */
        String enLinje = leseren.readLine();  // mottar en linje med tekst
        System.out.println(enLinje);
        while (enLinje != null) {  // forbindelsen p� klientsiden er lukket
            System.out.println("En klient skrev: " + enLinje);
            skriveren.println("En tjener skrev: " + enLinje);  // sender svar til klienten
            enLinje = leseren.readLine();
        }

        /* Lukker forbindelsen */
        leseren.close();
        skriveren.close();
        forbindelse.close();
    }catch(IOException e){
            e.printStackTrace();
        }
    }
}


