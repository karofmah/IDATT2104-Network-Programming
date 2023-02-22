package taskone;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class CalculatorServerThread extends Thread {
    final int PORT = 4445;
    private byte[] buffer=new byte[256];
    DatagramSocket socket;
    int firstNumber;
    int secondNumber;
    String operator;

    public CalculatorServerThread() throws SocketException {
            socket=new DatagramSocket(PORT);


    }
    public String calculateAnswer(String calculation) throws IOException {

        try {
           char[] list=calculation.toCharArray();

            for (int i = 0; i < list.length; i++) {
                if(list[i]=='+' || list[i]=='-'|| list[i]=='*' || list[i]=='/') {
                    firstNumber=Integer.parseInt(calculation.substring(0, i));
                    operator=String.valueOf(calculation.charAt(i));
                    secondNumber=Integer.parseInt(calculation.substring(i + 1));
                }
            }


            switch (operator){
                case "+":
                    return String.valueOf((firstNumber + secondNumber));

                case "-":
                    return String.valueOf((firstNumber - secondNumber));

                case "*":
                    System.out.println(secondNumber);
                    return String.valueOf((firstNumber * secondNumber));

                case "/":
                    return String.valueOf((firstNumber / secondNumber));

                default:
                    System.out.println("Please write valid input");
            }


        } catch (NumberFormatException e) {
            System.out.println("Please write an integer");
        }
        return "";
    }
    @Override
    public void run() {

        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        try {
            socket.receive(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String received = new String(packet.getData(), 0, packet.getLength());

        String answer="";

            try {
                answer = calculateAnswer(received);
            } catch (IOException e) {
                e.printStackTrace();
            }

            buffer = answer.getBytes();
        System.out.println(answer);
        InetAddress address = packet.getAddress();
        int port = packet.getPort();

        packet = new DatagramPacket(buffer, buffer.length, address, port);
        try {
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
        socket.close();
    }
}

