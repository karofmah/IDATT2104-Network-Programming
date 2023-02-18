import java.io.*;
import java.net.*;

class Server {
    public static void main(String[] args) throws IOException {
        final int PORTNR = 1251;

        ServerSocket tjener = new ServerSocket(PORTNR);
        while (true) {
            System.out.println("Logg for tjenersiden. N� venter vi...");
            Socket forbindelse = tjener.accept();  // venter inntil noen tar kontakt

            ServerThread thread = new ServerThread(forbindelse);
            thread.start();
        }
    }
}