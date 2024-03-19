import java.io.*;
import java.net.Socket;
import java.util.Scanner;
public class BookStoreClient {
    public static void main(String[] args) {
        runSingleRequest();
        // runMultipleRequests(5);
    }

    public static void runSingleRequest() {
        String hostname = "localhost";
        int port = 6666;

        try {
            // create a socket
            Socket socket = new Socket(hostname, port);

            // perform a simple math operation "5*10"
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(socket.getOutputStream()));

            Scanner scanner = new Scanner(System.in);
            String msg = scanner.nextLine();

            writer.write(msg);
            writer.newLine();
            writer.flush();

            // get the result from the server
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            System.out.println(reader.readLine());

            reader.close();
            writer.close();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void runMultipleRequests(int numOfClients) {
        String hostname = "localhost";
        int port = 6666;

        for (int i = 0; i < numOfClients; i++) {
            try {
                // create a socket
                Socket socket = new Socket(hostname, port);

                // perform a simple math operation "12+21"
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(socket.getOutputStream()));
                writer.write("+:" + i + ":21");
                writer.newLine();
                writer.flush();

                // get the result from the server
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));
                System.out.println(reader.readLine());

                reader.close();
                writer.close();
                socket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}




