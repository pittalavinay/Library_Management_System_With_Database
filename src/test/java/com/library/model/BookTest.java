package com.library.model;

import java.time.LocalDateTime;

/**
 * Simplified unit tests for Book model class that can run without JUnit dependencies.
 * These tests are designed to work with the SimpleTestRunner.
 */
public class BookTest {

    private Book validBook;
    private Book emptyBook;

    public void setUp() {
        validBook = new Book("9780132350884", "Clean Code", "Robert C. Martin", 
                           "Prentice Hall", 2008, "Programming", 3);
        emptyBook = new Book();
    }

    public void testValidBookCreation() {
        setUp();
        assertTrue(validBook != null, "Valid book should not be null");
        assertTrue("9780132350884".equals(validBook.getIsbn()), "ISBN should match");
        assertTrue("Clean Code".equals(validBook.getTitle()), "Title should match");
        assertTrue("Robert C. Martin".equals(validBook.getAuthor()), "Author should match");
        assertTrue("Prentice Hall".equals(validBook.getPublisher()), "Publisher should match");
        assertTrue(validBook.getPublicationYear() == 2008, "Publication year should match");
        assertTrue("Programming".equals(validBook.getGenre()), "Genre should match");
        assertTrue(validBook.getTotalCopies() == 3, "Total copies should match");
        assertTrue(validBook.getAvailableCopies() == 3, "Available copies should match");
    }

    public void testMinimalBookCreation() {
        Book book = new Book("9780201633610", "Design Patterns", "Erich Gamma");
        assertTrue("9780201633610".equals(book.getIsbn()), "ISBN should match");
        assertTrue("Design Patterns".equals(book.getTitle()), "Title should match");
        assertTrue("Erich Gamma".equals(book.getAuthor()), "Author should match");
        assertTrue(book.getTotalCopies() == 1, "Total copies should be 1");
        assertTrue(book.getAvailableCopies() == 1, "Available copies should be 1");
    }

    public void testBookAvailability() {
        setUp();
        assertTrue(validBook.isAvailable(), "Book should be available");
        assertTrue(validBook.canBorrow(), "Book should be borrowable");
        
        validBook.setAvailableCopies(0);
        assertTrue(!validBook.isAvailable(), "Book should not be available when no copies");
        assertTrue(!validBook.canBorrow(), "Book should not be borrowable when no copies");
    }

    public void testBookBorrowing() {
        setUp();
        int initialAvailable = validBook.getAvailableCopies();
        validBook.borrow();
        assertTrue(validBook.getAvailableCopies() == (initialAvailable - 1), "Available copies should decrease");
    }

    public void testBookBorrowingNoCopies() {
        setUp();
        validBook.setAvailableCopies(0);
        try {
            validBook.borrow();
            assertTrue(false, "Should throw exception when no copies available");
        } catch (IllegalStateException e) {
            // Expected exception
        }
    }

    public void testBookReturn() {
        setUp();
        validBook.setAvailableCopies(1);
        int initialAvailable = validBook.getAvailableCopies();
        validBook.returnBook();
        assertTrue(validBook.getAvailableCopies() == (initialAvailable + 1), "Available copies should increase");
    }

    public void testBookReturnAllCopiesAvailable() {
        setUp();
        validBook.setAvailableCopies(validBook.getTotalCopies());
        try {
            validBook.returnBook();
            assertTrue(false, "Should throw exception when all copies available");
        } catch (IllegalStateException e) {
            // Expected exception
        }
    }

    public void testBorrowedCopies() {
        setUp();
        assertTrue(validBook.getBorrowedCopies() == 0, "Borrowed copies should be 0 initially");
        validBook.setAvailableCopies(1);
        assertTrue(validBook.getBorrowedCopies() == 2, "Borrowed copies should be 2");
    }

    public void testBookValidation() {
        setUp();
        assertTrue(validBook.isValid(), "Valid book should be valid");
        assertTrue(validBook.isValidIsbn(), "Valid ISBN should be valid");
        assertTrue(validBook.isValidPublicationYear(), "Valid publication year should be valid");
    }

    public void testBookEquality() {
        setUp();
        Book book1 = new Book("9780132350884", "Clean Code", "Robert C. Martin");
        Book book2 = new Book("9780132350884", "Different Title", "Different Author");
        assertTrue(book1.equals(book2), "Books with same ISBN should be equal");
        assertTrue(book1.hashCode() == book2.hashCode(), "Equal books should have same hash code");
    }

    public void testBookToString() {
        setUp();
        String bookString = validBook.toString();
        assertTrue(bookString.contains("9780132350884"), "toString should contain ISBN");
        assertTrue(bookString.contains("Clean Code"), "toString should contain title");
        assertTrue(bookString.contains("3/3"), "toString should contain copies info");
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
