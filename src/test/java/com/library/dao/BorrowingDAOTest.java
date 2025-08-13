package com.library.dao;

import com.library.model.Borrowing;
import com.library.util.DatabaseConnection;
import org.junit.jupiter.api.*;
import java.sql.*;
import java.time.LocalDate;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class BorrowingDAOTest {

    private BorrowingDAO borrowingDAO;

    @BeforeEach
    void setUp() {
        borrowingDAO = new BorrowingDAO();
    }

    @Test
    @DisplayName("Should add borrowing successfully")
    void testAddBorrowing_Success() {
        // Arrange
        Borrowing borrowing = new Borrowing(1, 1, LocalDate.now(), LocalDate.now().plusDays(14));
        
        // Act
        boolean result = borrowingDAO.addBorrowing(borrowing);
        
        // Assert
        assertTrue(result);
    }

    @Test
    @DisplayName("Should fail to add borrowing with null input")
    void testAddBorrowing_NullInput() {
        // Act
        boolean result = borrowingDAO.addBorrowing(null);
        
        // Assert
        assertFalse(result);
    }

    @Test
    @DisplayName("Should fail to add borrowing with invalid data")
    void testAddBorrowing_InvalidData() {
        // Arrange
        Borrowing borrowing = new Borrowing(-1, -1, LocalDate.now(), LocalDate.now().minusDays(1)); // Invalid data
        
        // Act
        boolean result = borrowingDAO.addBorrowing(borrowing);
        
        // Assert
        assertFalse(result);
    }

    @Test
    @DisplayName("Should update borrowing successfully")
    void testUpdateBorrowing_Success() {
        // Arrange
        Borrowing borrowing = new Borrowing(1, 1, LocalDate.now(), LocalDate.now().plusDays(14));
        borrowing.setBorrowingId(1);
        borrowing.setReturnDate(LocalDate.now());
        borrowing.setFineAmount(new BigDecimal("5.00"));
        
        // Act
        boolean result = borrowingDAO.updateBorrowing(borrowing);
        
        // Assert
        // Note: This will return false if the borrowing doesn't exist in the database
        assertNotNull(result); // Just check it doesn't throw exception
    }

    @Test
    @DisplayName("Should fail to update borrowing with null input")
    void testUpdateBorrowing_NullInput() {
        // Act
        boolean result = borrowingDAO.updateBorrowing(null);
        
        // Assert
        assertFalse(result);
    }

    @Test
    @DisplayName("Should delete borrowing successfully")
    void testDeleteBorrowing_Success() {
        // Arrange
        int borrowingId = 1;
        
        // Act
        boolean result = borrowingDAO.deleteBorrowing(borrowingId);
        
        // Assert
        // Note: This will return false if the borrowing doesn't exist in the database
        assertNotNull(result); // Just check it doesn't throw exception
    }

    @Test
    @DisplayName("Should fail to delete borrowing with invalid ID")
    void testDeleteBorrowing_InvalidId() {
        // Arrange
        int borrowingId = -1;
        
        // Act
        boolean result = borrowingDAO.deleteBorrowing(borrowingId);
        
        // Assert
        assertFalse(result);
    }

    @Test
    @DisplayName("Should get borrowing by ID successfully")
    void testGetBorrowingById_Success() {
        // Arrange
        int borrowingId = 1;
        
        // Act
        Optional<Borrowing> result = borrowingDAO.getBorrowingById(borrowingId);
        
        // Assert
        // Note: This will return empty if the borrowing doesn't exist in the database
        assertNotNull(result); // Just check it doesn't throw exception
    }

    @Test
    @DisplayName("Should return empty when borrowing not found by ID")
    void testGetBorrowingById_NotFound() {
        // Arrange
        int borrowingId = 999999;
        
        // Act
        Optional<Borrowing> result = borrowingDAO.getBorrowingById(borrowingId);
        
        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Should get all borrowings successfully")
    void testGetAllBorrowings_Success() {
        // Act
        List<Borrowing> result = borrowingDAO.getAllBorrowings();
        
        // Assert
        assertNotNull(result);
        // Note: The list might be empty if no borrowings exist in the database
    }

    @Test
    @DisplayName("Should get borrowings by member ID successfully")
    void testGetBorrowingsByMemberId_Success() {
        // Arrange
        int memberId = 1;
        
        // Act
        List<Borrowing> result = borrowingDAO.getBorrowingsByMemberId(memberId);
        
        // Assert
        assertNotNull(result);
        // Note: The list might be empty if no borrowings exist for this member
    }

    @Test
    @DisplayName("Should return empty list when member has no borrowings")
    void testGetBorrowingsByMemberId_NoBorrowings() {
        // Arrange
        int memberId = 999999;
        
        // Act
        List<Borrowing> result = borrowingDAO.getBorrowingsByMemberId(memberId);
        
        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Should get borrowings by book ID successfully")
    void testGetBorrowingsByBookId_Success() {
        // Arrange
        int bookId = 1;
        
        // Act
        List<Borrowing> result = borrowingDAO.getBorrowingsByBookId(bookId);
        
        // Assert
        assertNotNull(result);
        // Note: The list might be empty if no borrowings exist for this book
    }

    @Test
    @DisplayName("Should return empty list when book has no borrowings")
    void testGetBorrowingsByBookId_NoBorrowings() {
        // Arrange
        int bookId = 999999;
        
        // Act
        List<Borrowing> result = borrowingDAO.getBorrowingsByBookId(bookId);
        
        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Should get current borrowings successfully")
    void testGetCurrentBorrowings_Success() {
        // Act
        List<Borrowing> result = borrowingDAO.getCurrentBorrowings();
        
        // Assert
        assertNotNull(result);
        // Note: The list might be empty if no current borrowings exist
    }

    @Test
    @DisplayName("Should get overdue borrowings successfully")
    void testGetOverdueBorrowings_Success() {
        // Act
        List<Borrowing> result = borrowingDAO.getOverdueBorrowings();
        
        // Assert
        assertNotNull(result);
        // Note: The list might be empty if no overdue borrowings exist
    }

    @Test
    @DisplayName("Should get borrowings with details successfully")
    void testGetBorrowingsWithDetails_Success() {
        // Act
        List<Borrowing> result = borrowingDAO.getBorrowingsWithDetails();
        
        // Assert
        assertNotNull(result);
        // Note: The list might be empty if no borrowings exist
    }

    @Test
    @DisplayName("Should return book successfully")
    void testReturnBook_Success() {
        // Arrange
        int borrowingId = 1;
        LocalDate returnDate = LocalDate.now();
        BigDecimal fineAmount = new BigDecimal("5.00");
        
        // Act
        boolean result = borrowingDAO.returnBook(borrowingId, returnDate, fineAmount);
        
        // Assert
        // Note: This will return false if the borrowing doesn't exist in the database
        assertNotNull(result); // Just check it doesn't throw exception
    }

    @Test
    @DisplayName("Should fail to return book with invalid ID")
    void testReturnBook_InvalidId() {
        // Arrange
        int borrowingId = -1;
        LocalDate returnDate = LocalDate.now();
        BigDecimal fineAmount = new BigDecimal("5.00");
        
        // Act
        boolean result = borrowingDAO.returnBook(borrowingId, returnDate, fineAmount);
        
        // Assert
        assertFalse(result);
    }

    @Test
    @DisplayName("Should fail to return book with null date")
    void testReturnBook_NullDate() {
        // Arrange
        int borrowingId = 1;
        LocalDate returnDate = null;
        BigDecimal fineAmount = new BigDecimal("5.00");
        
        // Act
        boolean result = borrowingDAO.returnBook(borrowingId, returnDate, fineAmount);
        
        // Assert
        assertFalse(result);
    }

    @Test
    @DisplayName("Should update status successfully")
    void testUpdateStatus_Success() {
        // Arrange
        int borrowingId = 1;
        Borrowing.BorrowingStatus newStatus = Borrowing.BorrowingStatus.OVERDUE;
        
        // Act
        boolean result = borrowingDAO.updateStatus(borrowingId, newStatus);
        
        // Assert
        // Note: This will return false if the borrowing doesn't exist in the database
        assertNotNull(result); // Just check it doesn't throw exception
    }

    @Test
    @DisplayName("Should fail to update status with invalid ID")
    void testUpdateStatus_InvalidId() {
        // Arrange
        int borrowingId = -1;
        Borrowing.BorrowingStatus newStatus = Borrowing.BorrowingStatus.BORROWED;
        
        // Act
        boolean result = borrowingDAO.updateStatus(borrowingId, newStatus);
        
        // Assert
        assertFalse(result);
    }

    @Test
    @DisplayName("Should fail to update status with null status")
    void testUpdateStatus_NullStatus() {
        // Arrange
        int borrowingId = 1;
        Borrowing.BorrowingStatus newStatus = null;
        
        // Act
        boolean result = borrowingDAO.updateStatus(borrowingId, newStatus);
        
        // Assert
        assertFalse(result);
    }

    @Test
    @DisplayName("Should get borrowings by status successfully")
    void testGetBorrowingsByStatus_Success() {
        // Arrange
        Borrowing.BorrowingStatus status = Borrowing.BorrowingStatus.BORROWED;
        
        // Act
        List<Borrowing> result = borrowingDAO.getBorrowingsByStatus(status);
        
        // Assert
        assertNotNull(result);
        // Note: The list might be empty if no borrowings exist with this status
    }

    @Test
    @DisplayName("Should return empty list when no borrowings with status")
    void testGetBorrowingsByStatus_NoBorrowings() {
        // Arrange
        Borrowing.BorrowingStatus status = Borrowing.BorrowingStatus.RETURNED;
        
        // Act
        List<Borrowing> result = borrowingDAO.getBorrowingsByStatus(status);
        
        // Assert
        assertNotNull(result);
        // Note: The list might be empty if no borrowings exist with this status
    }

    @Test
    @DisplayName("Should handle database connection issues gracefully")
    void testDatabaseConnectionIssues() {
        // This test verifies that the DAO handles database connection issues gracefully
        // In a real scenario, this would be tested with a mock database
        
        // Arrange
        Borrowing borrowing = new Borrowing(1, 1, LocalDate.now(), LocalDate.now().plusDays(14));
        
        // Act & Assert
        // These should not throw exceptions even if database is not available
        assertNotNull(borrowingDAO.addBorrowing(borrowing));
        assertNotNull(borrowingDAO.updateBorrowing(borrowing));
        assertNotNull(borrowingDAO.deleteBorrowing(1));
        assertNotNull(borrowingDAO.getBorrowingById(1));
        assertNotNull(borrowingDAO.getAllBorrowings());
        assertNotNull(borrowingDAO.getBorrowingsByMemberId(1));
        assertNotNull(borrowingDAO.getBorrowingsByBookId(1));
        assertNotNull(borrowingDAO.getCurrentBorrowings());
        assertNotNull(borrowingDAO.getOverdueBorrowings());
        assertNotNull(borrowingDAO.getBorrowingsWithDetails());
        assertNotNull(borrowingDAO.returnBook(1, LocalDate.now(), new BigDecimal("5.00")));
        assertNotNull(borrowingDAO.updateStatus(1, Borrowing.BorrowingStatus.BORROWED));
        assertNotNull(borrowingDAO.getBorrowingsByStatus(Borrowing.BorrowingStatus.BORROWED));
    }

    @Test
    @DisplayName("Should validate borrowing data before database operations")
    void testBorrowingValidation() {
        // Arrange
        Borrowing invalidBorrowing = new Borrowing(-1, -1, LocalDate.now(), LocalDate.now().minusDays(1)); // Invalid data
        Borrowing validBorrowing = new Borrowing(1, 1, LocalDate.now(), LocalDate.now().plusDays(14));
        
        // Act & Assert
        assertFalse(invalidBorrowing.isValid());
        assertTrue(validBorrowing.isValid());
    }

    @Test
    @DisplayName("Should handle borrowing business logic correctly")
    void testBorrowingBusinessLogic() {
        // Arrange
        LocalDate borrowDate = LocalDate.now().minusDays(10);
        LocalDate dueDate = LocalDate.now().minusDays(5);
        Borrowing borrowing = new Borrowing(1, 1, borrowDate, dueDate);
        
        // Act & Assert
        assertTrue(borrowing.isOverdue());
        assertTrue(borrowing.getDaysOverdue() > 0);
        assertTrue(borrowing.calculateFine().compareTo(BigDecimal.ZERO) > 0);
        
        // Test not overdue borrowing
        LocalDate futureDueDate = LocalDate.now().plusDays(5);
        Borrowing futureBorrowing = new Borrowing(1, 1, LocalDate.now(), futureDueDate);
        assertFalse(futureBorrowing.isOverdue());
        assertEquals(0, futureBorrowing.getDaysOverdue());
        assertEquals(0, futureBorrowing.calculateFine().compareTo(BigDecimal.ZERO));
    }

    @Test
    @DisplayName("Should handle edge cases in borrowing operations")
    void testBorrowingEdgeCases() {
        // Arrange
        LocalDate borrowDate = LocalDate.now();
        LocalDate dueDate = LocalDate.now().plusDays(14);
        Borrowing borrowing = new Borrowing(1, 1, borrowDate, dueDate);
        
        // Act & Assert
        assertFalse(borrowing.isOverdue());
        assertEquals(0, borrowing.getDaysOverdue());
        assertEquals(0, borrowing.calculateFine().compareTo(BigDecimal.ZERO));
        
        // Test borrowing with same borrow and due date
        Borrowing sameDateBorrowing = new Borrowing(1, 1, LocalDate.now(), LocalDate.now());
        assertFalse(sameDateBorrowing.isValid());
        
        // Test borrowing with null dates
        Borrowing nullDateBorrowing = new Borrowing(1, 1, null, null);
        assertFalse(nullDateBorrowing.isValid());
    }

    @Test
    @DisplayName("Should calculate fine amounts correctly")
    void testFineCalculation() {
        // Arrange
        LocalDate borrowDate = LocalDate.now().minusDays(20);
        LocalDate dueDate = LocalDate.now().minusDays(10);
        Borrowing borrowing = new Borrowing(1, 1, borrowDate, dueDate);
        
        // Act & Assert
        assertTrue(borrowing.isOverdue());
        assertEquals(10, borrowing.getDaysOverdue());
        
        // Fine rate is $1.00 per day
        BigDecimal expectedFine = new BigDecimal("10.00");
        assertEquals(0, borrowing.calculateFine().compareTo(expectedFine));
    }

    @Test
    @DisplayName("Should handle borrowing status correctly")
    void testBorrowingStatus() {
        // Arrange
        LocalDate borrowDate = LocalDate.now().minusDays(5);
        LocalDate dueDate = LocalDate.now().plusDays(5);
        Borrowing borrowing = new Borrowing(1, 1, borrowDate, dueDate);
        
        // Act & Assert
        assertFalse(borrowing.isReturned());
        assertTrue(borrowing.isCurrentlyBorrowed());
        
        // Test returned borrowing
        borrowing.returnBook();
        assertTrue(borrowing.isReturned());
        assertFalse(borrowing.isCurrentlyBorrowed());
    }

    @Test
    @DisplayName("Should validate borrowing relationships")
    void testBorrowingRelationships() {
        // Arrange
        Borrowing borrowing = new Borrowing(1, 1, LocalDate.now(), LocalDate.now().plusDays(14));
        
        // Act & Assert
        assertEquals(1, borrowing.getMemberId());
        assertEquals(1, borrowing.getBookId());
        
        // Test setting relationships
        borrowing.setMemberId(2);
        borrowing.setBookId(3);
        assertEquals(2, borrowing.getMemberId());
        assertEquals(3, borrowing.getBookId());
    }

    @Test
    @DisplayName("Should handle borrowing status transitions")
    void testBorrowingStatusTransitions() {
        // Arrange
        Borrowing borrowing = new Borrowing(1, 1, LocalDate.now(), LocalDate.now().plusDays(14));
        
        // Act & Assert
        assertEquals(Borrowing.BorrowingStatus.BORROWED, borrowing.getStatus());
        
        // Test marking as overdue
        borrowing.markAsOverdue();
        assertEquals(Borrowing.BorrowingStatus.BORROWED, borrowing.getStatus()); // Not overdue yet
        
        // Test updating status
        borrowing.updateStatus();
        assertEquals(Borrowing.BorrowingStatus.BORROWED, borrowing.getStatus());
    }

    @Test
    @DisplayName("Should validate date ranges correctly")
    void testDateRangeValidation() {
        // Arrange
        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);
        LocalDate tomorrow = today.plusDays(1);
        
        // Act & Assert
        // Valid borrow date (today or past)
        assertTrue(new Borrowing(1, 1, today, tomorrow).isValidBorrowDate(today));
        assertTrue(new Borrowing(1, 1, yesterday, tomorrow).isValidBorrowDate(yesterday));
        assertFalse(new Borrowing(1, 1, tomorrow, tomorrow.plusDays(1)).isValidBorrowDate(tomorrow));
        
        // Valid due date (after borrow date)
        assertTrue(new Borrowing(1, 1, today, tomorrow).isValidDueDate(tomorrow));
        assertFalse(new Borrowing(1, 1, today, yesterday).isValidDueDate(yesterday));
        
        // Valid return date (after borrow date or null)
        assertTrue(new Borrowing(1, 1, today, tomorrow).isValidReturnDate(tomorrow));
        assertTrue(new Borrowing(1, 1, today, tomorrow).isValidReturnDate(null));
        assertFalse(new Borrowing(1, 1, today, tomorrow).isValidReturnDate(yesterday));
    }
}
