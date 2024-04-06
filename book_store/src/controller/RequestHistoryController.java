package controller;

import entity.RequestHistory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RequestHistoryController {

    private Connection connection;

    public RequestHistoryController() {
        String username = "root";
        String password = "";
        String dbName = "bookStore";
        String url = "jdbc:mysql://localhost/" + dbName + "?user=" + username + "&password=" + password + "&useUnicode=true&characterEncoding=UTF-8";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.connection = DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<RequestHistory> findByUsername(String username) {
        List<RequestHistory> requestHistoryList = new ArrayList<>();

        String sql = "SELECT rh.history_id, rh.request_id, rh.status " +
                "FROM request_history rh " +
                "JOIN borrowing_requests br ON rh.request_id = br.request_id " +
                "JOIN users u ON br.borrower_id = u.user_id " +
                "WHERE u.username = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    int historyId = resultSet.getInt("history_id");
                    int requestId = resultSet.getInt("request_id");
                    String status = resultSet.getString("status");

                    RequestHistory requestHistory = new RequestHistory(historyId, requestId, status);
                    requestHistoryList.add(requestHistory);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return requestHistoryList;
    }


    public void save(RequestHistory requestHistory) {
        if (requestHistory == null) {
            System.out.println("RequestHistory object is null. Cannot save.");
            return;
        }

        String sql = "INSERT INTO request_history (request_id, status) VALUES (?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, requestHistory.getRequestId());
            statement.setString(2, requestHistory.getStatus());
            statement.executeUpdate();
            System.out.println("Request history saved successfully.");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
