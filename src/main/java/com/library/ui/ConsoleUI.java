package com.library.ui;

import com.library.model.Book;
import com.library.model.Member;
import com.library.model.Borrowing;
import com.library.service.LibraryService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

/**
 * Console-based user interface for the Library Management System.
 */
public class ConsoleUI {
    
    private final LibraryService libraryService;
    private final Scanner scanner;
    private final DateTimeFormatter dateFormatter;
    
    public ConsoleUI() {
        this.libraryService = new LibraryService();
        this.scanner = new Scanner(System.in);
        this.dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    }

    /**
     * Start the console application.
     */
    public void start() {
        System.out.println("=== LIBRARY MANAGEMENT SYSTEM ===");
        System.out.println("Welcome to the Library Management System!");
        
        // Test database connection
        try {
            if (!libraryService.testDatabaseConnection()) {
                System.out.println("WARNING: Cannot connect to database. The application will run in demo mode.");
                System.out.println("To use full functionality, please:");
                System.out.println("1. Download MySQL Connector/J from: https://dev.mysql.com/downloads/connector/j/");
                System.out.println("2. Add the JAR file to the classpath when running:");
                System.out.println("   java -cp \"src/main/java;src/main/resources;mysql-connector-java-8.0.33.jar\" com.library.Main");
                System.out.println("3. Install MySQL Server and create database 'library_management'");
                System.out.println("4. Update database.properties with your credentials");
                System.out.println("5. Run the schema.sql script");
                System.out.println();
            } else {
                System.out.println("Database connection successful!");
                System.out.println();
            }
        } catch (Exception e) {
            System.out.println("WARNING: Database connection failed. The application will run in demo mode.");
            System.out.println("Error: " + e.getMessage());
            System.out.println("To use full functionality, please:");
            System.out.println("1. Download MySQL Connector/J from: https://dev.mysql.com/downloads/connector/j/");
            System.out.println("2. Add the JAR file to the classpath when running:");
            System.out.println("   java -cp \"src/main/java;src/main/resources;mysql-connector-java-8.0.33.jar\" com.library.Main");
            System.out.println("3. Install MySQL Server and create database 'library_management'");
            System.out.println("4. Update database.properties with your credentials");
            System.out.println("5. Run the schema.sql script");
            System.out.println();
        }
        
        boolean running = true;
        while (running) {
            displayMainMenu();
            int choice = getIntInput("Enter your choice: ");
            
            switch (choice) {
                case 1:
                    bookManagementMenu();
                    break;
                case 2:
                    memberManagementMenu();
                    break;
                case 3:
                    borrowingManagementMenu();
                    break;
                case 4:
                    reportsMenu();
                    break;
                case 5:
                    System.out.println("Thank you for using the Library Management System!");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
            
            if (running) {
                System.out.println("\nPress Enter to continue...");
                scanner.nextLine();
            }
        }
        
        scanner.close();
    }

    /**
     * Display the main menu.
     */
    private void displayMainMenu() {
        System.out.println("\n=== MAIN MENU ===");
        System.out.println("1. Book Management");
        System.out.println("2. Member Management");
        System.out.println("3. Borrowing Management");
        System.out.println("4. Reports");
        System.out.println("5. Exit");
    }

    /**
     * Book management menu.
     */
    private void bookManagementMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n=== BOOK MANAGEMENT ===");
            System.out.println("1. Add New Book");
            System.out.println("2. View All Books");
            System.out.println("3. Search Books");
            System.out.println("4. Update Book");
            System.out.println("5. Delete Book");
            System.out.println("6. View Available Books");
            System.out.println("7. Back to Main Menu");
            
            int choice = getIntInput("Enter your choice: ");
            
            switch (choice) {
                case 1:
                    addBook();
                    break;
                case 2:
                    viewAllBooks();
                    break;
                case 3:
                    searchBooks();
                    break;
                case 4:
                    updateBook();
                    break;
                case 5:
                    deleteBook();
                    break;
                case 6:
                    viewAvailableBooks();
                    break;
                case 7:
                    back = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    /**
     * Member management menu.
     */
    private void memberManagementMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n=== MEMBER MANAGEMENT ===");
            System.out.println("1. Register New Member");
            System.out.println("2. View All Members");
            System.out.println("3. Search Members");
            System.out.println("4. Update Member");
            System.out.println("5. Delete Member");
            System.out.println("6. View Active Members");
            System.out.println("7. Update Member Status");
            System.out.println("8. Back to Main Menu");
            
            int choice = getIntInput("Enter your choice: ");
            
            switch (choice) {
                case 1:
                    registerMember();
                    break;
                case 2:
                    viewAllMembers();
                    break;
                case 3:
                    searchMembers();
                    break;
                case 4:
                    updateMember();
                    break;
                case 5:
                    deleteMember();
                    break;
                case 6:
                    viewActiveMembers();
                    break;
                case 7:
                    updateMemberStatus();
                    break;
                case 8:
                    back = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    /**
     * Borrowing management menu.
     */
    private void borrowingManagementMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n=== BORROWING MANAGEMENT ===");
            System.out.println("1. Borrow Book");
            System.out.println("2. Return Book");
            System.out.println("3. View Current Borrowings");
            System.out.println("4. View Overdue Books");
            System.out.println("5. View Member Borrowings");
            System.out.println("6. View All Borrowings");
            System.out.println("7. Back to Main Menu");
            
            int choice = getIntInput("Enter your choice: ");
            
            switch (choice) {
                case 1:
                    borrowBook();
                    break;
                case 2:
                    returnBook();
                    break;
                case 3:
                    viewCurrentBorrowings();
                    break;
                case 4:
                    viewOverdueBooks();
                    break;
                case 5:
                    viewMemberBorrowings();
                    break;
                case 6:
                    viewAllBorrowings();
                    break;
                case 7:
                    back = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    /**
     * Reports menu.
     */
    private void reportsMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n=== REPORTS ===");
            System.out.println("1. Library Statistics");
            System.out.println("2. Overdue Books Report");
            System.out.println("3. Current Borrowings Report");
            System.out.println("4. Back to Main Menu");
            
            int choice = getIntInput("Enter your choice: ");
            
            switch (choice) {
                case 1:
                    displayLibraryStatistics();
                    break;
                case 2:
                    displayOverdueBooksReport();
                    break;
                case 3:
                    displayCurrentBorrowingsReport();
                    break;
                case 4:
                    back = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    // ==================== BOOK OPERATIONS ====================

    /**
     * Add a new book.
     */
    private void addBook() {
        System.out.println("\n=== ADD NEW BOOK ===");
        
        String isbn = getStringInput("Enter ISBN: ");
        String title = getStringInput("Enter title: ");
        String author = getStringInput("Enter author: ");
        String publisher = getStringInput("Enter publisher (optional): ");
        String genre = getStringInput("Enter genre (optional): ");
        
        Integer publicationYear = null;
        String yearStr = getStringInput("Enter publication year (optional): ");
        if (!yearStr.trim().isEmpty()) {
            try {
                publicationYear = Integer.parseInt(yearStr);
            } catch (NumberFormatException e) {
                System.out.println("Invalid year format. Using null.");
            }
        }
        
        int totalCopies = getIntInput("Enter total copies: ");
        
        Book book = new Book(isbn, title, author, publisher, publicationYear, genre, totalCopies);
        
        if (libraryService.addBook(book)) {
            System.out.println("Book added successfully!");
        } else {
            System.out.println("Failed to add book. Please check your input.");
        }
    }

    /**
     * View all books.
     */
    private void viewAllBooks() {
        System.out.println("\n=== ALL BOOKS ===");
        List<Book> books = libraryService.getAllBooks();
        
        if (books.isEmpty()) {
            System.out.println("No books found.");
            return;
        }
        
        System.out.printf("%-5s %-15s %-30s %-20s %-10s %-8s %-8s%n", 
                         "ID", "ISBN", "Title", "Author", "Genre", "Total", "Available");
        System.out.println("-".repeat(100));
        
        for (Book book : books) {
            System.out.printf("%-5d %-15s %-30s %-20s %-10s %-8d %-8d%n",
                             book.getBookId(), 
                             book.getIsbn(), 
                             truncate(book.getTitle(), 28),
                             truncate(book.getAuthor(), 18),
                             truncate(book.getGenre(), 8),
                             book.getTotalCopies(),
                             book.getAvailableCopies());
        }
    }

    /**
     * Search books.
     */
    private void searchBooks() {
        System.out.println("\n=== SEARCH BOOKS ===");
        System.out.println("1. Search by Title");
        System.out.println("2. Search by Author");
        System.out.println("3. Search by Genre");
        
        int choice = getIntInput("Enter your choice: ");
        String searchTerm = getStringInput("Enter search term: ");
        
        List<Book> results = null;
        switch (choice) {
            case 1:
                results = libraryService.searchBooksByTitle(searchTerm);
                break;
            case 2:
                results = libraryService.searchBooksByAuthor(searchTerm);
                break;
            case 3:
                results = libraryService.searchBooksByGenre(searchTerm);
                break;
            default:
                System.out.println("Invalid choice.");
                return;
        }
        
        if (results.isEmpty()) {
            System.out.println("No books found matching your search.");
        } else {
            System.out.println("\n=== SEARCH RESULTS ===");
            System.out.printf("%-5s %-15s %-30s %-20s %-10s %-8s %-8s%n", 
                             "ID", "ISBN", "Title", "Author", "Genre", "Total", "Available");
            System.out.println("-".repeat(100));
            
            for (Book book : results) {
                System.out.printf("%-5d %-15s %-30s %-20s %-10s %-8d %-8d%n",
                                 book.getBookId(), 
                                 book.getIsbn(), 
                                 truncate(book.getTitle(), 28),
                                 truncate(book.getAuthor(), 18),
                                 truncate(book.getGenre(), 8),
                                 book.getTotalCopies(),
                                 book.getAvailableCopies());
            }
        }
    }

    /**
     * Update a book.
     */
    private void updateBook() {
        System.out.println("\n=== UPDATE BOOK ===");
        int bookId = getIntInput("Enter book ID to update: ");
        
        Optional<Book> bookOpt = libraryService.getBookById(bookId);
        if (bookOpt.isEmpty()) {
            System.out.println("Book not found.");
            return;
        }
        
        Book book = bookOpt.get();
        System.out.println("Current book details:");
        System.out.println("Title: " + book.getTitle());
        System.out.println("Author: " + book.getAuthor());
        System.out.println("ISBN: " + book.getIsbn());
        
        String title = getStringInput("Enter new title (or press Enter to keep current): ");
        if (!title.trim().isEmpty()) {
            book.setTitle(title);
        }
        
        String author = getStringInput("Enter new author (or press Enter to keep current): ");
        if (!author.trim().isEmpty()) {
            book.setAuthor(author);
        }
        
        String publisher = getStringInput("Enter new publisher (or press Enter to keep current): ");
        if (!publisher.trim().isEmpty()) {
            book.setPublisher(publisher);
        }
        
        String genre = getStringInput("Enter new genre (or press Enter to keep current): ");
        if (!genre.trim().isEmpty()) {
            book.setGenre(genre);
        }
        
        if (libraryService.updateBook(book)) {
            System.out.println("Book updated successfully!");
        } else {
            System.out.println("Failed to update book.");
        }
    }

    /**
     * Delete a book.
     */
    private void deleteBook() {
        System.out.println("\n=== DELETE BOOK ===");
        int bookId = getIntInput("Enter book ID to delete: ");
        
        if (libraryService.deleteBook(bookId)) {
            System.out.println("Book deleted successfully!");
        } else {
            System.out.println("Failed to delete book. It may not exist or have active borrowings.");
        }
    }

    /**
     * View available books.
     */
    private void viewAvailableBooks() {
        System.out.println("\n=== AVAILABLE BOOKS ===");
        List<Book> books = libraryService.getAvailableBooks();
        
        if (books.isEmpty()) {
            System.out.println("No available books found.");
            return;
        }
        
        System.out.printf("%-5s %-15s %-30s %-20s %-10s %-8s%n", 
                         "ID", "ISBN", "Title", "Author", "Genre", "Available");
        System.out.println("-".repeat(90));
        
        for (Book book : books) {
            System.out.printf("%-5d %-15s %-30s %-20s %-10s %-8d%n",
                             book.getBookId(), 
                             book.getIsbn(), 
                             truncate(book.getTitle(), 28),
                             truncate(book.getAuthor(), 18),
                             truncate(book.getGenre(), 8),
                             book.getAvailableCopies());
        }
    }

    // ==================== MEMBER OPERATIONS ====================

    /**
     * Register a new member.
     */
    private void registerMember() {
        System.out.println("\n=== REGISTER NEW MEMBER ===");
        
        String memberCode = getStringInput("Enter member code: ");
        String firstName = getStringInput("Enter first name: ");
        String lastName = getStringInput("Enter last name: ");
        String email = getStringInput("Enter email: ");
        String phone = getStringInput("Enter phone (optional): ");
        String address = getStringInput("Enter address (optional): ");
        int maxBooks = getIntInput("Enter max books allowed (1-10): ");
        
        Member member = new Member(memberCode, firstName, lastName, email, phone, address, LocalDate.now(), maxBooks);
        
        if (libraryService.registerMember(member)) {
            System.out.println("Member registered successfully!");
        } else {
            System.out.println("Failed to register member. Please check your input.");
        }
    }

    /**
     * View all members.
     */
    private void viewAllMembers() {
        System.out.println("\n=== ALL MEMBERS ===");
        List<Member> members = libraryService.getAllMembers();
        
        if (members.isEmpty()) {
            System.out.println("No members found.");
            return;
        }
        
        System.out.printf("%-5s %-10s %-20s %-25s %-15s %-10s%n", 
                         "ID", "Code", "Name", "Email", "Phone", "Status");
        System.out.println("-".repeat(90));
        
        for (Member member : members) {
            System.out.printf("%-5d %-10s %-20s %-25s %-15s %-10s%n",
                             member.getMemberId(),
                             member.getMemberCode(),
                             truncate(member.getFullName(), 18),
                             truncate(member.getEmail(), 23),
                             truncate(member.getPhone(), 13),
                             member.getMembershipStatus());
        }
    }

    /**
     * Search members.
     */
    private void searchMembers() {
        System.out.println("\n=== SEARCH MEMBERS ===");
        String searchTerm = getStringInput("Enter name to search: ");
        
        List<Member> results = libraryService.searchMembersByName(searchTerm);
        
        if (results.isEmpty()) {
            System.out.println("No members found matching your search.");
        } else {
            System.out.println("\n=== SEARCH RESULTS ===");
            System.out.printf("%-5s %-10s %-20s %-25s %-15s %-10s%n", 
                             "ID", "Code", "Name", "Email", "Phone", "Status");
            System.out.println("-".repeat(90));
            
            for (Member member : results) {
                System.out.printf("%-5d %-10s %-20s %-25s %-15s %-10s%n",
                                 member.getMemberId(),
                                 member.getMemberCode(),
                                 truncate(member.getFullName(), 18),
                                 truncate(member.getEmail(), 23),
                                 truncate(member.getPhone(), 13),
                                 member.getMembershipStatus());
            }
        }
    }

    /**
     * Update a member.
     */
    private void updateMember() {
        System.out.println("\n=== UPDATE MEMBER ===");
        int memberId = getIntInput("Enter member ID to update: ");
        
        Optional<Member> memberOpt = libraryService.getMemberById(memberId);
        if (memberOpt.isEmpty()) {
            System.out.println("Member not found.");
            return;
        }
        
        Member member = memberOpt.get();
        System.out.println("Current member details:");
        System.out.println("Name: " + member.getFullName());
        System.out.println("Email: " + member.getEmail());
        
        String firstName = getStringInput("Enter new first name (or press Enter to keep current): ");
        if (!firstName.trim().isEmpty()) {
            member.setFirstName(firstName);
        }
        
        String lastName = getStringInput("Enter new last name (or press Enter to keep current): ");
        if (!lastName.trim().isEmpty()) {
            member.setLastName(lastName);
        }
        
        String email = getStringInput("Enter new email (or press Enter to keep current): ");
        if (!email.trim().isEmpty()) {
            member.setEmail(email);
        }
        
        String phone = getStringInput("Enter new phone (or press Enter to keep current): ");
        if (!phone.trim().isEmpty()) {
            member.setPhone(phone);
        }
        
        String address = getStringInput("Enter new address (or press Enter to keep current): ");
        if (!address.trim().isEmpty()) {
            member.setAddress(address);
        }
        
        if (libraryService.updateMember(member)) {
            System.out.println("Member updated successfully!");
        } else {
            System.out.println("Failed to update member.");
        }
    }

    /**
     * Delete a member.
     */
    private void deleteMember() {
        System.out.println("\n=== DELETE MEMBER ===");
        int memberId = getIntInput("Enter member ID to delete: ");
        
        if (libraryService.deleteMember(memberId)) {
            System.out.println("Member deleted successfully!");
        } else {
            System.out.println("Failed to delete member. It may not exist or have active borrowings.");
        }
    }

    /**
     * View active members.
     */
    private void viewActiveMembers() {
        System.out.println("\n=== ACTIVE MEMBERS ===");
        List<Member> members = libraryService.getActiveMembers();
        
        if (members.isEmpty()) {
            System.out.println("No active members found.");
            return;
        }
        
        System.out.printf("%-5s %-10s %-20s %-25s %-15s%n", 
                         "ID", "Code", "Name", "Email", "Phone");
        System.out.println("-".repeat(80));
        
        for (Member member : members) {
            System.out.printf("%-5d %-10s %-20s %-25s %-15s%n",
                             member.getMemberId(),
                             member.getMemberCode(),
                             truncate(member.getFullName(), 18),
                             truncate(member.getEmail(), 23),
                             truncate(member.getPhone(), 13));
        }
    }

    /**
     * Update member status.
     */
    private void updateMemberStatus() {
        System.out.println("\n=== UPDATE MEMBER STATUS ===");
        int memberId = getIntInput("Enter member ID: ");
        
        System.out.println("Select new status:");
        System.out.println("1. ACTIVE");
        System.out.println("2. SUSPENDED");
        System.out.println("3. EXPIRED");
        
        int choice = getIntInput("Enter your choice: ");
        Member.MembershipStatus status = null;
        
        switch (choice) {
            case 1:
                status = Member.MembershipStatus.ACTIVE;
                break;
            case 2:
                status = Member.MembershipStatus.SUSPENDED;
                break;
            case 3:
                status = Member.MembershipStatus.EXPIRED;
                break;
            default:
                System.out.println("Invalid choice.");
                return;
        }
        
        if (libraryService.updateMemberStatus(memberId, status)) {
            System.out.println("Member status updated successfully!");
        } else {
            System.out.println("Failed to update member status.");
        }
    }

    // ==================== BORROWING OPERATIONS ====================

    /**
     * Borrow a book.
     */
    private void borrowBook() {
        System.out.println("\n=== BORROW BOOK ===");
        int bookId = getIntInput("Enter book ID: ");
        int memberId = getIntInput("Enter member ID: ");
        
        if (libraryService.borrowBook(bookId, memberId)) {
            System.out.println("Book borrowed successfully!");
        } else {
            System.out.println("Failed to borrow book. Please check book availability and member status.");
        }
    }

    /**
     * Return a book.
     */
    private void returnBook() {
        System.out.println("\n=== RETURN BOOK ===");
        int borrowingId = getIntInput("Enter borrowing ID: ");
        
        if (libraryService.returnBook(borrowingId)) {
            System.out.println("Book returned successfully!");
        } else {
            System.out.println("Failed to return book.");
        }
    }

    /**
     * View current borrowings.
     */
    private void viewCurrentBorrowings() {
        System.out.println("\n=== CURRENT BORROWINGS ===");
        List<Borrowing> borrowings = libraryService.getCurrentBorrowings();
        
        if (borrowings.isEmpty()) {
            System.out.println("No current borrowings found.");
            return;
        }
        
        System.out.printf("%-5s %-5s %-5s %-12s %-12s %-10s%n", 
                         "ID", "Book", "Member", "Borrow Date", "Due Date", "Status");
        System.out.println("-".repeat(60));
        
        for (Borrowing borrowing : borrowings) {
            System.out.printf("%-5d %-5d %-5d %-12s %-12s %-10s%n",
                             borrowing.getBorrowingId(),
                             borrowing.getBookId(),
                             borrowing.getMemberId(),
                             borrowing.getBorrowDate(),
                             borrowing.getDueDate(),
                             borrowing.getStatus());
        }
    }

    /**
     * View overdue books.
     */
    private void viewOverdueBooks() {
        System.out.println("\n=== OVERDUE BOOKS ===");
        List<Borrowing> borrowings = libraryService.getOverdueBorrowings();
        
        if (borrowings.isEmpty()) {
            System.out.println("No overdue books found.");
            return;
        }
        
        System.out.printf("%-5s %-5s %-5s %-12s %-12s %-8s%n", 
                         "ID", "Book", "Member", "Borrow Date", "Due Date", "Days Overdue");
        System.out.println("-".repeat(60));
        
        for (Borrowing borrowing : borrowings) {
            System.out.printf("%-5d %-5d %-5d %-12s %-12s %-8d%n",
                             borrowing.getBorrowingId(),
                             borrowing.getBookId(),
                             borrowing.getMemberId(),
                             borrowing.getBorrowDate(),
                             borrowing.getDueDate(),
                             borrowing.getDaysOverdue());
        }
    }

    /**
     * View member borrowings.
     */
    private void viewMemberBorrowings() {
        System.out.println("\n=== MEMBER BORROWINGS ===");
        int memberId = getIntInput("Enter member ID: ");
        
        List<Borrowing> borrowings = libraryService.getMemberBorrowings(memberId);
        
        if (borrowings.isEmpty()) {
            System.out.println("No borrowings found for this member.");
            return;
        }
        
        System.out.printf("%-5s %-5s %-12s %-12s %-12s %-10s%n", 
                         "ID", "Book", "Borrow Date", "Due Date", "Return Date", "Status");
        System.out.println("-".repeat(70));
        
        for (Borrowing borrowing : borrowings) {
            String returnDate = borrowing.getReturnDate() != null ? 
                               borrowing.getReturnDate().toString() : "Not returned";
            System.out.printf("%-5d %-5d %-12s %-12s %-12s %-10s%n",
                             borrowing.getBorrowingId(),
                             borrowing.getBookId(),
                             borrowing.getBorrowDate(),
                             borrowing.getDueDate(),
                             returnDate,
                             borrowing.getStatus());
        }
    }

    /**
     * View all borrowings.
     */
    private void viewAllBorrowings() {
        System.out.println("\n=== ALL BORROWINGS ===");
        List<Borrowing> borrowings = libraryService.getAllBorrowingsWithDetails();
        
        if (borrowings.isEmpty()) {
            System.out.println("No borrowings found.");
            return;
        }
        
        System.out.printf("%-5s %-30s %-20s %-12s %-12s %-10s%n", 
                         "ID", "Book", "Member", "Borrow Date", "Due Date", "Status");
        System.out.println("-".repeat(100));
        
        for (Borrowing borrowing : borrowings) {
            String bookTitle = borrowing.getBook() != null ? borrowing.getBook().getTitle() : "Unknown";
            String memberName = borrowing.getMember() != null ? borrowing.getMember().getFullName() : "Unknown";
            
            System.out.printf("%-5d %-30s %-20s %-12s %-12s %-10s%n",
                             borrowing.getBorrowingId(),
                             truncate(bookTitle, 28),
                             truncate(memberName, 18),
                             borrowing.getBorrowDate(),
                             borrowing.getDueDate(),
                             borrowing.getStatus());
        }
    }

    // ==================== REPORTS ====================

    /**
     * Display library statistics.
     */
    private void displayLibraryStatistics() {
        System.out.println("\n=== LIBRARY STATISTICS ===");
        System.out.println(libraryService.getLibraryStatistics());
    }

    /**
     * Display overdue books report.
     */
    private void displayOverdueBooksReport() {
        System.out.println("\n=== OVERDUE BOOKS REPORT ===");
        List<Borrowing> borrowings = libraryService.getOverdueBorrowings();
        
        if (borrowings.isEmpty()) {
            System.out.println("No overdue books found.");
            return;
        }
        
        System.out.printf("%-5s %-30s %-20s %-12s %-12s %-8s %-8s%n", 
                         "ID", "Book", "Member", "Borrow Date", "Due Date", "Days Overdue", "Fine");
        System.out.println("-".repeat(110));
        
        for (Borrowing borrowing : borrowings) {
            String bookTitle = borrowing.getBook() != null ? borrowing.getBook().getTitle() : "Unknown";
            String memberName = borrowing.getMember() != null ? borrowing.getMember().getFullName() : "Unknown";
            
            System.out.printf("%-5d %-30s %-20s %-12s %-12s %-8d %-8s%n",
                             borrowing.getBorrowingId(),
                             truncate(bookTitle, 28),
                             truncate(memberName, 18),
                             borrowing.getBorrowDate(),
                             borrowing.getDueDate(),
                             borrowing.getDaysOverdue(),
                             "$" + borrowing.getFineAmount());
        }
    }

    /**
     * Display current borrowings report.
     */
    private void displayCurrentBorrowingsReport() {
        System.out.println("\n=== CURRENT BORROWINGS REPORT ===");
        List<Borrowing> borrowings = libraryService.getCurrentBorrowings();
        
        if (borrowings.isEmpty()) {
            System.out.println("No current borrowings found.");
            return;
        }
        
        System.out.printf("%-5s %-30s %-20s %-12s %-12s %-10s%n", 
                         "ID", "Book", "Member", "Borrow Date", "Due Date", "Status");
        System.out.println("-".repeat(100));
        
        for (Borrowing borrowing : borrowings) {
            String bookTitle = borrowing.getBook() != null ? borrowing.getBook().getTitle() : "Unknown";
            String memberName = borrowing.getMember() != null ? borrowing.getMember().getFullName() : "Unknown";
            
            System.out.printf("%-5d %-30s %-20s %-12s %-12s %-10s%n",
                             borrowing.getBorrowingId(),
                             truncate(bookTitle, 28),
                             truncate(memberName, 18),
                             borrowing.getBorrowDate(),
                             borrowing.getDueDate(),
                             borrowing.getStatus());
        }
    }

    // ==================== UTILITY METHODS ====================

    /**
     * Get integer input from user.
     * 
     * @param prompt Prompt message
     * @return Integer input
     */
    private int getIntInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    /**
     * Get string input from user.
     * 
     * @param prompt Prompt message
     * @return String input
     */
    private String getStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    /**
     * Truncate string to specified length.
     * 
     * @param str String to truncate
     * @param maxLength Maximum length
     * @return Truncated string
     */
    private String truncate(String str, int maxLength) {
        if (str == null) {
            return "";
        }
        return str.length() <= maxLength ? str : str.substring(0, maxLength - 2) + "..";
    }
}
