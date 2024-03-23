import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

import controller.bookController;
import controller.userController;
import entity.Book;
import entity.User;

public class BookStoreServer {
    private static final int PORT = 1235;
    private static bookController bookController;
    private static userController userController;

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            bookController = new bookController();
            userController = new userController();

            System.out.println("Bookstore server started. Listening on port " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected: " + clientSocket);

                new Thread(new ClientHandler(clientSocket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class ClientHandler implements Runnable {
        private Socket clientSocket;
        private BufferedReader reader;
        private PrintWriter writer;

        public ClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
            try {
                reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                writer = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()), true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            try {
                String clientMessage;
                while ((clientMessage = reader.readLine()) != null) {
                    System.out.println("Received from client: " + clientMessage);

                    String[] parts = clientMessage.split(",");
                    String action = parts[0].trim();

                    switch (action.toLowerCase()) {
                        case "login":
                            String username = parts[1].trim();
                            String password = parts[2].trim();
                            User loginUser = new User(username, password);
                            boolean isAuthenticated = userController.getUser(loginUser);
                            if (isAuthenticated) {
                                writer.println("Login successful.");
                            } else {
                                writer.println("Invalid credentials.");
                            }
                            break;
                        case "register":
                            String name = parts[1].trim();
                            username = parts[2].trim();
                            password = parts[3].trim();
                            User newUser = new User(name, username, password);
                            userController.addUser(newUser);
                            writer.println("Registration successful.");
                            break;
                        case "addbook":
                            if (parts.length == 6) {
                                String title = parts[1].trim();
                                String author = parts[2].trim();
                                String genre = parts[3].trim();
                                double price = Double.parseDouble(parts[4].trim());
                                int quantity = Integer.parseInt(parts[5].trim());

                                Book book = new Book(0,title, author, genre, price, quantity); // Assuming the id is auto-generated by the database
                                bookController.addBook(book);
                                writer.println("Book added successfully.");
                            } else {
                                writer.println("Invalid message format for adding a book.");
                            }
                            break;

                        case "deletebook":
                            int bookId = Integer.parseInt(parts[1]);
                            bookController.deleteBook(bookId);
                            writer.println("Book deleted successfully.");
                            break;
                        case "getbookauthor":
                            String author = parts[1].trim();
                            List<Book> booksByAuthor = bookController.retrieveBooksByAuthor(author);
                            if (!booksByAuthor.isEmpty()) {
                                for (Book book : booksByAuthor) {
                                    writer.println(book); // Send each retrieved book back to the client
                                }
                            } else {
                                writer.println("No books found by Author: " + author);
                            }
                            break;
                        case "getbookid":
                            int id = Integer.parseInt(parts[1].trim());
                            Book bookById = bookController.retrieveBook(id);
                            if (bookById != null) {
                                writer.println(bookById); // Send the retrieved book back to the client
                            } else {
                                writer.println("No book found with ID: " + id);
                            }
                            break;
                        case "getbooktitle":
                            String title = parts[1].trim();
                            List<Book> booksByTitle = bookController.retrieveBooksByTitle(title);
                            if (!booksByTitle.isEmpty()) {
                                for (Book book : booksByTitle) {
                                    writer.println(book); // Send each retrieved book back to the client
                                }
                            } else {
                                writer.println("No books found by Title: " + title);
                            }
                            break;

                        case "getbookgenre":
                            String genre = parts[1].trim();
                            List<Book> booksByGenre = bookController.retrieveBooksByTitle(genre);
                            if (!booksByGenre.isEmpty()) {
                                for (Book book : booksByGenre) {
                                    writer.println(book); // Send each retrieved book back to the client
                                }
                            } else {
                                writer.println("No books found by Genre: " + genre);
                            }
                            break;
                        // Implement other actions as needed
                        default:
                            writer.println("Invalid option.");
                            break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (reader != null) {
                        reader.close();
                    }
                    if (writer != null) {
                        writer.close();
                    }
                    if (clientSocket != null) {
                        clientSocket.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
