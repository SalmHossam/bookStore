import java.io.*;
import java.net.Socket;

public class BookStoreClient {
    private static final String SERVER_IP = "127.0.0.1"; // Replace with the server's IP address
    private static final int SERVER_PORT = 1235;
    private static boolean isAuthenticated=false;

    public static void main(String[] args) {
        try {
            Socket socket = new Socket(SERVER_IP, SERVER_PORT);
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
            BufferedReader userInputReader = new BufferedReader(new InputStreamReader(System.in));

            System.out.println("Welcome to BookStore Server.");

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
                        String username = userInputReader.readLine();
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
                        writer.println("register," + name + "," + newUsername + "," + newPassword);
                        String registerResponse = reader.readLine();
                        System.out.println(registerResponse);
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
                System.out.println("7. Exit");
                System.out.print("Enter action number: ");
                String action = userInputReader.readLine();

                switch (action.trim().toLowerCase()) {
                    case "1":
                        System.out.print("Enter book title, author, genre, price, and quantity (comma-separated): ");
                        String[] bookInfo = userInputReader.readLine().split(",");
                        writer.println("addbook," + String.join(",", bookInfo));
                        System.out.println(reader.readLine());
                        break;
                    case "2":
                        System.out.print("Enter book id to delete: ");
                        String bookId = userInputReader.readLine();
                        writer.println("deletebook," + bookId);
                        System.out.println(reader.readLine());
                        break;
                    case "3":
                        System.out.print("Enter book id: ");
                        String bookId2 = userInputReader.readLine();
                        writer.println("getbookid," +  bookId2);
                        System.out.println(reader.readLine());
                        break;
                    case "4":
                        System.out.print("Enter book title: ");
                        String bookTitle = userInputReader.readLine();
                        writer.println("getbooktitle," +  bookTitle);
                        System.out.println(reader.readLine());
                        break;
                    case "5":
                        System.out.print("Enter book Author: ");
                        String bookAuthor = userInputReader.readLine();
                        writer.println("getbookauthor," + bookAuthor);
                        System.out.println(reader.readLine());
                        break;
                    case "6":
                        System.out.print("Enter book Genre: ");
                        String bookGenre = userInputReader.readLine();
                        writer.println("getbookgenre," + bookGenre);
                        System.out.println(reader.readLine());
                        break;

                    case "7":
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
}
