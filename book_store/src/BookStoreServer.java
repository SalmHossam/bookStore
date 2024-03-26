import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.List;
import java.util.Vector;

import controller.BorrowingRequestController;
import controller.RequestHistoryController;
import controller.bookController;
import controller.userController;
import entity.Book;
import entity.RequestHistory;
import entity.User;

public class BookStoreServer {
    private static final int PORT = 1235;
    private static bookController bookController;
    private static userController userController;
    private static int clientCounter = 1;
    private static Vector<ClientHandler> clients = new Vector<>();

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            bookController = new bookController();
            userController = new userController();

            System.out.println("Bookstore server started. Listening on port " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected  " + clientCounter+":"+clientSocket);

                ClientHandler clientHandler = new ClientHandler(clientSocket, clients);
                clients.add(clientHandler);
                new Thread(clientHandler).start();
                clientCounter++;

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class ClientHandler implements Runnable {
        private Socket clientSocket;
        private BufferedReader reader;
        private PrintWriter writer;
        private bookController bookController;
        private userController userController;
        private BorrowingRequestController borrowingRequestController;
        private String username;
        private RequestHistoryController requestHistoryController;
        private boolean isAdmin=false;

        public ClientHandler(Socket clientSocket, Vector<ClientHandler> clients) {
            this.clientSocket = clientSocket;
            this.bookController = new bookController();
            this.userController = new userController();
            this.borrowingRequestController = new BorrowingRequestController();
            this.requestHistoryController = new RequestHistoryController();

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
                    String features = parts[0].trim();

                    switch (features.toLowerCase()) {
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
                            String user_type=parts[4].trim();
                            User newUser = new User(name, username, password,user_type);
                            if(user_type.equals("admin")){
                                isAdmin=true;
                            }

                            boolean isRegistered = userController.getUser(newUser);

                            if (isRegistered){
                                writer.println("Cannot Register , User already exist");
                            }
                            else{
                                userController.addUser(newUser);
                                writer.println("Registration successful.");
                            }
                            break;
                        case "add_book":
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

                        case "delete_book":
                            int bookId = Integer.parseInt(parts[1]);
                            bookController.deleteBook(bookId);
                            writer.println("Book deleted successfully.");
                            break;
                        case "get_book_by_author":
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
                        case "get_book_by_id":
                            int id = Integer.parseInt(parts[1].trim());
                            Book bookById = bookController.retrieveBook(id);
                            if (bookById != null) {
                                writer.println(bookById); // Send the retrieved book back to the client
                            } else {
                                writer.println("No book found with ID: " + id);
                            }
                            break;
                        case "get_book_by_title":
                            String title = parts[1].trim();
                            List<Book> booksByTitle = bookController.retrieveBooksByTitle(title);
                            if (!booksByTitle.isEmpty()) {
                                for (Book book : booksByTitle) {
                                    writer.println(book);
                                }
                            } else {
                                writer.println("No books found by Title: " + title);
                            }
                            break;

                        case "get_book_by_genre":
                            String genre = parts[1].trim();
                            List<Book> booksByGenre = bookController.retrieveBooksByGenre(genre);
                            if (!booksByGenre.isEmpty()) {
                                for (Book book : booksByGenre) {
                                    writer.println(book);
                                }
                            } else {
                                writer.println("No books found by Genre: " + genre);
                            }
                            break;
                        case "submit_borrowing_request":
                            String borrowerUsername = parts[1].trim();
                            String lenderUsername = parts[2].trim();
                            String bookTitle = parts[3].trim();
                            boolean request = borrowingRequestController.createBorrowingRequest(borrowerUsername, lenderUsername, bookTitle);
                            if (request) {
                                writer.println("Borrowing request submitted successfully.");
                                // Update request history after submitting the borrowing request
                                RequestHistory requestHistory = new RequestHistory(0 ,0,"pending"); // Assuming you create a RequestHistory object here
                                requestHistoryController.save(requestHistory);
                            } else {
                                writer.println("Error, Borrowing request didn't submit successfully.");
                            }
                            break;
                        case "accept_request":
                            int requestIdToAccept = Integer.parseInt(parts[1].trim());
                            boolean isAccepted = borrowingRequestController.acceptBorrowingRequest(requestIdToAccept);
                            if (isAccepted) {
                                writer.println("Request accepted successfully.");
                                initiateChat();
                            } else {
                                writer.println("Failed to accept the request.");
                            }
                            break;

                        case "reject_request":
                            int requestIdToReject = Integer.parseInt(parts[1].trim());
                            boolean isRejected = borrowingRequestController.rejectBorrowingRequest(requestIdToReject);
                            if (isRejected) {
                                writer.println("Request rejected successfully.");
                            } else {
                                writer.println("Failed to reject the request.");
                            }
                            break;
                        case "initiate_chat":
                            this.username = parts[1].trim();
                            initiateChat();
                            break;
                        case "view_request_history":
                            username = parts.length > 1 ? parts[1].trim() : ""; // Check if username parameter exists
                            if (!username.isEmpty()) {
                                String requestHistory = borrowingRequestController.getRequestHistory(username); // Get request history for the user
                                writer.println(requestHistory); // Send request history to client
                            } else {
                                writer.println("Invalid username."); // Send error message to client
                            }
                            break;

                        default:
                            writer.println("Invalid option.");
                            break;

                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }  finally {
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
        private void initiateChat() {
            try {
                String message;
                while ((message = reader.readLine()) != null) {
                    // Broadcast the message to other clients
                    broadcastMessage(username + ": " + message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                // Remove the client from the list when the chat ends
                synchronized (clients) {
                    clients.remove(this);
                }
            }
        }

        private void broadcastMessage(String message) {
            for (ClientHandler client : clients) {
                if (client != this) {
                    client.sendMessage(message);
                }
            }
        }

        private void sendMessage(String message) {
            writer.println(message);
        }
    }
}
