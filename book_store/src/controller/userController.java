package controller;

import entity.User;

import java.sql.*;

public class userController {

    private Connection connection;

    public userController() {
        String username = "root";
        String password = "";
        String dbName = "bookStore";

        String url = "jdbc:mysql://localhost/" + dbName + "?user=" + username + "&password=" + password + "&useUnicode=true&characterEncoding=UTF-8";


        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.connection = DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            System.out.println(e);
        }


    }

    public void addUser(User user) {
        String sql = "INSERT INTO users(name, username, password, user_type) VALUES(?,?,?,?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, user.getName());
            statement.setString(2, user.getUsername());
            statement.setString(3, user.getPassword());
            statement.setString(4, user.getUser_type());
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    public void deleteUser(int id) {
        String sql = "DELETE FROM users WHERE user_id=?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    public void updatePassword(User user) {
        String sql = "UPDATE users SET password=? WHERE username=?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, user.getPassword());
            statement.setString(2, user.getUsername()); // Assuming user.getId() returns the user's ID
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    public boolean getUser(User user) {
        String sql = "SELECT * FROM users WHERE username=? AND password=?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPassword());
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            System.out.println(e);
            return false;
        }
    }
}
