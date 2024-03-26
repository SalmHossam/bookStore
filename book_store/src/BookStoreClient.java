import java.io.*;
import java.net.Socket;

public class BookStoreClient {
    private static final String SERVER_IP = "localhost";
    private static final int SERVER_PORT = 1235;
    private static boolean isAuthenticated=false;
    private static boolean isAdmin = false;
    private static String username;

    public static void main(String[] args) {
        try {
            Socket socket = new Socket(SERVER_IP, SERVER_PORT);
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
            BufferedReader userInputReader = new BufferedReader(new InputStreamReader(System.in));

            System.out.println("Connected to server.");

            while (!isAuthenticated) {
                System.out.println("\nPlease choose Option:");
                System.out.println("1. Login");
                System.out.println("2. Register");
                System.out.println("3. Exit");
                System.out.print("Enter action number: ");
                String action = userInputReader.readLine();

                switch (action.trim().toLowerCase()) {
                    case "1":
                        System.out.print("Enter username: ");
                        username = userInputReader.readLine(); 
                        System.out.print("Enter password: ");
                        String password = userInputReader.readLine();
                        writer.println("login," + username + "," + password);
                        String loginResponse = reader.readLine();
                        System.out.println(loginResponse);
                        isAuthenticated = loginResponse.equals("Login successful.");
                        break;
                    case "2":
                        System.out.print("Enter name: ");
                        String name = userInputReader.readLine();
                        System.out.print("Enter username: ");
                        String newUsername = userInputReader.readLine();
                        System.out.print("Enter password: ");
                        String newPassword = userInputReader.readLine();
                        System.out.print("Enter type: ");
                        String user_type = userInputReader.readLine();
                        writer.println("register," + name + "," + newUsername + "," + newPassword+","+user_type);
                        String registerResponse = reader.readLine();
                        System.out.println(registerResponse);
                        if (registerResponse.equals("Registration successful.")) {
                            username = newUsername; // Update the username variable
                            isAuthenticated = true;
                        }
                        break;

                    case "3":
                        writer.println("exit");
                        socket.close();
                        System.out.println("Disconnected from server.");
                        return;
                    default:
                        System.out.println("Invalid action. Please try again.");
                        break;
                }
            }

            while (true) {
                System.out.println("\nPlease choose Option:");
                System.out.println("1. Add book");
                System.out.println("2. Delete book");
                System.out.println("3. browse book by Id");
                System.out.println("4. browse book by Title");
                System.out.println("5. browse book by Author");
                System.out.println("6. browse book by Genre");
                System.out.println("7. Submit borrowing request");
                System.out.println("8. Accept borrowing request");
                System.out.println("9. Reject borrowing request");
                if (isAdmin) {
                    System.out.println("10. BookStore Analytics");
                }
                System.out.println("10. Request History");
                System.out.println("11.Exit");
                System.out.print("Enter action number: ");
                String action = userInputReader.readLine();

                switch (action.trim().toLowerCase()) {
                    case "1":
                        System.out.print("Enter book title, author, genre, price, and quantity (comma-separated): ");
                        String[] bookInfo = userInputReader.readLine().split(",");
                        writer.println("add_book," + String.join(",", bookInfo));
                        System.out.println(reader.readLine());
                        break;
                    case "2":
                        System.out.print("Enter book id to delete: ");
                        String bookId = userInputReader.readLine();
                        writer.println("delete_book," + bookId);
                        System.out.println(reader.readLine());
                        break;
                    case "3":
                        System.out.print("Enter book id: ");
                        String bookId2 = userInputReader.readLine();
                        writer.println("get_book_by_id," +  bookId2);
                        System.out.println(reader.readLine());
                        break;
                    case "4":
                        System.out.print("Enter book title: ");
                        String bookTitle = userInputReader.readLine();
                        writer.println("get_book_by_title," +  bookTitle);
                        System.out.println(reader.readLine());
                        break;
                    case "5":
                        System.out.print("Enter book Author: ");
                        String bookAuthor = userInputReader.readLine();
                        writer.println("get_book_by_author," + bookAuthor);
                        System.out.println(reader.readLine());
                        break;
                    case "6":
                        System.out.print("Enter book Genre: ");
                        String bookGenre = userInputReader.readLine();
                        writer.println("get_book_by_genre," + bookGenre);
                        System.out.println(reader.readLine());
                        break;
                    case "7":
                        System.out.print("Enter borrower's username: ");
                        String borrowerUsername = userInputReader.readLine();
                        System.out.print("Enter lender's username: ");
                        String lenderUsername = userInputReader.readLine();
                        System.out.print("Enter book title: ");
                        String book = userInputReader.readLine();
                        writer.println("submit_borrowing_request," + borrowerUsername+","+lenderUsername+","+book);
                        System.out.println(reader.readLine());
                        break;
                    case "8": // Accept borrowing request and option to initiate chat
                        System.out.print("Enter request ID to accept: ");
                        String requestIdToAccept = userInputReader.readLine();
                        writer.println("accept_request," + requestIdToAccept);
                        String acceptResponse = reader.readLine();
                        if (acceptResponse.equals("Request accepted successfully.")) {
                            System.out.print("Do you want to initiate chat? (yes/no): ");
                            String initiateChatChoice = userInputReader.readLine().toLowerCase();
                            if (initiateChatChoice.equals("yes")) {
                                writer.println("initiate_chat," + username);
                                initiateChat(reader, writer, userInputReader, username);
                            } else {
                                System.out.println("Chat not initiated.");
                                System.out.println("Successfully accepted request.");
                            }
                        } else {
                            System.out.println("Failed to accept the request.");
                        }
                        break;

                    case "9":
                        System.out.print("Enter request ID to reject: ");
                        String requestIdToReject = userInputReader.readLine();
                        writer.println("reject_request," + requestIdToReject);
                        String rejectResponse = reader.readLine();
                        if (rejectResponse.equals("Request rejected successfully.")) {

                        } else {
                            System.out.println("Failed to accept the request.");
                        }
                        break;

                    case "10":
                        writer.println("view_request_history," + username); // Send request to server
                        String historyResponse = reader.readLine(); // Receive response from server
                        System.out.println("Request History:");
                        System.out.println(historyResponse); // Display request history to user
                        break;
                    case "11":
                        writer.println("exit");
                        socket.close();
                        System.out.println("Disconnected from server.");
                        return;
                    default:
                        System.out.println("Invalid action. Please try again.");
                        break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static void initiateChat(BufferedReader reader, PrintWriter writer, BufferedReader userInputReader, String username) throws IOException {
        System.out.println("Chat initiated. You can start typing your messages.");

        // Thread to continuously receive messages from the server and print them
        Thread receiveThread = new Thread(() -> {
            try {
                String message;
                while ((message = reader.readLine()) != null) {
                    if (message.equals("exit")) {
                        break; // Exit the loop if the server signals the end of the chat
                    }
                    System.out.println(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        receiveThread.start(); // Start the receive thread

        // Thread to continuously read user input and send messages to the server
        Thread sendThread = new Thread(() -> {
            try {
                String userInput;
                while ((userInput = userInputReader.readLine()) != null) {
                    writer.println(userInput); // Send message to the server
                    if (userInput.equalsIgnoreCase("exit")) {
                        break; // Exit the loop if the user types "exit"
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        sendThread.start(); // Start the send thread

        try {
            sendThread.join(); // Wait for the send thread to finish
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            // Notify the server that the chat is ending
            writer.println("exit");
            try {
                receiveThread.join(); // Wait for the receive thread to finish
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
