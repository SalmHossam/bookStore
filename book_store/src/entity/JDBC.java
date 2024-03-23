package entity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static java.lang.Class.forName;

public class JDBC {
    public static void main(String[] args){
        String username="root";
        String password="";
        String dbName="bookStore";
        String url= "jdbc:mysql://localhost/" + dbName + "?user=" + username + "&password=" + password + "&useUnicode=true&characterEncoding=UTF-8";


        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(url,username,password);
            System.out.println(connection);
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }
}

