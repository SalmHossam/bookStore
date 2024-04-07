import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
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
    private static int rejectedRequestsCount = 0;
    private static int viewRequestHistoryCount = 0;
    private static int adminViewRequestHistoryCount = 0;

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
                                isAdmin = username.equals("root");
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
                            } else if (user_type.equals("admin") || user_type.equals("user")) {
                                userController.addUser(newUser);
                                writer.println("Registration successful.");

                            } else{
                                writer.println("Registration unsuccessful.");

                            }
                            break;
                        case "add_book":
                            if (parts.length == 6) {
                                String title = parts[1].trim();
                                String author = parts[2].trim();
                                String genre = parts[3].trim();
                                double price = Double.parseDouble(parts[4].trim());
                                int quantity = Integer.parseInt(parts[5].trim());

                                Book book = new Book(0,title, author, genre, price, quantity);
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
                                    writer.println(book);
                                }
                            } else {
                                writer.println("No books found by Author: " + author);
                            }
                            break;
                        case "get_book_by_id":
                            int id = Integer.parseInt(parts[1].trim());
                            Book bookById = bookController.retrieveBook(id);
                            if (bookById != null) {
                                writer.println(bookById);
                            } else {
                                writer.println("No book found with ID: " + id);
                            }
                            break;
                        case "get_book_by_title":
                            String title = parts[1].trim();
                            Book bookByTitle = bookController.retrieveBookTitle(title);
                            if (true) {
                                    writer.println(bookByTitle);

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
                                RequestHistory requestHistory = new RequestHistory(0 ,0,"pending");
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
                        case "library_statistics":
                            if (isAdmin) {
                                String statistics = getLibraryStatistics();
                                writer.println(statistics);
                                System.out.println(getLibraryStatistics());
                                writer.println("EndOfStatistics");


                            } else {
                                writer.println("You do not have permission to view library statistics.");
                            }
                            break;

                        case "initiate_chat":
                            this.username = parts[1].trim();
                            initiateChat();
                            break;
                        case "view_request_history":
                            username = parts.length > 1 ? parts[1].trim() : "";
                            if (!username.isEmpty()) {
                                String requestHistory = borrowingRequestController.getRequestHistory(username);
                                writer.println(requestHistory);
                                writer.println("EndOfHistory");
                            } else {
                                writer.println("Invalid username.");
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
        private String getLibraryStatistics() {
            int borrowedBooksCount = borrowingRequestController.getBorrowedBooksCount();
            int availableBooksCount = bookController.getAvailableBooksCount();
            int acceptedRequestsCount = borrowingRequestController.getAcceptedRequestsCount();
            int rejectedRequestsCount = borrowingRequestController.getRejectedRequestsCount();
            int pendingRequestsCount = borrowingRequestController.getPendingRequestsCount();
            List<Book>Books=bookController.retrieveBooks();

            StringBuilder statisticsMessage = new StringBuilder();
            statisticsMessage.append("Library Statistics:\n");
            statisticsMessage.append(String.format("Borrowing Transactions: %d\n", borrowedBooksCount));
            statisticsMessage.append(String.format("Available Books count: %d\n", availableBooksCount));
            statisticsMessage.append("Available Books:\n");
            for (Book book : Books) {
                statisticsMessage.append(String.format("  Title: %s, Author: %s, Genre: %s, Price: %.2f, Quantity: %d\n",
                        book.getTitle(), book.getAuthor(), book.getGenre(), book.getPrice(), book.getQuantity()));
            }
            statisticsMessage.append(String.format("Accepted Requests: %d\n", acceptedRequestsCount));
            statisticsMessage.append(String.format("Rejected Requests: %d\n", rejectedRequestsCount));
            statisticsMessage.append(String.format("pending Requests: %d\n", pendingRequestsCount));
            return statisticsMessage.toString();
        }

        private void initiateChat() {
            try {
                String message;
                while ((message = reader.readLine()) != null) {
                    broadcastMessage(username + ": " + message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
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
