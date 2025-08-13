package com.library.dao;

import com.library.model.Borrowing;
import com.library.model.Book;
import com.library.model.Member;
import com.library.util.DatabaseConnection;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Data Access Object for Borrowing entity.
 */
public class BorrowingDAO {
    
    // SQL Queries
    private static final String INSERT_BORROWING = 
        "INSERT INTO borrowings (book_id, member_id, borrow_date, due_date, status) " +
        "VALUES (?, ?, ?, ?, ?)";
    
    private static final String SELECT_BY_ID = 
        "SELECT * FROM borrowings WHERE borrowing_id = ?";
    
    private static final String SELECT_ALL = 
        "SELECT * FROM borrowings ORDER BY borrow_date DESC";
    
    private static final String UPDATE_BORROWING = 
        "UPDATE borrowings SET book_id = ?, member_id = ?, borrow_date = ?, " +
        "due_date = ?, return_date = ?, status = ?, fine_amount = ? " +
        "WHERE borrowing_id = ?";
    
    private static final String DELETE_BORROWING = 
        "DELETE FROM borrowings WHERE borrowing_id = ?";
    
    private static final String SELECT_BY_MEMBER_ID = 
        "SELECT * FROM borrowings WHERE member_id = ? ORDER BY borrow_date DESC";
    
    private static final String SELECT_BY_BOOK_ID = 
        "SELECT * FROM borrowings WHERE book_id = ? ORDER BY borrow_date DESC";
    
    private static final String SELECT_CURRENT_BORROWINGS = 
        "SELECT * FROM borrowings WHERE status = 'BORROWED' ORDER BY borrow_date DESC";
    
    private static final String SELECT_OVERDUE_BORROWINGS = 
        "SELECT * FROM borrowings WHERE status = 'BORROWED' AND due_date < ? ORDER BY due_date";
    
    private static final String SELECT_BY_STATUS = 
        "SELECT * FROM borrowings WHERE status = ? ORDER BY borrow_date DESC";
    
    private static final String UPDATE_RETURN_DATE = 
        "UPDATE borrowings SET return_date = ?, fine_amount = ?, status = 'RETURNED' " +
        "WHERE borrowing_id = ?";
    
    private static final String UPDATE_STATUS = 
        "UPDATE borrowings SET status = ? WHERE borrowing_id = ?";
    
    private static final String SELECT_BORROWINGS_WITH_DETAILS = 
        "SELECT b.borrowing_id, b.borrow_date, b.due_date, b.return_date, b.status, b.fine_amount, " +
        "bk.book_id, bk.isbn, bk.title, bk.author, " +
        "m.member_id, m.member_code, m.first_name, m.last_name, m.email " +
        "FROM borrowings b " +
        "JOIN books bk ON b.book_id = bk.book_id " +
        "JOIN members m ON b.member_id = m.member_id " +
        "ORDER BY b.borrow_date DESC";

    /**
     * Add a new borrowing record.
     * 
     * @param borrowing Borrowing to add
     * @return true if successful, false otherwise
     */
    public boolean addBorrowing(Borrowing borrowing) {
        if (!borrowing.isValid()) {
            System.err.println("Invalid borrowing data: " + borrowing);
            return false;
        }

        Connection connection = null;
        PreparedStatement pstmt = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            pstmt = connection.prepareStatement(INSERT_BORROWING, Statement.RETURN_GENERATED_KEYS);
            
            pstmt.setInt(1, borrowing.getBookId());
            pstmt.setInt(2, borrowing.getMemberId());
            pstmt.setObject(3, borrowing.getBorrowDate());
            pstmt.setObject(4, borrowing.getDueDate());
            pstmt.setString(5, borrowing.getStatus().name());
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    borrowing.setBorrowingId(rs.getInt(1));
                }
                DatabaseConnection.commit(connection);
                System.out.println("Borrowing added successfully: ID " + borrowing.getBorrowingId());
                return true;
            }
            
        } catch (SQLException e) {
            DatabaseConnection.rollback(connection);
            System.err.println("Error adding borrowing: " + e.getMessage());
        } finally {
            closeResources(pstmt, connection);
        }
        return false;
    }

    /**
     * Get borrowing by ID.
     * 
     * @param borrowingId Borrowing ID
     * @return Optional containing borrowing if found
     */
    public Optional<Borrowing> getBorrowingById(int borrowingId) {
        Connection connection = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            pstmt = connection.prepareStatement(SELECT_BY_ID);
            pstmt.setInt(1, borrowingId);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return Optional.of(mapResultSetToBorrowing(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error retrieving borrowing by ID " + borrowingId + ": " + e.getMessage());
        } finally {
            closeResources(rs, pstmt, connection);
        }
        return Optional.empty();
    }

    /**
     * Get all borrowings.
     * 
     * @return List of all borrowings
     */
    public List<Borrowing> getAllBorrowings() {
        List<Borrowing> borrowings = new ArrayList<>();
        Connection connection = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            pstmt = connection.prepareStatement(SELECT_ALL);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                borrowings.add(mapResultSetToBorrowing(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error retrieving all borrowings: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Unexpected error retrieving borrowings: " + e.getMessage());
        } finally {
            closeResources(rs, pstmt, connection);
        }
        return borrowings;
    }

    /**
     * Get borrowings by member ID.
     * 
     * @param memberId Member ID
     * @return List of borrowings for the member
     */
    public List<Borrowing> getBorrowingsByMemberId(int memberId) {
        List<Borrowing> borrowings = new ArrayList<>();
        Connection connection = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            pstmt = connection.prepareStatement(SELECT_BY_MEMBER_ID);
            pstmt.setInt(1, memberId);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                borrowings.add(mapResultSetToBorrowing(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error retrieving borrowings for member ID " + memberId + ": " + e.getMessage());
        } finally {
            closeResources(rs, pstmt, connection);
        }
        return borrowings;
    }

    /**
     * Get borrowings by book ID.
     * 
     * @param bookId Book ID
     * @return List of borrowings for the book
     */
    public List<Borrowing> getBorrowingsByBookId(int bookId) {
        List<Borrowing> borrowings = new ArrayList<>();
        Connection connection = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            pstmt = connection.prepareStatement(SELECT_BY_BOOK_ID);
            pstmt.setInt(1, bookId);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                borrowings.add(mapResultSetToBorrowing(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error retrieving borrowings for book ID " + bookId + ": " + e.getMessage());
        } finally {
            closeResources(rs, pstmt, connection);
        }
        return borrowings;
    }

    /**
     * Get current borrowings (not returned).
     * 
     * @return List of current borrowings
     */
    public List<Borrowing> getCurrentBorrowings() {
        List<Borrowing> borrowings = new ArrayList<>();
        Connection connection = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            pstmt = connection.prepareStatement(SELECT_CURRENT_BORROWINGS);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                borrowings.add(mapResultSetToBorrowing(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error retrieving current borrowings: " + e.getMessage());
        } finally {
            closeResources(rs, pstmt, connection);
        }
        return borrowings;
    }

    /**
     * Get overdue borrowings.
     * 
     * @return List of overdue borrowings
     */
    public List<Borrowing> getOverdueBorrowings() {
        List<Borrowing> borrowings = new ArrayList<>();
        Connection connection = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            pstmt = connection.prepareStatement(SELECT_OVERDUE_BORROWINGS);
            pstmt.setObject(1, LocalDate.now());
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                borrowings.add(mapResultSetToBorrowing(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error retrieving overdue borrowings: " + e.getMessage());
        } finally {
            closeResources(rs, pstmt, connection);
        }
        return borrowings;
    }

    /**
     * Get borrowings by status.
     * 
     * @param status Status to filter by
     * @return List of borrowings with the specified status
     */
    public List<Borrowing> getBorrowingsByStatus(Borrowing.BorrowingStatus status) {
        List<Borrowing> borrowings = new ArrayList<>();
        Connection connection = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            pstmt = connection.prepareStatement(SELECT_BY_STATUS);
            pstmt.setString(1, status.name());
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                borrowings.add(mapResultSetToBorrowing(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error retrieving borrowings by status " + status + ": " + e.getMessage());
        } finally {
            closeResources(rs, pstmt, connection);
        }
        return borrowings;
    }

    /**
     * Return a book (update return date and fine).
     * 
     * @param borrowingId Borrowing ID
     * @param returnDate Return date
     * @param fineAmount Fine amount
     * @return true if successful, false otherwise
     */
    public boolean returnBook(int borrowingId, LocalDate returnDate, BigDecimal fineAmount) {
        Connection connection = null;
        PreparedStatement pstmt = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            pstmt = connection.prepareStatement(UPDATE_RETURN_DATE);
            pstmt.setObject(1, returnDate);
            pstmt.setBigDecimal(2, fineAmount);
            pstmt.setInt(3, borrowingId);
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                DatabaseConnection.commit(connection);
                System.out.println("Book returned successfully: borrowing ID " + borrowingId);
                return true;
            }
            
        } catch (SQLException e) {
            DatabaseConnection.rollback(connection);
            System.err.println("Error returning book for borrowing ID " + borrowingId + ": " + e.getMessage());
        } finally {
            closeResources(pstmt, connection);
        }
        return false;
    }

    /**
     * Update borrowing status.
     * 
     * @param borrowingId Borrowing ID
     * @param status New status
     * @return true if successful, false otherwise
     */
    public boolean updateStatus(int borrowingId, Borrowing.BorrowingStatus status) {
        Connection connection = null;
        PreparedStatement pstmt = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            pstmt = connection.prepareStatement(UPDATE_STATUS);
            pstmt.setString(1, status.name());
            pstmt.setInt(2, borrowingId);
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                DatabaseConnection.commit(connection);
                System.out.println("Borrowing status updated: ID " + borrowingId + " -> " + status);
                return true;
            }
            
        } catch (SQLException e) {
            DatabaseConnection.rollback(connection);
            System.err.println("Error updating borrowing status for ID " + borrowingId + ": " + e.getMessage());
        } finally {
            closeResources(pstmt, connection);
        }
        return false;
    }

    /**
     * Get borrowings with book and member details.
     * 
     * @return List of borrowings with details
     */
    public List<Borrowing> getBorrowingsWithDetails() {
        List<Borrowing> borrowings = new ArrayList<>();
        Connection connection = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            pstmt = connection.prepareStatement(SELECT_BORROWINGS_WITH_DETAILS);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Borrowing borrowing = mapResultSetToBorrowing(rs);
                
                // Set book details
                Book book = new Book();
                book.setBookId(rs.getInt("book_id"));
                book.setIsbn(rs.getString("isbn"));
                book.setTitle(rs.getString("title"));
                book.setAuthor(rs.getString("author"));
                borrowing.setBook(book);
                
                // Set member details
                Member member = new Member();
                member.setMemberId(rs.getInt("member_id"));
                member.setMemberCode(rs.getString("member_code"));
                member.setFirstName(rs.getString("first_name"));
                member.setLastName(rs.getString("last_name"));
                member.setEmail(rs.getString("email"));
                borrowing.setMember(member);
                
                borrowings.add(borrowing);
            }
            
        } catch (SQLException e) {
            System.err.println("Error retrieving borrowings with details: " + e.getMessage());
        } finally {
            closeResources(rs, pstmt, connection);
        }
        return borrowings;
    }

    /**
     * Update an existing borrowing.
     * 
     * @param borrowing Borrowing to update
     * @return true if successful, false otherwise
     */
    public boolean updateBorrowing(Borrowing borrowing) {
        if (!borrowing.isValid() || borrowing.getBorrowingId() <= 0) {
            System.err.println("Invalid borrowing data for update: " + borrowing);
            return false;
        }

        Connection connection = null;
        PreparedStatement pstmt = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            pstmt = connection.prepareStatement(UPDATE_BORROWING);
            
            pstmt.setInt(1, borrowing.getBookId());
            pstmt.setInt(2, borrowing.getMemberId());
            pstmt.setObject(3, borrowing.getBorrowDate());
            pstmt.setObject(4, borrowing.getDueDate());
            pstmt.setObject(5, borrowing.getReturnDate());
            pstmt.setString(6, borrowing.getStatus().name());
            pstmt.setBigDecimal(7, borrowing.getFineAmount());
            pstmt.setInt(8, borrowing.getBorrowingId());
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                DatabaseConnection.commit(connection);
                System.out.println("Borrowing updated successfully: ID " + borrowing.getBorrowingId());
                return true;
            }
            
        } catch (SQLException e) {
            DatabaseConnection.rollback(connection);
            System.err.println("Error updating borrowing: " + e.getMessage());
        } finally {
            closeResources(pstmt, connection);
        }
        return false;
    }

    /**
     * Delete a borrowing by ID.
     * 
     * @param borrowingId Borrowing ID to delete
     * @return true if successful, false otherwise
     */
    public boolean deleteBorrowing(int borrowingId) {
        Connection connection = null;
        PreparedStatement pstmt = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            pstmt = connection.prepareStatement(DELETE_BORROWING);
            pstmt.setInt(1, borrowingId);
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                DatabaseConnection.commit(connection);
                System.out.println("Borrowing deleted successfully: ID " + borrowingId);
                return true;
            }
            
        } catch (SQLException e) {
            DatabaseConnection.rollback(connection);
            System.err.println("Error deleting borrowing ID " + borrowingId + ": " + e.getMessage());
        } finally {
            closeResources(pstmt, connection);
        }
        return false;
    }

    /**
     * Map ResultSet to Borrowing object.
     * 
     * @param rs ResultSet
     * @return Borrowing object
     * @throws SQLException if mapping fails
     */
    private Borrowing mapResultSetToBorrowing(ResultSet rs) throws SQLException {
        Borrowing borrowing = new Borrowing();
        borrowing.setBorrowingId(rs.getInt("borrowing_id"));
        borrowing.setBookId(rs.getInt("book_id"));
        borrowing.setMemberId(rs.getInt("member_id"));
        borrowing.setBorrowDate(rs.getObject("borrow_date", LocalDate.class));
        borrowing.setDueDate(rs.getObject("due_date", LocalDate.class));
        
        java.sql.Date returnDate = rs.getDate("return_date");
        if (returnDate != null) {
            borrowing.setReturnDate(returnDate.toLocalDate());
        }
        
        borrowing.setStatus(Borrowing.BorrowingStatus.valueOf(rs.getString("status")));
        
        BigDecimal fineAmount = rs.getBigDecimal("fine_amount");
        if (fineAmount != null) {
            borrowing.setFineAmount(fineAmount);
        }
        
        return borrowing;
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
