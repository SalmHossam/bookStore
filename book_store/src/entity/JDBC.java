package entity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static java.lang.Class.forName;

public class JDBC {
    public static void main(String[] args){
        String url="jdbc:mysql://localhost::3306/bookStore";
        String username="root";
        String password="Salma@2001";

        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(url,username,password);
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }
}

