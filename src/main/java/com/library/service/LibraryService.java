package com.library.service;

import com.library.dao.BookDAO;
import com.library.dao.MemberDAO;
import com.library.dao.BorrowingDAO;
import com.library.model.Book;
import com.library.model.Member;
import com.library.model.Borrowing;
import com.library.util.DatabaseConnection;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Main service class for library operations.
 */
public class LibraryService {
    
    private final BookDAO bookDAO;
    private final MemberDAO memberDAO;
    private final BorrowingDAO borrowingDAO;
    
    // Constants
    private static final int DEFAULT_BORROW_DAYS = 14;
    private static final BigDecimal DAILY_FINE_RATE = new BigDecimal("1.00");
    private static final int MAX_BOOKS_PER_MEMBER = 5;

    public LibraryService() {
        this.bookDAO = new BookDAO();
        this.memberDAO = new MemberDAO();
        this.borrowingDAO = new BorrowingDAO();
    }

    // ==================== BOOK MANAGEMENT ====================

    /**
     * Add a new book to the library.
     * 
     * @param book Book to add
     * @return true if successful, false otherwise
     */
    public boolean addBook(Book book) {
        if (book == null || !book.isValid()) {
            System.err.println("Invalid book data provided");
            return false;
        }

        // Check if ISBN already exists
        if (bookDAO.getBookByIsbn(book.getIsbn()).isPresent()) {
            System.err.println("Book with ISBN " + book.getIsbn() + " already exists");
            return false;
        }

        return bookDAO.addBook(book);
    }

    /**
     * Get book by ID.
     * 
     * @param bookId Book ID
     * @return Optional containing book if found
     */
    public Optional<Book> getBookById(int bookId) {
        return bookDAO.getBookById(bookId);
    }

    /**
     * Get book by ISBN.
     * 
     * @param isbn ISBN
     * @return Optional containing book if found
     */
    public Optional<Book> getBookByIsbn(String isbn) {
        return bookDAO.getBookByIsbn(isbn);
    }

    /**
     * Get all books.
     * 
     * @return List of all books
     */
    public List<Book> getAllBooks() {
        return bookDAO.getAllBooks();
    }

    /**
     * Update an existing book.
     * 
     * @param book Book to update
     * @return true if successful, false otherwise
     */
    public boolean updateBook(Book book) {
        if (book == null || !book.isValid() || book.getBookId() <= 0) {
            System.err.println("Invalid book data for update");
            return false;
        }

        // Check if book exists
        Optional<Book> existingBook = bookDAO.getBookById(book.getBookId());
        if (existingBook.isEmpty()) {
            System.err.println("Book with ID " + book.getBookId() + " not found");
            return false;
        }

        return bookDAO.updateBook(book);
    }

    /**
     * Delete a book by ID.
     * 
     * @param bookId Book ID to delete
     * @return true if successful, false otherwise
     */
    public boolean deleteBook(int bookId) {
        // Check if book exists
        Optional<Book> book = bookDAO.getBookById(bookId);
        if (book.isEmpty()) {
            System.err.println("Book with ID " + bookId + " not found");
            return false;
        }

        // Check if book is currently borrowed
        List<Borrowing> borrowings = borrowingDAO.getBorrowingsByBookId(bookId);
        boolean hasActiveBorrowings = borrowings.stream()
                .anyMatch(b -> b.isCurrentlyBorrowed());

        if (hasActiveBorrowings) {
            System.err.println("Cannot delete book with ID " + bookId + " - it has active borrowings");
            return false;
        }

        return bookDAO.deleteBook(bookId);
    }

    /**
     * Search books by title.
     * 
     * @param title Title to search for
     * @return List of matching books
     */
    public List<Book> searchBooksByTitle(String title) {
        return bookDAO.searchByTitle(title);
    }

    /**
     * Search books by author.
     * 
     * @param author Author to search for
     * @return List of matching books
     */
    public List<Book> searchBooksByAuthor(String author) {
        return bookDAO.searchByAuthor(author);
    }

    /**
     * Search books by genre.
     * 
     * @param genre Genre to search for
     * @return List of matching books
     */
    public List<Book> searchBooksByGenre(String genre) {
        return bookDAO.searchByGenre(genre);
    }

    /**
     * Get all available books.
     * 
     * @return List of available books
     */
    public List<Book> getAvailableBooks() {
        return bookDAO.getAvailableBooks();
    }

    // ==================== MEMBER MANAGEMENT ====================

    /**
     * Register a new member.
     * 
     * @param member Member to register
     * @return true if successful, false otherwise
     */
    public boolean registerMember(Member member) {
        if (member == null || !member.isValid()) {
            System.err.println("Invalid member data provided");
            return false;
        }

        // Check if member code already exists
        if (memberDAO.isMemberCodeExists(member.getMemberCode())) {
            System.err.println("Member with code " + member.getMemberCode() + " already exists");
            return false;
        }

        // Check if email already exists
        if (memberDAO.isEmailExists(member.getEmail())) {
            System.err.println("Member with email " + member.getEmail() + " already exists");
            return false;
        }

        return memberDAO.addMember(member);
    }

    /**
     * Get member by ID.
     * 
     * @param memberId Member ID
     * @return Optional containing member if found
     */
    public Optional<Member> getMemberById(int memberId) {
        return memberDAO.getMemberById(memberId);
    }

    /**
     * Get member by member code.
     * 
     * @param memberCode Member code
     * @return Optional containing member if found
     */
    public Optional<Member> getMemberByCode(String memberCode) {
        return memberDAO.getMemberByCode(memberCode);
    }

    /**
     * Get member by email.
     * 
     * @param email Email address
     * @return Optional containing member if found
     */
    public Optional<Member> getMemberByEmail(String email) {
        return memberDAO.getMemberByEmail(email);
    }

    /**
     * Get all members.
     * 
     * @return List of all members
     */
    public List<Member> getAllMembers() {
        return memberDAO.getAllMembers();
    }

    /**
     * Update member details.
     * 
     * @param member Member to update
     * @return true if successful, false otherwise
     */
    public boolean updateMember(Member member) {
        if (member == null || !member.isValid() || member.getMemberId() <= 0) {
            System.err.println("Invalid member data for update");
            return false;
        }

        // Check if member exists
        Optional<Member> existingMember = memberDAO.getMemberById(member.getMemberId());
        if (existingMember.isEmpty()) {
            System.err.println("Member with ID " + member.getMemberId() + " not found");
            return false;
        }

        return memberDAO.updateMember(member);
    }

    /**
     * Delete a member by ID.
     * 
     * @param memberId Member ID to delete
     * @return true if successful, false otherwise
     */
    public boolean deleteMember(int memberId) {
        // Check if member exists
        Optional<Member> member = memberDAO.getMemberById(memberId);
        if (member.isEmpty()) {
            System.err.println("Member with ID " + memberId + " not found");
            return false;
        }

        // Check if member has active borrowings
        List<Borrowing> borrowings = borrowingDAO.getBorrowingsByMemberId(memberId);
        boolean hasActiveBorrowings = borrowings.stream()
                .anyMatch(b -> b.isCurrentlyBorrowed());

        if (hasActiveBorrowings) {
            System.err.println("Cannot delete member with ID " + memberId + " - has active borrowings");
            return false;
        }

        return memberDAO.deleteMember(memberId);
    }

    /**
     * Search members by name.
     * 
     * @param name Name to search for
     * @return List of matching members
     */
    public List<Member> searchMembersByName(String name) {
        return memberDAO.searchByName(name);
    }

    /**
     * Get all active members.
     * 
     * @return List of active members
     */
    public List<Member> getActiveMembers() {
        return memberDAO.getActiveMembers();
    }

    /**
     * Update member status.
     * 
     * @param memberId Member ID
     * @param status New status
     * @return true if successful, false otherwise
     */
    public boolean updateMemberStatus(int memberId, Member.MembershipStatus status) {
        return memberDAO.updateMembershipStatus(memberId, status);
    }

    // ==================== BORROWING MANAGEMENT ====================

    /**
     * Borrow a book.
     * 
     * @param bookId Book ID
     * @param memberId Member ID
     * @return true if successful, false otherwise
     */
    public boolean borrowBook(int bookId, int memberId) {
        // Validate book
        Optional<Book> bookOpt = bookDAO.getBookById(bookId);
        if (bookOpt.isEmpty()) {
            System.err.println("Book with ID " + bookId + " not found");
            return false;
        }
        Book book = bookOpt.get();

        if (!book.isAvailable()) {
            System.err.println("Book '" + book.getTitle() + "' is not available for borrowing");
            return false;
        }

        // Validate member
        Optional<Member> memberOpt = memberDAO.getMemberById(memberId);
        if (memberOpt.isEmpty()) {
            System.err.println("Member with ID " + memberId + " not found");
            return false;
        }
        Member member = memberOpt.get();

        if (!member.isActive()) {
            System.err.println("Member '" + member.getFullName() + "' is not active");
            return false;
        }

        // Check if member has reached borrowing limit
        List<Borrowing> currentBorrowings = borrowingDAO.getBorrowingsByMemberId(memberId);
        long activeBorrowings = currentBorrowings.stream()
                .filter(Borrowing::isCurrentlyBorrowed)
                .count();

        if (activeBorrowings >= member.getMaxBooksAllowed()) {
            System.err.println("Member '" + member.getFullName() + "' has reached borrowing limit of " + member.getMaxBooksAllowed());
            return false;
        }

        // Create borrowing record
        LocalDate borrowDate = LocalDate.now();
        LocalDate dueDate = borrowDate.plusDays(DEFAULT_BORROW_DAYS);
        
        Borrowing borrowing = new Borrowing(bookId, memberId, borrowDate, dueDate);

        // Update book availability and create borrowing record
        if (bookDAO.updateAvailableCopies(bookId, book.getAvailableCopies() - 1) &&
            borrowingDAO.addBorrowing(borrowing)) {
            System.out.println("Book '" + book.getTitle() + "' borrowed by member '" + member.getFullName() + "'");
            return true;
        }

        return false;
    }

    /**
     * Return a book.
     * 
     * @param borrowingId Borrowing ID
     * @return true if successful, false otherwise
     */
    public boolean returnBook(int borrowingId) {
        // Get borrowing record
        Optional<Borrowing> borrowingOpt = borrowingDAO.getBorrowingById(borrowingId);
        if (borrowingOpt.isEmpty()) {
            System.err.println("Borrowing with ID " + borrowingId + " not found");
            return false;
        }
        Borrowing borrowing = borrowingOpt.get();

        if (borrowing.isReturned()) {
            System.err.println("Book already returned for borrowing ID " + borrowingId);
            return false;
        }

        // Calculate fine
        BigDecimal fineAmount = borrowing.calculateFine();
        LocalDate returnDate = LocalDate.now();

        // Update borrowing record and book availability
        Book book = bookDAO.getBookById(borrowing.getBookId()).orElse(null);
        if (book == null) {
            System.err.println("Book not found for borrowing ID " + borrowingId);
            return false;
        }

        if (borrowingDAO.returnBook(borrowingId, returnDate, fineAmount) &&
            bookDAO.updateAvailableCopies(book.getBookId(), book.getAvailableCopies() + 1)) {
            System.out.println("Book returned successfully. Fine amount: $" + fineAmount);
            return true;
        }

        return false;
    }

    /**
     * Get borrowings for a member.
     * 
     * @param memberId Member ID
     * @return List of borrowings
     */
    public List<Borrowing> getMemberBorrowings(int memberId) {
        return borrowingDAO.getBorrowingsByMemberId(memberId);
    }

    /**
     * Get current borrowings (not returned).
     * 
     * @return List of current borrowings
     */
    public List<Borrowing> getCurrentBorrowings() {
        return borrowingDAO.getCurrentBorrowings();
    }

    /**
     * Get overdue borrowings.
     * 
     * @return List of overdue borrowings
     */
    public List<Borrowing> getOverdueBorrowings() {
        return borrowingDAO.getOverdueBorrowings();
    }

    /**
     * Get all borrowings with details.
     * 
     * @return List of borrowings with book and member details
     */
    public List<Borrowing> getAllBorrowingsWithDetails() {
        return borrowingDAO.getBorrowingsWithDetails();
    }

    // ==================== REPORTS ====================

    /**
     * Get library statistics.
     * 
     * @return Library statistics as a string
     */
    public String getLibraryStatistics() {
        List<Book> allBooks = getAllBooks();
        List<Member> allMembers = getAllMembers();
        List<Borrowing> currentBorrowings = getCurrentBorrowings();
        List<Borrowing> overdueBorrowings = getOverdueBorrowings();

        int totalBooks = allBooks.size();
        int totalMembers = allMembers.size();
        int activeMembers = (int) allMembers.stream().filter(Member::isActive).count();
        int totalCopies = allBooks.stream().mapToInt(Book::getTotalCopies).sum();
        int availableCopies = allBooks.stream().mapToInt(Book::getAvailableCopies).sum();
        int borrowedCopies = totalCopies - availableCopies;

        StringBuilder stats = new StringBuilder();
        stats.append("=== LIBRARY STATISTICS ===\n");
        stats.append(String.format("Total Books: %d\n", totalBooks));
        stats.append(String.format("Total Copies: %d\n", totalCopies));
        stats.append(String.format("Available Copies: %d\n", availableCopies));
        stats.append(String.format("Borrowed Copies: %d\n", borrowedCopies));
        stats.append(String.format("Total Members: %d\n", totalMembers));
        stats.append(String.format("Active Members: %d\n", activeMembers));
        stats.append(String.format("Current Borrowings: %d\n", currentBorrowings.size()));
        stats.append(String.format("Overdue Books: %d\n", overdueBorrowings.size()));

        return stats.toString();
    }

    /**
     * Test database connection.
     * 
     * @return true if connection successful, false otherwise
     */
    public boolean testDatabaseConnection() {
        return DatabaseConnection.testConnection();
    }
}
