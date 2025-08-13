package com.library.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

/**
 * Model class representing a book borrowing transaction.
 */
public class Borrowing {
    private int borrowingId;
    private int bookId;
    private int memberId;
    private LocalDate borrowDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private BorrowingStatus status;
    private BigDecimal fineAmount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Reference objects (not stored in database)
    private Book book;
    private Member member;

    public enum BorrowingStatus {
        BORROWED, RETURNED, OVERDUE
    }

    // Default constructor
    public Borrowing() {
        this.status = BorrowingStatus.BORROWED;
        this.fineAmount = BigDecimal.ZERO;
    }

    // Constructor with essential fields
    public Borrowing(int bookId, int memberId, LocalDate borrowDate, LocalDate dueDate) {
        this();
        this.bookId = bookId;
        this.memberId = memberId;
        this.borrowDate = borrowDate;
        this.dueDate = dueDate;
    }

    // Full constructor
    public Borrowing(int bookId, int memberId, LocalDate borrowDate, LocalDate dueDate,
                     LocalDate returnDate, BorrowingStatus status, BigDecimal fineAmount) {
        this(bookId, memberId, borrowDate, dueDate);
        this.returnDate = returnDate;
        this.status = status;
        this.fineAmount = fineAmount != null ? fineAmount : BigDecimal.ZERO;
    }

    // Getters and Setters
    public int getBorrowingId() {
        return borrowingId;
    }

    public void setBorrowingId(int borrowingId) {
        this.borrowingId = borrowingId;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public LocalDate getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(LocalDate borrowDate) {
        this.borrowDate = borrowDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public BorrowingStatus getStatus() {
        return status;
    }

    public void setStatus(BorrowingStatus status) {
        this.status = status;
    }

    public BigDecimal getFineAmount() {
        return fineAmount;
    }

    public void setFineAmount(BigDecimal fineAmount) {
        this.fineAmount = fineAmount != null ? fineAmount : BigDecimal.ZERO;
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

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    // Business logic methods
    public boolean isOverdue() {
        if (returnDate != null) {
            return returnDate.isAfter(dueDate);
        }
        return LocalDate.now().isAfter(dueDate);
    }

    public boolean isReturned() {
        return status == BorrowingStatus.RETURNED && returnDate != null;
    }

    public boolean isCurrentlyBorrowed() {
        return status == BorrowingStatus.BORROWED || 
               (status == BorrowingStatus.OVERDUE && returnDate == null);
    }

    public long getDaysOverdue() {
        if (returnDate != null) {
            return Math.max(0, ChronoUnit.DAYS.between(dueDate, returnDate));
        } else if (isOverdue()) {
            return ChronoUnit.DAYS.between(dueDate, LocalDate.now());
        }
        return 0;
    }

    public long getDaysBorrowed() {
        LocalDate endDate = returnDate != null ? returnDate : LocalDate.now();
        return ChronoUnit.DAYS.between(borrowDate, endDate);
    }

    public BigDecimal calculateFine() {
        if (!isOverdue()) {
            return BigDecimal.ZERO;
        }
        
        long daysOverdue = getDaysOverdue();
        // Fine calculation: $1 per day overdue
        BigDecimal dailyFine = new BigDecimal("1.00");
        return dailyFine.multiply(BigDecimal.valueOf(daysOverdue));
    }

    public void returnBook() {
        this.returnDate = LocalDate.now();
        this.status = BorrowingStatus.RETURNED;
        this.fineAmount = calculateFine();
    }

    public void markAsOverdue() {
        if (isOverdue() && !isReturned()) {
            this.status = BorrowingStatus.OVERDUE;
        }
    }

    public void updateStatus() {
        if (isReturned()) {
            this.status = BorrowingStatus.RETURNED;
        } else if (isOverdue()) {
            this.status = BorrowingStatus.OVERDUE;
        } else {
            this.status = BorrowingStatus.BORROWED;
        }
    }

    // Validation methods
    public boolean isValid() {
        return bookId > 0 && memberId > 0 &&
               borrowDate != null && dueDate != null &&
               borrowDate.isBefore(dueDate) &&
               (returnDate == null || returnDate.isAfter(borrowDate)) &&
               fineAmount != null && fineAmount.compareTo(BigDecimal.ZERO) >= 0;
    }

    public boolean isValidBorrowDate(LocalDate borrowDate) {
        return borrowDate != null && !borrowDate.isAfter(LocalDate.now());
    }

    public boolean isValidDueDate(LocalDate dueDate) {
        return dueDate != null && dueDate.isAfter(borrowDate);
    }

    public boolean isValidReturnDate(LocalDate returnDate) {
        return returnDate == null || returnDate.isAfter(borrowDate);
    }

    // Override methods
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Borrowing borrowing = (Borrowing) obj;
        return borrowingId == borrowing.borrowingId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(borrowingId);
    }

    @Override
    public String toString() {
        return String.format("Borrowing{borrowingId=%d, bookId=%d, memberId=%d, " +
                           "borrowDate=%s, dueDate=%s, status=%s, fineAmount=%s}", 
                           borrowingId, bookId, memberId, borrowDate, dueDate, 
                           status, fineAmount);
    }
}
