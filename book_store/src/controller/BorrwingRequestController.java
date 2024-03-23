package controller;

import java.sql.Connection;
import java.sql.DriverManager;

public class BorrwingRequestController {
    private  Connection connection;

    public BorrwingRequestController() {
        String username = "root";
        String password = "";
        String dbName="bookStore";
        String url= "jdbc:mysql://localhost/" + dbName + "?user=" + username + "&password=" + password + "&useUnicode=true&characterEncoding=UTF-8";


        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.connection = DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
}
