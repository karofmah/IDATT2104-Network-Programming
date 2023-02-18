import java.io.*;
import java.net.*;
import java.util.Scanner;

class Client {
    public void startClient()throws IOException {
        final int PORTNR = 1251;

        /* Bruker en scanner til � lese fra kommandovinduet */
        Scanner leserFraKommandovindu = new Scanner(System.in);
        System.out.print("Oppgi navnet p� maskinen der tjenerprogrammet kj�rer: ");
        String tjenermaskin = leserFraKommandovindu.nextLine();

        /* Setter opp forbindelsen til tjenerprogrammet */
        Socket forbindelse = new Socket(tjenermaskin, PORTNR);
        System.out.println("N� er forbindelsen opprettet.");

        /* �pner en forbindelse for kommunikasjon med tjenerprogrammet */
        InputStreamReader leseforbindelse = new InputStreamReader(forbindelse.getInputStream());
        BufferedReader leseren = new BufferedReader(leseforbindelse);
        PrintWriter skriveren = new PrintWriter(forbindelse.getOutputStream(), true);

        /* Leser innledning fra tjeneren og skriver den til kommandovinduet */
        String innledning1 = leseren.readLine();
        String innledning2 = leseren.readLine();
        System.out.println(innledning1 + "\n" + innledning2);

        /* Leser tekst fra kommandovinduet (brukeren) */
        System.out.println("Write first number");
        int firstNumber = Integer.parseInt(leserFraKommandovindu.nextLine());
        System.out.println("Write '+' or '-'");
        String operator=leserFraKommandovindu.nextLine();
        System.out.println(operator);
        System.out.println("Write second number");
        int secondNumber = Integer.parseInt(leserFraKommandovindu.nextLine());
        if(operator.equals("+")){
            skriveren.println(firstNumber + secondNumber);

        }else{
            skriveren.println(firstNumber -  secondNumber);
        }
            String respons = leseren.readLine();  // mottar respons fra tjeneren
            System.out.println("Fra tjenerprogrammet: " + respons);



        /* Lukker forbindelsen */
        leseren.close();
        skriveren.close();
        forbindelse.close();
    }
}