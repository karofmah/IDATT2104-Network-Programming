package taskone;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;


public class CalculatorClient {
    int firstNumber;
    int secondNumber;
    String operator;
    public String getCalculation() {
        try {
            Scanner scanner = new Scanner(System.in);

            System.out.println("Write first number");
            int firstNumber = Integer.parseInt(scanner.nextLine());
            this.firstNumber = firstNumber;
            System.out.println("Write '+', '-', '*' or '/'");
            String operator = scanner.nextLine();
            this.operator = operator;
            System.out.println("Write second number");
            int secondNumber = Integer.parseInt(scanner.nextLine());
            this.secondNumber = secondNumber;
            if (operator.equals("+") || operator.equals("-") || operator.equals("*") || operator.equals("/")){
                return firstNumber + operator + secondNumber;
            }else{
                System.out.println("Please enter valid input");
            }
        }catch(NumberFormatException e){
            System.out.println("Please write an Integer");
        }
        return "";
    }

    public static void main(String[] args) throws IOException {
        InetAddress address;
        DatagramSocket socket;
        DatagramPacket packet;
        socket = new DatagramSocket();

        CalculatorClient client=new CalculatorClient();

        byte[] buffer =  client.getCalculation().getBytes();
        address = InetAddress.getByName("localhost");
        packet = new DatagramPacket(buffer, buffer.length, address, 4445);
        socket.send(packet);

        packet = new DatagramPacket(buffer, buffer.length);
        socket.receive(packet);
        String received = new String(packet.getData(), 0, packet.getLength());
        System.out.println( client.firstNumber + " " + client.operator + " " + client.secondNumber + " = "  + received);
    }
}
