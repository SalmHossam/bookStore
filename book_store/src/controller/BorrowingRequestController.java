package controller;

import entity.BorrowingRequest;
import entity.RequestHistory;

import java.sql.*;
import java.util.List;

public class BorrowingRequestController {
    private Connection connection;
    private RequestHistoryController requestHistoryController; // Add this field


    public BorrowingRequestController() {
        String username = "root";
        String password = "";
        String dbName = "bookStore";
        String url = "jdbc:mysql://localhost/" + dbName + "?user=" + username + "&password=" + password + "&useUnicode=true&characterEncoding=UTF-8";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.connection = DriverManager.getConnection(url, username, password);
            this.requestHistoryController = new RequestHistoryController();
        } catch (Exception e) {
            e.printStackTrace(); // Properly handle exceptions
        }
    }

    public boolean acceptBorrowingRequest(int requestId) {
        String sqlUpdateBorrowingRequest = "UPDATE borrowing_requests SET status = 'accepted' WHERE request_id = ?";
        String sqlUpdateRequestHistory = "UPDATE request_history SET status = 'accepted' WHERE request_id = ?";

        try (PreparedStatement statementUpdateBorrowingRequest = connection.prepareStatement(sqlUpdateBorrowingRequest);
             PreparedStatement statementUpdateRequestHistory = connection.prepareStatement(sqlUpdateRequestHistory)) {

            connection.setAutoCommit(false); // Start transaction

            // Update borrowing_requests table
            statementUpdateBorrowingRequest.setInt(1, requestId);
            statementUpdateBorrowingRequest.executeUpdate();

            // Update request_history table
            statementUpdateRequestHistory.setInt(1, requestId);
            statementUpdateRequestHistory.executeUpdate();

            // Commit transaction
            connection.commit();

            System.out.println("Borrowing request accepted successfully.");
        } catch (SQLException e) {
            try {
                connection.rollback(); // Rollback transaction if any error occurs
            } catch (SQLException rollbackException) {
                System.out.println("Error rolling back transaction: " + rollbackException.getMessage());
            }
            System.out.println("Error accepting borrowing request: " + e.getMessage());
            return false;
        } finally {
            try {
                connection.setAutoCommit(true); // Restore auto-commit mode
            } catch (SQLException autoCommitException) {
                System.out.println("Error setting auto-commit mode: " + autoCommitException.getMessage());
            }
        }

        return true;
    }

    public boolean rejectBorrowingRequest(int requestId) {
        String sqlUpdateBorrowingRequest = "UPDATE borrowing_requests SET status = 'rejected' WHERE request_id = ?";
        String sqlUpdateRequestHistory = "UPDATE request_history SET status = 'rejected' WHERE request_id = ?";

        try (PreparedStatement statementUpdateBorrowingRequest = connection.prepareStatement(sqlUpdateBorrowingRequest);
             PreparedStatement statementUpdateRequestHistory = connection.prepareStatement(sqlUpdateRequestHistory)) {

            connection.setAutoCommit(false); // Start transaction

            // Update borrowing_requests table
            statementUpdateBorrowingRequest.setInt(1, requestId);
            statementUpdateBorrowingRequest.executeUpdate();

            // Update request_history table
            statementUpdateRequestHistory.setInt(1, requestId);
            statementUpdateRequestHistory.executeUpdate();

            // Commit transaction
            connection.commit();

            System.out.println("Borrowing request rejected successfully.");
        } catch (SQLException e) {
            try {
                connection.rollback(); // Rollback transaction if any error occurs
            } catch (SQLException rollbackException) {
                System.out.println("Error rolling back transaction: " + rollbackException.getMessage());
            }
            System.out.println("Error rejecting borrowing request: " + e.getMessage());
            return false;
        } finally {
            try {
                connection.setAutoCommit(true); // Restore auto-commit mode
            } catch (SQLException autoCommitException) {
                System.out.println("Error setting auto-commit mode: " + autoCommitException.getMessage());
            }
        }

        return true;
    }


    public boolean createBorrowingRequest(String borrowerUsername, String lenderUsername, String bookTitle) {
        try {
            int borrowerId = getUserIdByUsername(borrowerUsername);
            int lenderId = getUserIdByUsername(lenderUsername);
            int bookId = getBookIdByTitle(bookTitle);

            // Check if the book exists
            if (bookId == -1) {
                System.out.println("Book with title '" + bookTitle + "' does not exist.");
                return false;
            }

            String sql = "INSERT INTO borrowing_requests (borrower_id, lender_id, book_id, status) VALUES (?, ?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                statement.setInt(1, borrowerId);
                statement.setInt(2, lenderId);
                statement.setInt(3, bookId);
                statement.setString(4, "pending");
                statement.executeUpdate();

                // Get the auto-generated request_id
                ResultSet generatedKeys = statement.getGeneratedKeys();
                int requestId = -1;
                if (generatedKeys.next()) {
                    requestId = generatedKeys.getInt(1);
                }

                // Save the request history
                RequestHistory requestHistory = new RequestHistory(0, requestId, "pending");
                requestHistoryController.save(requestHistory);

                System.out.println("Borrowing request created successfully.");
            }
        } catch (SQLException e) {
            System.out.println("Error creating borrowing request: " + e.getMessage());
            return false;
        }
        return true;
    }



    private int getUserIdByUsername(String username) throws SQLException {
        String sql = "SELECT user_id FROM users WHERE username=?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("user_id");
                }
            }
        }
        return -1; // If user not found
    }

    private int getBookIdByTitle(String title) throws SQLException {
        String sql = "SELECT book_id FROM books WHERE title=?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, title);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("book_id");
                }
            }
        }
        return -1; // If book not found
    }

    public String getRequestHistory(String username) {
        List<RequestHistory> requestHistory = requestHistoryController.findByUsername(username);

        StringBuilder historyBuilder = new StringBuilder();
        for (RequestHistory request : requestHistory) {
            historyBuilder.append("Request ID: ").append(request.getRequestId())
                    .append(", Status: ").append(request.getStatus())
                    .append("\n");
        }
        String history = historyBuilder.toString();
        return !history.isEmpty() ? history : "No request history found for user: " + username;
    }

}


