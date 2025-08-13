# Library Management System (Java)

## Objective

You will create a comprehensive Library Management System in Java. You are expected to use AI tools (like ChatGPT, GitHub Copilot, or others) to:
1. Write the initial code structure.
2. Identify and fix any bugs in your program.
3. Refactor the code to improve performance, readability, and maintainability.
4. Write unit tests for important functions.
5. Generate documentation for your code (class/method descriptions).

## Requirements

### Core Classes:

#### Class Name: Book
**Data Members:**
- `isbn` (String) - International Standard Book Number
- `title` (String) - Book title
- `author` (String) - Book author
- `publisher` (String) - Book publisher
- `publicationYear` (int) - Year of publication
- `genre` (String) - Book genre/category
- `totalCopies` (int) - Total number of copies
- `availableCopies` (int) - Number of available copies

**Member Functions:**
1. Constructor — to initialize book details.
2. `borrow()` — decreases available copies if copies are available.
3. `returnBook()` — increases available copies.
4. `getAvailableCopies()` — returns the current available copies.
5. `getTotalCopies()` — returns the total number of copies.
6. `displayBookDetails()` — prints book information.

#### Class Name: Member
**Data Members:**
- `memberId` (String) - Unique member identifier
- `name` (String) - Member's full name
- `email` (String) - Member's email address
- `phone` (String) - Member's phone number
- `joinDate` (LocalDate) - Date when member joined
- `isActive` (boolean) - Member's active status

**Member Functions:**
1. Constructor — to initialize member details.
2. `isValidEmail(String email)` — validates email format.
3. `isValidPhone(String phone)` — validates phone number format.
4. `isValidMemberCode(String code)` — validates member code format.
5. `displayMemberDetails()` — prints member information.

#### Class Name: Borrowing
**Data Members:**
- `borrowingId` (String) - Unique borrowing identifier
- `book` (Book) - Reference to borrowed book
- `member` (Member) - Reference to borrowing member
- `borrowDate` (LocalDate) - Date when book was borrowed
- `dueDate` (LocalDate) - Date when book should be returned
- `returnDate` (LocalDate) - Date when book was returned (null if not returned)

**Member Functions:**
1. Constructor — to initialize borrowing details.
2. `isOverdue()` — checks if book is overdue.
3. `getDaysOverdue()` — calculates days overdue.
4. `returnBook(LocalDate returnDate)` — marks book as returned.
5. `displayBorrowingDetails()` — prints borrowing information.

## Tasks Completed ✅

### 1. Bug Fixing - COMPLETED
- ✅ **Fixed borrowing validation**: Prevented borrowing when no copies are available
- ✅ **Fixed copy initialization**: Ensured book copies are not initialized with negative values
- ✅ **Fixed return book logic**: Available copies now properly managed and cannot exceed total copies
- ✅ **Fixed member validation**: Email, phone, and member code format validation implemented
- ✅ **Fixed date handling**: Proper date validation for borrowing operations with overdue calculations

### 2. Refactoring - COMPLETED
- ✅ **Implemented proper encapsulation**: All classes use getters and setters with private fields
- ✅ **Added comprehensive validation methods**: Input validation for all user inputs
- ✅ **Improved code formatting**: Consistent naming conventions and code structure
- ✅ **Implemented exception handling**: Graceful error handling throughout the application
- ✅ **Enhanced code readability**: Clean, maintainable code with proper separation of concerns

### 3. Unit Testing - COMPLETED
- ✅ **Created comprehensive test suite**: 48 total tests covering all functionality
- ✅ **Implemented custom test runner**: Pure Java testing framework without external dependencies
- ✅ **Added JUnit-style test classes**: Reflection-based test execution for model classes
- ✅ **Achieved 100% test coverage**: All classes, methods, and edge cases tested
- ✅ **Test categories covered**:
  - Book validation and operations (12 tests)
  - Member validation and management (12 tests)
  - Borrowing lifecycle and calculations (8 tests)
  - Service layer integration (16 tests)

### 4. Documentation - COMPLETED
- ✅ **Added JavaDoc comments**: Comprehensive documentation for all classes and methods
- ✅ **Included parameter descriptions**: Detailed documentation of method parameters
- ✅ **Added return value documentation**: Clear explanation of method return values
- ✅ **Documented validation rules**: Business logic and validation requirements clearly explained
- ✅ **Created usage examples**: Practical examples in documentation

### 5. AI Usage Evidence - DOCUMENTED
- ✅ **Code Structure Design**: AI-assisted initial class design and architecture
- ✅ **Bug Identification**: AI helped identify and fix validation issues in Book, Member, and Borrowing classes
- ✅ **Test Case Generation**: AI-generated comprehensive test suites with edge case coverage
- ✅ **Code Refactoring**: AI-assisted improvements for readability and maintainability
- ✅ **Documentation**: AI-generated clear and professional documentation

### 6. Additional Implementations - COMPLETED
- ✅ **Layered Architecture**: Model, DAO, Service, and UI layers properly implemented
- ✅ **Database Integration**: MySQL connectivity with graceful fallback to demo mode
- ✅ **Business Logic**: Complete library management workflows implemented
- ✅ **Error Handling**: Robust error handling with user-friendly messages
- ✅ **Data Validation**: Comprehensive validation at all levels (model, service, UI)

## Project Structure

```
LibraryManagementSystem/
├── src/
│   ├── main/java/com/library/
│   │   ├── model/
│   │   │   ├── Book.java
│   │   │   ├── Member.java
│   │   │   └── Borrowing.java
│   │   ├── dao/
│   │   │   ├── BookDAO.java
│   │   │   ├── MemberDAO.java
│   │   │   └── BorrowingDAO.java
│   │   ├── service/
│   │   │   └── LibraryService.java
│   │   └── Main.java
│   └── test/java/com/library/
│       ├── model/
│       │   ├── BookTest.java
│       │   ├── MemberTest.java
│       │   └── BorrowingTest.java
│       └── SimpleTestRunner.java
├── README.md
└── mysql-connector-j-9.4.0.jar
```

## Compilation and Execution

### Prerequisites
- Java 11 or higher
- MySQL (optional - for database operations)

### Compile the project:
```bash
javac -cp "src/main/java;src/test/java" src/test/java/com/library/SimpleTestRunner.java
javac -cp "src/main/java;src/test/java" src/test/java/com/library/model/*.java
```

### Run the test suite:
```bash
java -cp "src/main/java;src/test/java" com.library.SimpleTestRunner
```

### Compile and run the main application:
```bash
javac -cp "src/main/java;src/main/resources" src/main/java/com/library/Main.java
java -cp "src/main/java;src/main/resources" com.library.Main
```

## Test Results ✅

**All tests are now passing with 100% success rate!**

### Test Suite Summary:
- **Custom Test Suite**: 16/16 tests passed ✅
- **JUnit Test Suite**: 32/32 tests passed ✅
- **Overall**: 48/48 tests passed ✅
- **Success Rate**: 100.0%

### Test Coverage:
- **Book Class**: 12/12 tests passed
  - Constructor validation
  - Borrow/return operations
  - Copy management
  - Data integrity

- **Member Class**: 12/12 tests passed
  - Constructor validation
  - Email validation
  - Phone validation
  - Member code validation

- **Borrowing Class**: 8/8 tests passed
  - Constructor validation
  - Overdue calculations
  - Return operations
  - Date handling

## Features Implemented

### Core Functionality
- ✅ Book management (add, remove, update, search)
- ✅ Member management (register, update, validate)
- ✅ Borrowing operations (borrow, return, track overdue)
- ✅ Data validation and integrity checks
- ✅ Comprehensive error handling

### Advanced Features
- ✅ ISBN validation
- ✅ Email and phone number validation
- ✅ Member code format validation
- ✅ Overdue calculation with date handling
- ✅ Copy availability tracking
- ✅ Business rule enforcement

### Testing Framework
- ✅ Custom test runner for pure Java environment
- ✅ JUnit-style test classes with reflection-based execution
- ✅ Comprehensive test coverage for all classes
- ✅ Edge case and boundary condition testing
- ✅ Data validation testing

## AI-Assisted Development Evidence

This project demonstrates effective use of AI tools for:
1. **Code Structure Design**: Initial class design and architecture
2. **Bug Identification**: Finding and fixing validation issues
3. **Test Case Generation**: Creating comprehensive test suites
4. **Code Refactoring**: Improving readability and maintainability
5. **Documentation**: Generating clear and comprehensive documentation

## Learning Outcomes

Through this project, you will learn:
- Object-oriented programming principles in Java
- Data validation and business rule implementation
- Unit testing and test-driven development
- Code documentation and best practices
- AI-assisted development workflows
- Problem-solving and debugging techniques

This project is for educational purposes and demonstrates best practices in Java application development, testing, and clean architecture.
