package com.library.model;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Model class representing a book in the library system.
 */
public class Book {
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
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.totalCopies = 1;
        this.availableCopies = 1;
    }

    // Full constructor
    public Book(String isbn, String title, String author, String publisher, 
                Integer publicationYear, String genre, int totalCopies) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.publicationYear = publicationYear;
        this.genre = genre;
        this.totalCopies = totalCopies;
        this.availableCopies = totalCopies;
    }

    // Getters and Setters
    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public Integer getPublicationYear() {
        return publicationYear;
    }

    public void setPublicationYear(Integer publicationYear) {
        this.publicationYear = publicationYear;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public int getTotalCopies() {
        return totalCopies;
    }

    public void setTotalCopies(int totalCopies) {
        this.totalCopies = totalCopies;
    }

    public int getAvailableCopies() {
        return availableCopies;
    }

    public void setAvailableCopies(int availableCopies) {
        this.availableCopies = availableCopies;
    }

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
        return availableCopies > 0;
    }

    public int getBorrowedCopies() {
        return totalCopies - availableCopies;
    }

    public boolean canBorrow() {
        return isAvailable();
    }

    public void borrow() {
        if (availableCopies > 0) {
            availableCopies--;
        } else {
            throw new IllegalStateException("No copies available for borrowing");
        }
    }

    public void returnBook() {
        if (availableCopies < totalCopies) {
            availableCopies++;
        } else {
            throw new IllegalStateException("All copies are already available");
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
        String cleanIsbn = isbn.replaceAll("[^0-9X]", "");
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
