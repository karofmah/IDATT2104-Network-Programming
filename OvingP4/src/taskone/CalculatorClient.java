package taskone;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;


public class CalculatorClient {
    public static void main(String[] args) throws IOException {
        InetAddress address;
        DatagramSocket socket;
        DatagramPacket packet;
        socket = new DatagramSocket();

        byte[] buffer = new byte[256];
        address = InetAddress.getByName("localhost");
        packet = new DatagramPacket(buffer, buffer.length, address, 4445);
        socket.send(packet);

        packet = new DatagramPacket(buffer, buffer.length);
        socket.receive(packet);
        String received = new String(packet.getData(), 0, packet.getLength());
        System.out.println("Answer: " + received);
    }
}
