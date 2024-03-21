import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;

public class BookStoreClient {
    private Scanner scanner;

    public void start() {
        System.out.println("Welcome to the Online Bookstore!");
        boolean isLoggedIn = false;

        while (!isLoggedIn) {
            isLoggedIn = showLoginMenu();
        }

        showMainMenu();
    }

    private boolean showLoginMenu() {
        System.out.println("Please select an option:");
        System.out.println("1. Login");
        System.out.println("2. Register");
        System.out.println("3. Exit");

        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        switch (choice) {
            case 1:
                // Implement login logic
                break;
            case 2:
                // Implement registration logic
                break;
            case 3:
                System.out.println("Exiting...");
                System.exit(0);
            default:
                System.out.println("Invalid choice. Please try again.");
                return false;
        }

        return true; // If logged in successfully
    }

    private void showMainMenu() {
        boolean isRunning = true;

        while (isRunning) {
            System.out.println("Please select an option:");
            System.out.println("1. Browse Books");
            System.out.println("2. Search Books");
            System.out.println("3. Add Book");
            System.out.println("4. Remove Book");
            System.out.println("5. Submit Request");
            System.out.println("6. Check Requests");
            System.out.println("7. Request History");
            System.out.println("8. Logout");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    // Implement browse books logic
                    break;
                case 2:
                    // Implement search books logic
                    break;
                case 3:
                    // Implement add book logic
                    break;
                case 4:
                    // Implement remove book logic
                    break;
                case 5:
                    // Implement submit request logic
                    break;
                case 6:
                    // Implement check requests logic
                    break;
                case 7:
                    // Implement request history logic
                    break;
                case 8:
                    System.out.println("Logging out...");
                    isRunning = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
    public static void runMultipleRequests(int numOfClients) {
        String hostname = "localhost";
        int port = 1253;

        for (int i = 0; i < numOfClients; i++) {
            try {
                // create a socket
                Socket socket = new Socket(hostname, port);


                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(socket.getOutputStream()));

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


    public static void main(String[] args) {
        runMultipleRequests(5);
    }
}
