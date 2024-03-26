package controller;

import entity.Book;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class bookController {
    private Connection connection;

    public bookController() {
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

    public void addBook(Book book) {
        String sql = "INSERT INTO books(title,author,genre, price,quantity) VALUES(?,?,?,?,?)";

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
        String sql = "DELETE FROM books WHERE book_id=?";
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
        String sql = "SELECT * From books WHERE book_id=?";
        Book book = null;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                book = new Book(resultSet.getInt("book_id"), resultSet.getString("title"),
                        resultSet.getString("author"), resultSet.getString("genre"),
                        resultSet.getDouble("price"), resultSet.getInt("quantity"));
            }


        } catch (SQLException e) {
            System.out.println(e);
        }
        return book;
    }
    public Book retrieveBookTitle(String bookTitlee) {
        String sql = "SELECT * From books WHERE title=?";
        Book book = null;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, bookTitlee);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                book = new Book(resultSet.getInt("book_id"), resultSet.getString("title"),
                        resultSet.getString("author"), resultSet.getString("genre"),
                        resultSet.getDouble("price"), resultSet.getInt("quantity"));
            }


        } catch (SQLException e) {
            System.out.println(e);
        }
        return book;
    }

    public List<Book> retrieveBooksByTitle(String title) {
        String sql = "SELECT * FROM books WHERE title=?";
        List<Book> books = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, title);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Book book = new Book(resultSet.getInt("book_id"), resultSet.getString("title"),
                        resultSet.getString("author"), resultSet.getString("genre"),
                        resultSet.getDouble("price"), resultSet.getInt("quantity"));
                books.add(book);
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return books;
    }

    public List<Book> retrieveBooksByAuthor(String author) {
        String sql = "SELECT * FROM books WHERE author=?";
        List<Book> books = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, author);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Book book = new Book(resultSet.getInt("book_id"), resultSet.getString("title"),
                        resultSet.getString("author"), resultSet.getString("genre"),
                        resultSet.getDouble("price"), resultSet.getInt("quantity"));
                books.add(book);
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return books;
    }

    public List<Book> retrieveBooksByGenre(String genre) {
        String sql = "SELECT * FROM books WHERE genre=?";
        List<Book> books = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, genre);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Book book = new Book(resultSet.getInt("book_id"), resultSet.getString("title"),
                        resultSet.getString("author"), resultSet.getString("genre"),
                        resultSet.getDouble("price"), resultSet.getInt("quantity"));
                books.add(book);
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return books;
    }

    public List<Book> retrieveBooks(int id) {
        String sql = "SELECT * From books ";
        Book book = null;
        List<Book> books = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                book = new Book(resultSet.getInt("book_id"), resultSet.getString("title"),
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