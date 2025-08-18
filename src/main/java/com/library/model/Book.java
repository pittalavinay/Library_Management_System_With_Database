package com.library.model;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Model class representing a book in the library system.
 * This class is thread-safe and includes comprehensive validation.
 */
public class Book {
    // Pre-compiled patterns for performance
    private static final Pattern ISBN_PATTERN = Pattern.compile("[^0-9X]");
    private static final Pattern WHITESPACE_PATTERN = Pattern.compile("\\s+");
    
    // Thread safety lock for copy operations
    private final Object copyLock = new Object();
    
    private int bookId;
    private String isbn;
    private String title;
    private String author;
    private String publisher;
    private Integer publicationYear;
    private String genre;
    private int totalCopies;
    private int availableCopies;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Default constructor
    public Book() {}

    // Constructor with essential fields
    public Book(String isbn, String title, String author) {
        validateRequiredFields(isbn, title, author);
        this.isbn = isbn.trim();
        this.title = title.trim();
        this.author = author.trim();
        this.totalCopies = 1;
        this.availableCopies = 1;
    }

    // Full constructor
    public Book(String isbn, String title, String author, String publisher, 
                Integer publicationYear, String genre, int totalCopies) {
        validateRequiredFields(isbn, title, author);
        validateTotalCopies(totalCopies);
        
        this.isbn = isbn.trim();
        this.title = title.trim();
        this.author = author.trim();
        this.publisher = publisher != null ? publisher.trim() : null;
        this.publicationYear = publicationYear;
        this.genre = genre != null ? genre.trim() : null;
        this.totalCopies = totalCopies;
        this.availableCopies = totalCopies;
    }

    // Full constructor with available copies
    public Book(String isbn, String title, String author, String publisher,
                Integer publicationYear, String genre, int totalCopies, int availableCopies) {
        validateRequiredFields(isbn, title, author);
        validateTotalCopies(totalCopies);
        this.isbn = isbn.trim();
        this.title = title.trim();
        this.author = author.trim();
        this.publisher = publisher != null ? publisher.trim() : null;
        this.publicationYear = publicationYear;
        this.genre = genre != null ? genre.trim() : null;
        this.totalCopies = totalCopies;
        validateAvailableCopies(availableCopies);
        this.availableCopies = availableCopies;
    }

    // Validation methods
    private void validateRequiredFields(String isbn, String title, String author) {
        if (isbn == null || isbn.trim().isEmpty()) {
            throw new IllegalArgumentException("ISBN cannot be null or empty");
        }
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be null or empty");
        }
        if (author == null || author.trim().isEmpty()) {
            throw new IllegalArgumentException("Author cannot be null or empty");
        }
    }

    private void validateTotalCopies(int totalCopies) {
        if (totalCopies < 0) {
            throw new IllegalArgumentException("Total copies cannot be negative");
        }
    }

    private void validateAvailableCopies(int availableCopies) {
        if (availableCopies < 0) {
            throw new IllegalArgumentException("Available copies cannot be negative");
        }
        if (availableCopies > totalCopies) {
            throw new IllegalArgumentException("Available copies cannot exceed total copies");
        }
    }

    // Getters and Setters
    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        if (bookId < 0) {
            throw new IllegalArgumentException("Book ID cannot be negative");
        }
        this.bookId = bookId;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        if (isbn == null || isbn.trim().isEmpty()) {
            throw new IllegalArgumentException("ISBN cannot be null or empty");
        }
        this.isbn = isbn.trim();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be null or empty");
        }
        this.title = title.trim();
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        if (author == null || author.trim().isEmpty()) {
            throw new IllegalArgumentException("Author cannot be null or empty");
        }
        this.author = author.trim();
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher != null ? publisher.trim() : null;
    }

    public Integer getPublicationYear() {
        return publicationYear;
    }

    public void setPublicationYear(Integer publicationYear) {
        if (publicationYear != null && (publicationYear < 1800 || publicationYear > java.time.Year.now().getValue())) {
            throw new IllegalArgumentException("Publication year must be between 1800 and current year");
        }
        this.publicationYear = publicationYear;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre != null ? genre.trim() : null;
    }

    public int getTotalCopies() {
        return totalCopies;
    }

    public void setTotalCopies(int totalCopies) {
        validateTotalCopies(totalCopies);
        synchronized(copyLock) {
            if (totalCopies < this.availableCopies) {
                throw new IllegalArgumentException("Total copies cannot be less than available copies");
            }
            this.totalCopies = totalCopies;
        }
    }

    public int getAvailableCopies() {
        synchronized(copyLock) {
            return availableCopies;
        }
    }

    // REMOVED: setAvailableCopies() method to prevent inventory manipulation

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    // Business logic methods
    public boolean isAvailable() {
        synchronized(copyLock) {
            return availableCopies > 0;
        }
    }

    public int getBorrowedCopies() {
        synchronized(copyLock) {
            return totalCopies - availableCopies;
        }
    }

    public boolean canBorrow() {
        return isAvailable();
    }

    public void borrow() {
        synchronized(copyLock) {
            if (availableCopies > 0) {
                availableCopies--;
            } else {
                throw new IllegalStateException("No copies available for borrowing");
            }
        }
    }

    public void returnBook() {
        synchronized(copyLock) {
            if (availableCopies < totalCopies) {
                availableCopies++;
            } else {
                throw new IllegalStateException("All copies are already available");
            }
        }
    }

    // Validation methods
    public boolean isValid() {
        return isbn != null && !isbn.trim().isEmpty() &&
               title != null && !title.trim().isEmpty() &&
               author != null && !author.trim().isEmpty() &&
               totalCopies >= 0 && availableCopies >= 0 &&
               availableCopies <= totalCopies;
    }

    public boolean isValidIsbn() {
        if (isbn == null || isbn.trim().isEmpty()) {
            return false;
        }
        // Basic ISBN validation (10 or 13 digits)
        String cleanIsbn = ISBN_PATTERN.matcher(isbn).replaceAll("");
        return cleanIsbn.length() == 10 || cleanIsbn.length() == 13;
    }

    public boolean isValidPublicationYear() {
        if (publicationYear == null) {
            return true; // Optional field
        }
        int currentYear = java.time.Year.now().getValue();
        return publicationYear >= 1800 && publicationYear <= currentYear;
    }

    // Override methods
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Book book = (Book) obj;
        return Objects.equals(isbn, book.isbn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(isbn);
    }

    @Override
    public String toString() {
        return String.format("Book{bookId=%d, isbn='%s', title='%s', author='%s', " +
                           "availableCopies=%d/%d}", 
                           bookId, isbn, title, author, availableCopies, totalCopies);
    }
}
