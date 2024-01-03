import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Client {
    public static void main(String[] args) throws IOException {
        List<String> serverAddresses = new ArrayList<>();
        serverAddresses.add("localhost");  // Add server IP addresses here

        List<Integer> serverPorts = new ArrayList<>();
        serverPorts.add(8888);  // Add server ports here

        System.out.println("Clock synchronization using the Berkeley algorithm");

        for (int i = 0; i < serverAddresses.size(); i++) {
            String serverAddress = serverAddresses.get(i);
            int serverPort = serverPorts.get(i);

            Socket socket = null;
            PrintWriter out = null;
            BufferedReader in = null;

            try {
                socket = new Socket(serverAddress, serverPort);
                out = new PrintWriter(socket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                // Get the client's time
                String clientTime = new java.util.Date().toString();

                // Send the client's time to the server
                out.println(clientTime);

                // Receive the server's time
                String serverTime = in.readLine();
                System.out.println("Received server's time: " + serverTime);

                // Calculate the clock adjustment
                long clientTimestamp = parseTimestamp(clientTime);
                long serverTimestamp = parseTimestamp(serverTime);
                long adjustment = serverTimestamp - clientTimestamp;

                // Synchronize the client's clock
                String synchronizedTime = formatTimestamp(clientTimestamp + adjustment);
                System.out.println("Synchronized time: " + synchronizedTime);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (socket != null)
                    socket.close();
                if (out != null)
                    out.close();
                if (in != null)
                    in.close();
            }
        }
    }

    private static long parseTimestamp(String timestamp) {
        return java.sql.Timestamp.valueOf(timestamp).getTime();
    }

    private static String formatTimestamp(long timestamp) {
        return new java.sql.Timestamp(timestamp).toString();
    }
}

