package controller;

import java.sql.Connection;
import java.sql.DriverManager;

public class userController {

    private Connection connection;

    public userController() {
        String username = "root";
        String password = "Salma@2001";
        String dbName = "bookStore";

        String url = "jdbc:mysql://localhost/" + dbName + "?user=" + username + "&password=" + password + "&useUnicode=true&characterEncoding=UTF-8";


        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            System.out.println(e);
        }


    }
}
