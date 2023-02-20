package taskone;

import java.io.*;
import java.net.*;
import java.util.Arrays;
import java.util.Scanner;

public class CalculatorServerThread extends Thread {
    final int PORTNR = 4445;
    DatagramSocket tjener;
    private byte[] buffer=new byte[256];
    DatagramSocket socket;
    int firstNumber;
    int secondNumber;
    String operator;

    public CalculatorServerThread() throws SocketException {
        socket=new DatagramSocket(PORTNR);
    }
    public String getNextAnswer() throws IOException {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Write first number");
        int firstNumber = Integer.parseInt(scanner.nextLine());
        this.firstNumber=firstNumber;
        System.out.println("Write '+' or '-'");
        String operator=scanner.nextLine();
        this.operator=operator;
        System.out.println("Write second number");
        int secondNumber = Integer.parseInt(scanner.nextLine());
        this.secondNumber=secondNumber;
        if(operator.equals("+")){
            return String.valueOf((firstNumber + secondNumber));
        }else{
            return String.valueOf((firstNumber -  secondNumber));
        }

    }
    @Override
    public void run() {
        DatagramPacket packet = null;

        try {
            packet = new DatagramPacket(buffer, buffer.length);
            socket.receive(packet);

        } catch (IOException e) {
            e.printStackTrace();
        }
        String dString="";

            try {
                dString = getNextAnswer();
            } catch (IOException e) {
                e.printStackTrace();
            }

            buffer = dString.getBytes();

        InetAddress address = packet.getAddress();
        int port = packet.getPort();

        String received = new String(packet.getData(), 0, packet.getLength());

        packet = new DatagramPacket(buffer, buffer.length, address, port);
        try {
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String sent = new String(packet.getData(), 0, packet.getLength());
        System.out.println( firstNumber + " " + operator + " " + secondNumber + " = "  + sent);


        socket.close();
    }
}

