package controller;

import entity.Book;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class bookController {
    private Connection connection;

    public bookController() {
        String url= "jdbc:mysql://localhost/" + dbName + "?user=" + username + "&password=" + password + "&useUnicode=true&characterEncoding=UTF-8";
        String username = "root";
        String password = "Salma@2001";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void addBook(Book book) {
        String sql = "INSERT INTO books(title,auther,genre, price,quantity) VALUES(?,?,?,?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, book.getTitle());
            statement.setString(2, book.getAuthor());
            statement.setString(3, book.getGenre());
            statement.setDouble(4, book.getPrice());
            statement.setInt(5, book.getQuantity());

            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e);
        }

    }

    public void deleteBook(int id) {
        String sql = "DELETE FROM books WHERE id=?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    public void updateBook(Book book) {
        String sql = "UPDATE books SET title=?,auther=?,genre=?, price=?,quantity=? ";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, book.getTitle());
            statement.setString(2, book.getAuthor());
            statement.setString(3, book.getGenre());
            statement.setDouble(4, book.getPrice());
            statement.setInt(5, book.getQuantity());

            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e);
        }

    }

    public Book retrieveBook(int id) {
        String sql = "SELECT * From books WHERE id=?";
        Book book = null;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                book = new Book(resultSet.getInt("id"), resultSet.getString("title"),
                        resultSet.getString("author"), resultSet.getString("genre"),
                        resultSet.getDouble("price"), resultSet.getInt("quantity"));
            }


        } catch (SQLException e) {
            System.out.println(e);
        }
        return book;
    }
    public Book retrieveBookByTitle(Book book) {
        String sql = "SELECT * From books WHERE title=?";
        Book book2 = null;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, book.getTitle());
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                book = new Book(resultSet.getInt("id"), resultSet.getString("title"),
                        resultSet.getString("author"), resultSet.getString("genre"),
                        resultSet.getDouble("price"), resultSet.getInt("quantity"));
            }


        } catch (SQLException e) {
            System.out.println(e);
        }
        return book2;
    }
    public Book retrieveBookByAuthour(Book book) {
        String sql = "SELECT * From books WHERE author=?";
        Book book3 = null;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, book.getAuthor());
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                book = new Book(resultSet.getInt("id"), resultSet.getString("title"),
                        resultSet.getString("author"), resultSet.getString("genre"),
                        resultSet.getDouble("price"), resultSet.getInt("quantity"));
            }


        } catch (SQLException e) {
            System.out.println(e);
        }
        return book3;
    }
    public Book retrieveBookByGenre(Book book) {
        String sql = "SELECT * From books WHERE genre=?";
        Book book4 = null;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, book.getAuthor());
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                book = new Book(resultSet.getInt("id"), resultSet.getString("title"),
                        resultSet.getString("author"), resultSet.getString("genre"),
                        resultSet.getDouble("price"), resultSet.getInt("quantity"));
            }


        } catch (SQLException e) {
            System.out.println(e);
        }
        return book4;
    }

    public List<Book> retrieveBooks(int id) {
        String sql = "SELECT * From books ";
        Book book = null;
        List<Book>books=new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                book = new Book(resultSet.getInt("id"), resultSet.getString("title"),
                        resultSet.getString("author"), resultSet.getString("genre"),
                        resultSet.getDouble("price"), resultSet.getInt("quantity"));
                books.add(book);
            }


        } catch (SQLException e) {
            System.out.println(e);
        }
        return books;
    }
}
