import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private final Socket clientSocket;

    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try (
                ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream())
        ) {
            // Handle client requests here
            // Example: Read input from client and send response
            Object inputObject;
            while ((inputObject = in.readObject()) != null) {
                // Process client request
                System.out.println("Received from client: " + inputObject);

                // You can implement book store logic here

                // Send response back to client
                out.writeObject("Server response: " + inputObject);
                out.flush();
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
