import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class WebServer {
    public static void main(String[] args) throws IOException {
        final int PORTNR = 80;

        ServerSocket tjener = new ServerSocket(PORTNR);
        System.out.println("Logg for tjenersiden. N� venter vi...");
        Socket forbindelse = tjener.accept();  // venter inntil noen tar kontakt

        /* �pner str�mmer for kommunikasjon med klientprogrammet */
        InputStreamReader leseforbindelse = new InputStreamReader(forbindelse.getInputStream());
        BufferedReader leseren = new BufferedReader(leseforbindelse);
        PrintWriter skriveren = new PrintWriter(forbindelse.getOutputStream(), true);

        /* Sender innledning til klienten */
        skriveren.println("HTTP/1.0 200 OK\n" +
                "Content-Type: text/html; charset=utf-8 \n" +
                "\n" +
                "<HTML><BODY>\n" +
                "<H1> Hilsen. Du har koblet deg opp til min enkle web-tjener </h1>\n" +
                "Header fra klient er:\n" +
                "<UL>\n" +
                "<LI> ...... </LI>\n" +
                "</UL>\n" +
                "</BODY></HTML>");

        /* Mottar data fra klienten */
        String enLinje = leseren.readLine();  // mottar en linje med tekst
        System.out.println(enLinje);
        while (enLinje != null) {  // forbindelsen p� klientsiden er lukket

            skriveren.println("<LI> " + enLinje + "</LI>");  // sender svar til klienten
            enLinje = leseren.readLine();
        }

        /* Lukker forbindelsen */
        leseren.close();
        skriveren.close();
        forbindelse.close();
    }
}
