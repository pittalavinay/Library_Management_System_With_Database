package com.library.model;

import java.lang.reflect.Method;

/**
 * Security-focused tests for the Book class to verify all fixes are working.
 * These tests are designed to work with the SimpleTestRunner without JUnit dependencies.
 */
public class BookSecurityTest {

    private Book book;

    public void setUp() {
        book = new Book("1234567890", "Test Book", "Test Author");
    }

    // Test 1: Verify inventory manipulation vulnerability is fixed
    public void testInventoryManipulationVulnerabilityFixed() {
        setUp();
        // Arrange
        book.setTotalCopies(5);
        book.borrow(); // availableCopies = 4
        
        // Act & Assert: The dangerous setAvailableCopies method should not exist
        try {
            Method setAvailableCopiesMethod = book.getClass().getMethod("setAvailableCopies", int.class);
            assertTrue(false, "setAvailableCopies method should not exist to prevent inventory manipulation");
        } catch (NoSuchMethodException e) {
            // Expected - method should not exist
        }
        
        // Verify business logic integrity
        assertTrue(book.getTotalCopies() == 5, "Total copies should be 5");
        assertTrue(book.getAvailableCopies() == 4, "Available copies should be 4");
        assertTrue(book.getAvailableCopies() <= book.getTotalCopies(), "Available copies should not exceed total copies");
    }

    // Test 2: Verify negative copies vulnerability is fixed
    public void testNegativeCopiesVulnerabilityFixed() {
        setUp();
        // Act & Assert: Should throw exception for negative total copies
        try {
            book.setTotalCopies(-5);
            assertTrue(false, "Should not allow negative total copies");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
        
        // Verify the book state remains unchanged
        assertTrue(book.getTotalCopies() == 1, "Total copies should remain 1");
        assertTrue(book.getAvailableCopies() == 1, "Available copies should remain 1");
    }

    // Test 3: Verify constructor validation
    public void testConstructorValidation() {
        // Test null ISBN
        try {
            new Book(null, "Title", "Author");
            assertTrue(false, "Should not allow null ISBN");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
        
        // Test empty ISBN
        try {
            new Book("", "Title", "Author");
            assertTrue(false, "Should not allow empty ISBN");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
        
        // Test null title
        try {
            new Book("1234567890", null, "Author");
            assertTrue(false, "Should not allow null title");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
        
        // Test null author
        try {
            new Book("1234567890", "Title", null);
            assertTrue(false, "Should not allow null author");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    // Test 4: Verify setter validation
    public void testSetterValidation() {
        setUp();
        // Test setTotalCopies validation
        try {
            book.setTotalCopies(-1);
            assertTrue(false, "Should not allow negative total copies");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
        
        // Test setTotalCopies with available copies constraint
        book.setTotalCopies(5);
        book.borrow(); // availableCopies = 4
        try {
            book.setTotalCopies(3); // Less than available copies
            assertTrue(false, "Should not allow total copies less than available copies");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
        
        // Test setBookId validation
        try {
            book.setBookId(-1);
            assertTrue(false, "Should not allow negative book ID");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
        
        // Test setPublicationYear validation
        try {
            book.setPublicationYear(1799);
            assertTrue(false, "Should not allow publication year before 1800");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
        
        try {
            book.setPublicationYear(java.time.Year.now().getValue() + 1);
            assertTrue(false, "Should not allow future publication year");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    // Test 5: Verify thread safety
    public void testThreadSafety() {
        setUp();
        // Arrange
        book.setTotalCopies(100);
        final int numThreads = 10;
        final int operationsPerThread = 100;
        Thread[] threads = new Thread[numThreads];
        
        // Act: Multiple threads borrowing and returning books
        for (int i = 0; i < numThreads; i++) {
            final int threadId = i;
            threads[i] = new Thread(() -> {
                for (int j = 0; j < operationsPerThread; j++) {
                    try {
                        book.borrow();
                        Thread.sleep(1); // Small delay to increase race condition chance
                        book.returnBook();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            });
            threads[i].start();
        }
        
        // Wait for all threads to complete
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        
        // Assert: Final state should be consistent
        assertTrue(book.getTotalCopies() == 100, "Total copies should be 100");
        assertTrue(book.getAvailableCopies() == 100, "Available copies should be 100");
        assertTrue(book.getBorrowedCopies() == 0, "Borrowed copies should be 0");
    }

    // Test 6: Verify input sanitization
    public void testInputSanitization() {
        // Test whitespace trimming
        Book bookWithWhitespace = new Book("  1234567890  ", "  Test Book  ", "  Test Author  ");
        assertTrue("1234567890".equals(bookWithWhitespace.getIsbn()), "ISBN should be trimmed");
        assertTrue("Test Book".equals(bookWithWhitespace.getTitle()), "Title should be trimmed");
        assertTrue("Test Author".equals(bookWithWhitespace.getAuthor()), "Author should be trimmed");
        
        // Test setter trimming
        book.setPublisher("  Test Publisher  ");
        assertTrue("Test Publisher".equals(book.getPublisher()), "Publisher should be trimmed");
        
        book.setGenre("  Fiction  ");
        assertTrue("Fiction".equals(book.getGenre()), "Genre should be trimmed");
    }

    // Test 7: Verify business logic integrity
    public void testBusinessLogicIntegrity() {
        setUp();
        // Test borrowing logic
        book.setTotalCopies(3);
        assertTrue(book.getAvailableCopies() == 3, "Initial available copies should be 3");
        
        book.borrow();
        assertTrue(book.getAvailableCopies() == 2, "Available copies should be 2 after borrow");
        assertTrue(book.getBorrowedCopies() == 1, "Borrowed copies should be 1");
        
        book.borrow();
        assertTrue(book.getAvailableCopies() == 1, "Available copies should be 1 after second borrow");
        assertTrue(book.getBorrowedCopies() == 2, "Borrowed copies should be 2");
        
        book.borrow();
        assertTrue(book.getAvailableCopies() == 0, "Available copies should be 0 after third borrow");
        assertTrue(book.getBorrowedCopies() == 3, "Borrowed copies should be 3");
        
        // Test borrowing when no copies available
        try {
            book.borrow();
            assertTrue(false, "Should not allow borrowing when no copies available");
        } catch (IllegalStateException e) {
            // Expected exception
        }
        
        // Test returning logic
        book.returnBook();
        assertTrue(book.getAvailableCopies() == 1, "Available copies should be 1 after return");
        assertTrue(book.getBorrowedCopies() == 2, "Borrowed copies should be 2");
        
        // Test returning when all copies are available
        book.returnBook();
        book.returnBook();
        assertTrue(book.getAvailableCopies() == 3, "Available copies should be 3");
        assertTrue(book.getBorrowedCopies() == 0, "Borrowed copies should be 0");
        
        try {
            book.returnBook();
            assertTrue(false, "Should not allow returning when all copies are available");
        } catch (IllegalStateException e) {
            // Expected exception
        }
    }

    // Test 8: Verify performance improvement (pre-compiled patterns)
    public void testPerformanceImprovement() {
        setUp();
        // Test ISBN validation performance
        long startTime = System.nanoTime();
        for (int i = 0; i < 10000; i++) {
            book.isValidIsbn();
        }
        long endTime = System.nanoTime();
        long duration = endTime - startTime;
        
        // The test should complete quickly due to pre-compiled patterns
        assertTrue(duration < 10000000, "ISBN validation should be fast with pre-compiled patterns");
    }

    // Test 9: Verify data consistency
    public void testDataConsistency() {
        setUp();
        // Test that available copies never exceed total copies
        book.setTotalCopies(5);
        book.borrow();
        book.borrow();
        
        assertTrue(book.getTotalCopies() == 5, "Total copies should be 5");
        assertTrue(book.getAvailableCopies() == 3, "Available copies should be 3");
        assertTrue(book.getBorrowedCopies() == 2, "Borrowed copies should be 2");
        
        // Verify the relationship always holds
        assertTrue(book.getAvailableCopies() <= book.getTotalCopies(), "Available copies should not exceed total copies");
        assertTrue(book.getBorrowedCopies() <= book.getTotalCopies(), "Borrowed copies should not exceed total copies");
        assertTrue(book.getTotalCopies() == (book.getAvailableCopies() + book.getBorrowedCopies()), "Total should equal available + borrowed");
    }

    // Test 10: Verify edge cases
    public void testEdgeCases() {
        // Test with minimum valid values
        Book minimalBook = new Book("1234567890", "A", "B");
        assertTrue(minimalBook.getTotalCopies() == 1, "Total copies should be 1");
        assertTrue(minimalBook.getAvailableCopies() == 1, "Available copies should be 1");
        
        // Test with maximum publication year
        int currentYear = java.time.Year.now().getValue();
        book.setPublicationYear(currentYear);
        assertTrue(book.getPublicationYear() == currentYear, "Publication year should be set correctly");
        
        // Test with null optional fields
        book.setPublisher(null);
        book.setGenre(null);
        book.setPublicationYear(null);
        
        assertTrue(book.getPublisher() == null, "Publisher should be null");
        assertTrue(book.getGenre() == null, "Genre should be null");
        assertTrue(book.getPublicationYear() == null, "Publication year should be null");
    }

    // Helper method for assertions
    private void assertTrue(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }
}
