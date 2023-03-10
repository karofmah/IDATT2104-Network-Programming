
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Objects;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WebSocket {
    private static final ArrayList<ClientThread> clients = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        try (ServerSocket server = new ServerSocket(3001)) {
            System.out.println("Server has started on 127.0.0.1:3001.\r\nWaiting for a connection…");

            while (true) {
                Socket client = server.accept();
                System.out.println("A client connected.");

                ClientThread clientThread = new ClientThread(client);
                clients.add(clientThread);
                clientThread.start();
            }
        }
    }

    private static class ClientThread extends Thread {
        private final Socket client;
        private final InputStream in;
        private final OutputStream out;

        public ClientThread(Socket client) throws IOException {
            this.client = client;
            this.in = client.getInputStream();
            this.out = client.getOutputStream();
        }

        @Override
        public void run() {
            try {
                Scanner s = new Scanner(in, StandardCharsets.UTF_8);

                String data = s.useDelimiter("\\r\\n\\r\\n").next();
                Matcher get = Pattern.compile("^GET").matcher(data);

                if (get.find()) {
                    Matcher match = Pattern.compile("Sec-WebSocket-Key: (.*)").matcher(data);
                    match.find();
                    byte[] response = ("HTTP/1.1 101 Switching Protocols\r\n"
                            + "Connection: Upgrade\r\n"
                            + "Upgrade: websocket\r\n"
                            + "Sec-WebSocket-Accept: "
                            + Base64.getEncoder().encodeToString(MessageDigest.getInstance("SHA-1").digest((match.group(1) + "258EAFA5-E914-47DA-95CA-C5AB0DC85B11").getBytes("UTF-8")))
                            + "\r\n\r\n").getBytes(StandardCharsets.UTF_8);
                    out.write(response, 0, response.length);

                    // Start handling WebSocket communication
                    byte[] buffer = new byte[1024];
                    while (true) {
                        int len = in.read(buffer);
                        if (len == -1) {
                            break;
                        }
                        byte[] payload = new byte[len - 6];
                        for (int i = 6; i < len; i++) {
                            payload[i - 6] = (byte) (buffer[i] ^ buffer[i % 4]);
                        }
                        String message = decodeMessage(in);
                        System.out.println("Received message: " + message);

                        // Send the message to all clients
                        for (ClientThread clientThread : clients) {
                            clientThread.out.write(Objects.requireNonNull(encodeMessage(message)));
                        }
                    }
                }
            } catch (IOException | NoSuchAlgorithmException e) {
                e.printStackTrace();
            } finally {
                try {
                    in.close();
                    out.close();
                    client.close();
                    clients.remove(this);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private String decodeMessage(InputStream in) throws IOException {
            byte[] frameHeader = new byte[2];
            in.read(frameHeader);

            //We read the first parts of the message frame. The opcode is the message type, fin indicates if this is final fragment in a message.
            //masked indicates if the payload data is masked or not.

            byte opcode = (byte)(frameHeader[0] & 0x0F);
            boolean fin = (frameHeader[1] & 0x80) != 0;
            boolean masked = (frameHeader[1] & 0x80) != 0;
            int payloadLength = (byte)(frameHeader[1] & 0x7F);

            //Determine payload length

            if(payloadLength == 126){
                //We extract whether there is another extension of payload.
                byte[] extendedLen = new byte[2];
                in.read(extendedLen);
                payloadLength = ByteBuffer.wrap(extendedLen).getShort();
            }
            else if(payloadLength == 127){
                byte[] extendedLen = new byte[8];
                in.read(extendedLen);
                payloadLength = (int) ByteBuffer.wrap(extendedLen).getLong();
            }

            //Read mask if masked.
            byte[] mask = new byte[4];
            if(masked){
                in.read(mask);
            }

            //Read the actual payload
            byte[] payload = new byte[payloadLength];

            in.read(payload);

            //Use the mask and a XOR operation to demask the payload.
            if(masked) {
                for (int i = 0; i < payloadLength; i++){
                    payload[i] ^= mask[i % 4];
                }
            }

            return new String(payload, StandardCharsets.UTF_8);
        }


        private static byte[] encodeMessage(String message) {
            try {
                byte[] messageBytes = message.getBytes(StandardCharsets.UTF_8);
                byte[] encoded = new byte[messageBytes.length + 2];
                encoded[0] = (byte) 0x81;
                encoded[1] = (byte) messageBytes.length;
                System.arraycopy(messageBytes, 0, encoded, 2, messageBytes.length);
                return encoded;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }
}