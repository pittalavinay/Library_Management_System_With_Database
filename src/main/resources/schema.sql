-- Library Management System Database Schema

-- Create database
CREATE DATABASE IF NOT EXISTS library_management;
USE library_management;

-- Books table
CREATE TABLE IF NOT EXISTS books (
    book_id INT PRIMARY KEY AUTO_INCREMENT,
    isbn VARCHAR(13) UNIQUE NOT NULL,
    title VARCHAR(255) NOT NULL,
    author VARCHAR(255) NOT NULL,
    publisher VARCHAR(255),
    publication_year INT,
    genre VARCHAR(100),
    total_copies INT DEFAULT 1,
    available_copies INT DEFAULT 1,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT chk_publication_year CHECK (publication_year >= 1800 AND publication_year <= YEAR(CURRENT_DATE)),
    CONSTRAINT chk_copies CHECK (total_copies >= 0 AND available_copies >= 0 AND available_copies <= total_copies)
);

-- Members table
CREATE TABLE IF NOT EXISTS members (
    member_id INT PRIMARY KEY AUTO_INCREMENT,
    member_code VARCHAR(20) UNIQUE NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    phone VARCHAR(20),
    address TEXT,
    membership_date DATE NOT NULL,
    membership_status ENUM('ACTIVE', 'SUSPENDED', 'EXPIRED') DEFAULT 'ACTIVE',
    max_books_allowed INT DEFAULT 5,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT chk_membership_date CHECK (membership_date <= CURRENT_DATE),
    CONSTRAINT chk_max_books CHECK (max_books_allowed >= 1 AND max_books_allowed <= 10)
);

-- Borrowings table
CREATE TABLE IF NOT EXISTS borrowings (
    borrowing_id INT PRIMARY KEY AUTO_INCREMENT,
    book_id INT NOT NULL,
    member_id INT NOT NULL,
    borrow_date DATE NOT NULL,
    due_date DATE NOT NULL,
    return_date DATE,
    status ENUM('BORROWED', 'RETURNED', 'OVERDUE') DEFAULT 'BORROWED',
    fine_amount DECIMAL(10,2) DEFAULT 0.00,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (book_id) REFERENCES books(book_id) ON DELETE CASCADE,
    FOREIGN KEY (member_id) REFERENCES members(member_id) ON DELETE CASCADE,
    CONSTRAINT chk_borrow_date CHECK (borrow_date <= CURRENT_DATE),
    CONSTRAINT chk_due_date CHECK (due_date > borrow_date),
    CONSTRAINT chk_return_date CHECK (return_date IS NULL OR return_date >= borrow_date),
    CONSTRAINT chk_fine_amount CHECK (fine_amount >= 0)
);

-- Create indexes for better performance
CREATE INDEX idx_books_isbn ON books(isbn);
CREATE INDEX idx_books_title ON books(title);
CREATE INDEX idx_books_author ON books(author);
CREATE INDEX idx_members_email ON members(email);
CREATE INDEX idx_members_code ON members(member_code);
CREATE INDEX idx_borrowings_book_id ON borrowings(book_id);
CREATE INDEX idx_borrowings_member_id ON borrowings(member_id);
CREATE INDEX idx_borrowings_status ON borrowings(status);
CREATE INDEX idx_borrowings_due_date ON borrowings(due_date);

-- Insert sample data
INSERT INTO books (isbn, title, author, publisher, publication_year, genre, total_copies, available_copies) VALUES
('9780132350884', 'Clean Code', 'Robert C. Martin', 'Prentice Hall', 2008, 'Programming', 3, 3),
('9780201633610', 'Design Patterns', 'Erich Gamma, Richard Helm, Ralph Johnson, John Vlissides', 'Addison-Wesley', 1994, 'Programming', 2, 2),
('9780262033848', 'Introduction to Algorithms', 'Thomas H. Cormen', 'MIT Press', 2009, 'Computer Science', 1, 1),
('9780596007126', 'Head First Java', 'Kathy Sierra, Bert Bates', 'O''Reilly Media', 2005, 'Programming', 4, 4),
('9780134685991', 'Effective Java', 'Joshua Bloch', 'Addison-Wesley', 2017, 'Programming', 2, 2);

INSERT INTO members (member_code, first_name, last_name, email, phone, address, membership_date, max_books_allowed) VALUES
('MEM001', 'John', 'Doe', 'john.doe@email.com', '555-0101', '123 Main St, City, State', '2023-01-15', 5),
('MEM002', 'Jane', 'Smith', 'jane.smith@email.com', '555-0102', '456 Oak Ave, City, State', '2023-02-20', 5),
('MEM003', 'Bob', 'Johnson', 'bob.johnson@email.com', '555-0103', '789 Pine Rd, City, State', '2023-03-10', 3),
('MEM004', 'Alice', 'Brown', 'alice.brown@email.com', '555-0104', '321 Elm St, City, State', '2023-04-05', 5),
('MEM005', 'Charlie', 'Wilson', 'charlie.wilson@email.com', '555-0105', '654 Maple Dr, City, State', '2023-05-12', 4);
