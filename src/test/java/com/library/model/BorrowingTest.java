package com.library.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Simplified unit tests for Borrowing model class that can run without JUnit dependencies.
 * These tests are designed to work with the SimpleTestRunner.
 */
public class BorrowingTest {

    private Borrowing validBorrowing;
    private Borrowing emptyBorrowing;

    public void setUp() {
        validBorrowing = new Borrowing(1, 1, LocalDate.now(), LocalDate.now().plusDays(14));
        emptyBorrowing = new Borrowing();
    }

    public void testValidBorrowingCreation() {
        setUp();
        assertTrue(validBorrowing != null, "Valid borrowing should not be null");
        assertTrue(validBorrowing.getBookId() == 1, "Book ID should match");
        assertTrue(validBorrowing.getMemberId() == 1, "Member ID should match");
        assertTrue(LocalDate.now().equals(validBorrowing.getBorrowDate()), "Borrow date should match");
        assertTrue(LocalDate.now().plusDays(14).equals(validBorrowing.getDueDate()), "Due date should match");
        assertTrue(Borrowing.BorrowingStatus.BORROWED.equals(validBorrowing.getStatus()), "Status should be BORROWED");
        assertTrue(BigDecimal.ZERO.equals(validBorrowing.getFineAmount()), "Fine amount should be zero");
    }

    public void testFullBorrowingCreation() {
        LocalDate borrowDate = LocalDate.now();
        LocalDate dueDate = borrowDate.plusDays(14);
        LocalDate returnDate = borrowDate.plusDays(10);
        BigDecimal fineAmount = new BigDecimal("5.00");
        
        Borrowing borrowing = new Borrowing(1, 1, borrowDate, dueDate, returnDate, 
                                          Borrowing.BorrowingStatus.RETURNED, fineAmount);
        
        assertTrue(borrowing.getBookId() == 1, "Book ID should match");
        assertTrue(borrowing.getMemberId() == 1, "Member ID should match");
        assertTrue(borrowDate.equals(borrowing.getBorrowDate()), "Borrow date should match");
        assertTrue(dueDate.equals(borrowing.getDueDate()), "Due date should match");
        assertTrue(returnDate.equals(borrowing.getReturnDate()), "Return date should match");
        assertTrue(Borrowing.BorrowingStatus.RETURNED.equals(borrowing.getStatus()), "Status should be RETURNED");
        assertTrue(fineAmount.equals(borrowing.getFineAmount()), "Fine amount should match");
    }

    public void testBorrowingOverdueDetection() {
        setUp();
        // Not overdue
        assertTrue(!validBorrowing.isOverdue(), "Borrowing should not be overdue initially");
        
        // Overdue
        validBorrowing.setDueDate(LocalDate.now().minusDays(1));
        assertTrue(validBorrowing.isOverdue(), "Borrowing should be overdue");
        
        // Returned on time
        validBorrowing.setReturnDate(LocalDate.now().minusDays(2));
        validBorrowing.setDueDate(LocalDate.now().minusDays(1));
        assertTrue(!validBorrowing.isOverdue(), "Returned borrowing should not be overdue");
        
        // Returned late
        validBorrowing.setReturnDate(LocalDate.now());
        validBorrowing.setDueDate(LocalDate.now().minusDays(1));
        assertTrue(validBorrowing.isOverdue(), "Late returned borrowing should be overdue");
    }

    public void testBorrowingReturnedStatus() {
        setUp();
        assertTrue(!validBorrowing.isReturned(), "Borrowing should not be returned initially");
        
        validBorrowing.setStatus(Borrowing.BorrowingStatus.RETURNED);
        validBorrowing.setReturnDate(LocalDate.now());
        assertTrue(validBorrowing.isReturned(), "Borrowing should be returned");
        
        validBorrowing.setReturnDate(null);
        assertTrue(!validBorrowing.isReturned(), "Borrowing without return date should not be returned");
    }

    public void testBorrowingCurrentlyBorrowedStatus() {
        setUp();
        assertTrue(validBorrowing.isCurrentlyBorrowed(), "Borrowing should be currently borrowed initially");
        
        validBorrowing.setStatus(Borrowing.BorrowingStatus.RETURNED);
        assertTrue(!validBorrowing.isCurrentlyBorrowed(), "Returned borrowing should not be currently borrowed");
        
        validBorrowing.setStatus(Borrowing.BorrowingStatus.OVERDUE);
        assertTrue(validBorrowing.isCurrentlyBorrowed(), "Overdue borrowing should be currently borrowed");
    }

    public void testBorrowingDaysOverdue() {
        setUp();
        assertTrue(validBorrowing.getDaysOverdue() == 0, "Days overdue should be 0 initially");
        
        validBorrowing.setDueDate(LocalDate.now().minusDays(5));
        assertTrue(validBorrowing.getDaysOverdue() == 5, "Days overdue should be 5");
        
        validBorrowing.setReturnDate(LocalDate.now().minusDays(2));
        // When returned, days overdue is calculated between due date and return date
        // If due date is 5 days ago and return date is 2 days ago, that's 3 days overdue
        assertTrue(validBorrowing.getDaysOverdue() == 3, "Returned borrowing should have correct days overdue calculation");
    }

    public void testBorrowingFineCalculation() {
        setUp();
        assertTrue(BigDecimal.ZERO.equals(validBorrowing.calculateFine()), "Fine should be zero initially");
        
        // Set overdue
        validBorrowing.setDueDate(LocalDate.now().minusDays(10));
        BigDecimal fine = validBorrowing.calculateFine();
        assertTrue(fine.compareTo(BigDecimal.ZERO) > 0, "Overdue borrowing should have positive fine");
        
        // Return the book
        validBorrowing.setReturnDate(LocalDate.now().minusDays(5));
        validBorrowing.setDueDate(LocalDate.now().minusDays(10));
        BigDecimal finalFine = validBorrowing.calculateFine();
        assertTrue(finalFine.compareTo(BigDecimal.ZERO) > 0, "Returned overdue borrowing should still have fine");
    }

    public void testBorrowingValidation() {
        setUp();
        assertTrue(validBorrowing.isValid(), "Valid borrowing should be valid");
        
        // Test invalid borrowing
        Borrowing invalidBorrowing = new Borrowing(-1, -1, LocalDate.now(), LocalDate.now().minusDays(1));
        assertTrue(!invalidBorrowing.isValid(), "Invalid borrowing should not be valid");
    }

    public void testBorrowingEquality() {
        setUp();
        Borrowing borrowing1 = new Borrowing(1, 1, LocalDate.now(), LocalDate.now().plusDays(14));
        Borrowing borrowing2 = new Borrowing(1, 1, LocalDate.now(), LocalDate.now().plusDays(14));
        
        assertTrue(borrowing1.equals(borrowing2), "Equal borrowings should be equal");
        assertTrue(borrowing1.hashCode() == borrowing2.hashCode(), "Equal borrowings should have same hash code");
    }

    public void testBorrowingToString() {
        setUp();
        String borrowingString = validBorrowing.toString();
        assertTrue(borrowingString.contains("1"), "toString should contain book ID");
        assertTrue(borrowingString.contains("1"), "toString should contain member ID");
        assertTrue(borrowingString.contains("BORROWED"), "toString should contain status");
    }

    public void testBorrowingSettersAndGetters() {
        setUp();
        LocalDate newReturnDate = LocalDate.now().plusDays(5);
        BigDecimal newFine = new BigDecimal("10.00");
        
        validBorrowing.setReturnDate(newReturnDate);
        validBorrowing.setFineAmount(newFine);
        validBorrowing.setStatus(Borrowing.BorrowingStatus.RETURNED);
        
        assertTrue(newReturnDate.equals(validBorrowing.getReturnDate()), "Return date should be updated");
        assertTrue(newFine.equals(validBorrowing.getFineAmount()), "Fine amount should be updated");
        assertTrue(Borrowing.BorrowingStatus.RETURNED.equals(validBorrowing.getStatus()), "Status should be updated");
    }

    // Helper method for assertions
    private void assertTrue(boolean condition) {
        if (!condition) {
            throw new AssertionError("Assertion failed");
        }
    }

    private void assertTrue(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }
}
