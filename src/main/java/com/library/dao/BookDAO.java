package com.library.dao;

import com.library.model.Book;
import com.library.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Data Access Object for Book entity.
 */
public class BookDAO {
    
    // SQL Queries
    private static final String INSERT_BOOK = 
        "INSERT INTO books (isbn, title, author, publisher, publication_year, genre, total_copies, available_copies) " +
        "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    
    private static final String SELECT_BY_ID = 
        "SELECT * FROM books WHERE book_id = ?";
    
    private static final String SELECT_BY_ISBN = 
        "SELECT * FROM books WHERE isbn = ?";
    
    private static final String SELECT_ALL = 
        "SELECT * FROM books ORDER BY title";
    
    private static final String UPDATE_BOOK = 
        "UPDATE books SET isbn = ?, title = ?, author = ?, publisher = ?, " +
        "publication_year = ?, genre = ?, total_copies = ?, available_copies = ? " +
        "WHERE book_id = ?";
    
    private static final String DELETE_BOOK = 
        "DELETE FROM books WHERE book_id = ?";
    
    private static final String SEARCH_BY_TITLE = 
        "SELECT * FROM books WHERE title LIKE ? ORDER BY title";
    
    private static final String SEARCH_BY_AUTHOR = 
        "SELECT * FROM books WHERE author LIKE ? ORDER BY author, title";
    
    private static final String SEARCH_BY_GENRE = 
        "SELECT * FROM books WHERE genre LIKE ? ORDER BY genre, title";
    
    private static final String SEARCH_AVAILABLE = 
        "SELECT * FROM books WHERE available_copies > 0 ORDER BY title";
    
    private static final String UPDATE_AVAILABLE_COPIES = 
        "UPDATE books SET available_copies = ? WHERE book_id = ?";

    /**
     * Add a new book to the database.
     * 
     * @param book Book to add
     * @return true if successful, false otherwise
     */
    public boolean addBook(Book book) {
        if (!book.isValid()) {
            System.err.println("Invalid book data: " + book);
            return false;
        }

        Connection connection = null;
        PreparedStatement pstmt = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            pstmt = connection.prepareStatement(INSERT_BOOK, Statement.RETURN_GENERATED_KEYS);
            
            pstmt.setString(1, book.getIsbn());
            pstmt.setString(2, book.getTitle());
            pstmt.setString(3, book.getAuthor());
            pstmt.setString(4, book.getPublisher());
            pstmt.setObject(5, book.getPublicationYear());
            pstmt.setString(6, book.getGenre());
            pstmt.setInt(7, book.getTotalCopies());
            pstmt.setInt(8, book.getAvailableCopies());
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    book.setBookId(rs.getInt(1));
                }
                DatabaseConnection.commit(connection);
                System.out.println("Book added successfully: " + book.getTitle());
                return true;
            }
            
        } catch (SQLException e) {
            DatabaseConnection.rollback(connection);
            System.err.println("Error adding book: " + e.getMessage());
        } finally {
            closeResources(pstmt, connection);
        }
        return false;
    }

    /**
     * Get book by ID.
     * 
     * @param bookId Book ID
     * @return Optional containing book if found
     */
    public Optional<Book> getBookById(int bookId) {
        Connection connection = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            pstmt = connection.prepareStatement(SELECT_BY_ID);
            pstmt.setInt(1, bookId);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return Optional.of(mapResultSetToBook(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error retrieving book by ID " + bookId + ": " + e.getMessage());
        } finally {
            closeResources(rs, pstmt, connection);
        }
        return Optional.empty();
    }

    /**
     * Get book by ISBN.
     * 
     * @param isbn ISBN
     * @return Optional containing book if found
     */
    public Optional<Book> getBookByIsbn(String isbn) {
        Connection connection = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            pstmt = connection.prepareStatement(SELECT_BY_ISBN);
            pstmt.setString(1, isbn);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return Optional.of(mapResultSetToBook(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error retrieving book by ISBN " + isbn + ": " + e.getMessage());
        } finally {
            closeResources(rs, pstmt, connection);
        }
        return Optional.empty();
    }

    /**
     * Get all books.
     * 
     * @return List of all books
     */
    public List<Book> getAllBooks() {
        List<Book> books = new ArrayList<>();
        Connection connection = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            pstmt = connection.prepareStatement(SELECT_ALL);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                books.add(mapResultSetToBook(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error retrieving all books: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Unexpected error retrieving books: " + e.getMessage());
        } finally {
            closeResources(rs, pstmt, connection);
        }
        return books;
    }

    /**
     * Update an existing book.
     * 
     * @param book Book to update
     * @return true if successful, false otherwise
     */
    public boolean updateBook(Book book) {
        if (!book.isValid() || book.getBookId() <= 0) {
            System.err.println("Invalid book data for update: " + book);
            return false;
        }

        Connection connection = null;
        PreparedStatement pstmt = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            pstmt = connection.prepareStatement(UPDATE_BOOK);
            
            pstmt.setString(1, book.getIsbn());
            pstmt.setString(2, book.getTitle());
            pstmt.setString(3, book.getAuthor());
            pstmt.setString(4, book.getPublisher());
            pstmt.setObject(5, book.getPublicationYear());
            pstmt.setString(6, book.getGenre());
            pstmt.setInt(7, book.getTotalCopies());
            pstmt.setInt(8, book.getAvailableCopies());
            pstmt.setInt(9, book.getBookId());
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                DatabaseConnection.commit(connection);
                System.out.println("Book updated successfully: " + book.getTitle());
                return true;
            }
            
        } catch (SQLException e) {
            DatabaseConnection.rollback(connection);
            System.err.println("Error updating book: " + e.getMessage());
        } finally {
            closeResources(pstmt, connection);
        }
        return false;
    }

    /**
     * Delete a book by ID.
     * 
     * @param bookId Book ID to delete
     * @return true if successful, false otherwise
     */
    public boolean deleteBook(int bookId) {
        Connection connection = null;
        PreparedStatement pstmt = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            pstmt = connection.prepareStatement(DELETE_BOOK);
            pstmt.setInt(1, bookId);
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                DatabaseConnection.commit(connection);
                System.out.println("Book deleted successfully: ID " + bookId);
                return true;
            }
            
        } catch (SQLException e) {
            DatabaseConnection.rollback(connection);
            System.err.println("Error deleting book ID " + bookId + ": " + e.getMessage());
        } finally {
            closeResources(pstmt, connection);
        }
        return false;
    }

    /**
     * Search books by title.
     * 
     * @param title Title to search for
     * @return List of matching books
     */
    public List<Book> searchByTitle(String title) {
        List<Book> books = new ArrayList<>();
        Connection connection = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            pstmt = connection.prepareStatement(SEARCH_BY_TITLE);
            pstmt.setString(1, "%" + title + "%");
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                books.add(mapResultSetToBook(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error searching books by title: " + e.getMessage());
        } finally {
            closeResources(rs, pstmt, connection);
        }
        return books;
    }

    /**
     * Search books by author.
     * 
     * @param author Author to search for
     * @return List of matching books
     */
    public List<Book> searchByAuthor(String author) {
        List<Book> books = new ArrayList<>();
        Connection connection = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            pstmt = connection.prepareStatement(SEARCH_BY_AUTHOR);
            pstmt.setString(1, "%" + author + "%");
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                books.add(mapResultSetToBook(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error searching books by author: " + e.getMessage());
        } finally {
            closeResources(rs, pstmt, connection);
        }
        return books;
    }

    /**
     * Search books by genre.
     * 
     * @param genre Genre to search for
     * @return List of matching books
     */
    public List<Book> searchByGenre(String genre) {
        List<Book> books = new ArrayList<>();
        Connection connection = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            pstmt = connection.prepareStatement(SEARCH_BY_GENRE);
            pstmt.setString(1, "%" + genre + "%");
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                books.add(mapResultSetToBook(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error searching books by genre: " + e.getMessage());
        } finally {
            closeResources(rs, pstmt, connection);
        }
        return books;
    }

    /**
     * Get all available books.
     * 
     * @return List of available books
     */
    public List<Book> getAvailableBooks() {
        List<Book> books = new ArrayList<>();
        Connection connection = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            pstmt = connection.prepareStatement(SEARCH_AVAILABLE);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                books.add(mapResultSetToBook(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error retrieving available books: " + e.getMessage());
        } finally {
            closeResources(rs, pstmt, connection);
        }
        return books;
    }

    /**
     * Update available copies for a book.
     * 
     * @param bookId Book ID
     * @param availableCopies New available copies count
     * @return true if successful, false otherwise
     */
    public boolean updateAvailableCopies(int bookId, int availableCopies) {
        Connection connection = null;
        PreparedStatement pstmt = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            pstmt = connection.prepareStatement(UPDATE_AVAILABLE_COPIES);
            pstmt.setInt(1, availableCopies);
            pstmt.setInt(2, bookId);
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                DatabaseConnection.commit(connection);
                System.out.println("Updated available copies for book ID " + bookId + ": " + availableCopies);
                return true;
            }
            
        } catch (SQLException e) {
            DatabaseConnection.rollback(connection);
            System.err.println("Error updating available copies for book ID " + bookId + ": " + e.getMessage());
        } finally {
            closeResources(pstmt, connection);
        }
        return false;
    }

    /**
     * Map ResultSet to Book object.
     * 
     * @param rs ResultSet
     * @return Book object
     * @throws SQLException if mapping fails
     */
    private Book mapResultSetToBook(ResultSet rs) throws SQLException {
        Book book = new Book();
        book.setBookId(rs.getInt("book_id"));
        book.setIsbn(rs.getString("isbn"));
        book.setTitle(rs.getString("title"));
        book.setAuthor(rs.getString("author"));
        book.setPublisher(rs.getString("publisher"));
        book.setPublicationYear(rs.getInt("publication_year"));
        book.setGenre(rs.getString("genre"));
        book.setTotalCopies(rs.getInt("total_copies"));
        book.setAvailableCopies(rs.getInt("available_copies"));
        return book;
    }

    /**
     * Close database resources.
     * 
     * @param rs ResultSet
     * @param pstmt PreparedStatement
     * @param connection Connection
     */
    private void closeResources(ResultSet rs, PreparedStatement pstmt, Connection connection) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                System.err.println("Error closing ResultSet: " + e.getMessage());
            }
        }
        closeResources(pstmt, connection);
    }

    /**
     * Close database resources.
     * 
     * @param pstmt PreparedStatement
     * @param connection Connection
     */
    private void closeResources(PreparedStatement pstmt, Connection connection) {
        if (pstmt != null) {
            try {
                pstmt.close();
            } catch (SQLException e) {
                System.err.println("Error closing PreparedStatement: " + e.getMessage());
            }
        }
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.err.println("Error closing PreparedStatement: " + e.getMessage());
            }
        }
    }
}
