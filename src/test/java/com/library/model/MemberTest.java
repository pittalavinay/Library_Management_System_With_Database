package com.library.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Simplified unit tests for Member model class that can run without JUnit dependencies.
 * These tests are designed to work with the SimpleTestRunner.
 */
public class MemberTest {

    private Member validMember;
    private Member emptyMember;

    public void setUp() {
        validMember = new Member("MEM001", "John", "Doe", "john.doe@email.com", 
                               "555-0101", "123 Main St", LocalDate.now(), 5);
        emptyMember = new Member();
    }

    public void testValidMemberCreation() {
        setUp();
        assertTrue(validMember != null, "Valid member should not be null");
        assertTrue("MEM001".equals(validMember.getMemberCode()), "Member code should match");
        assertTrue("John".equals(validMember.getFirstName()), "First name should match");
        assertTrue("Doe".equals(validMember.getLastName()), "Last name should match");
        assertTrue("john.doe@email.com".equals(validMember.getEmail()), "Email should match");
        assertTrue("555-0101".equals(validMember.getPhone()), "Phone should match");
        assertTrue("123 Main St".equals(validMember.getAddress()), "Address should match");
        assertTrue(LocalDate.now().equals(validMember.getMembershipDate()), "Membership date should match");
        assertTrue(Member.MembershipStatus.ACTIVE.equals(validMember.getMembershipStatus()), "Status should be ACTIVE");
        assertTrue(validMember.getMaxBooksAllowed() == 5, "Max books should be 5");
    }

    public void testMinimalMemberCreation() {
        Member member = new Member("MEM002", "Jane", "Smith", "jane.smith@email.com");
        assertTrue("MEM002".equals(member.getMemberCode()), "Member code should match");
        assertTrue("Jane".equals(member.getFirstName()), "First name should match");
        assertTrue("Smith".equals(member.getLastName()), "Last name should match");
        assertTrue("jane.smith@email.com".equals(member.getEmail()), "Email should match");
        assertTrue(Member.MembershipStatus.ACTIVE.equals(member.getMembershipStatus()), "Status should be ACTIVE");
        assertTrue(member.getMaxBooksAllowed() == 5, "Max books should be 5");
        assertTrue(LocalDate.now().equals(member.getMembershipDate()), "Membership date should be today");
    }

    public void testMemberFullName() {
        setUp();
        assertTrue("John Doe".equals(validMember.getFullName()), "Full name should be 'John Doe'");
        
        validMember.setFirstName("Jane");
        validMember.setLastName("Smith");
        assertTrue("Jane Smith".equals(validMember.getFullName()), "Full name should be 'Jane Smith'");
    }

    public void testMemberStatusOperations() {
        setUp();
        assertTrue(validMember.isActive(), "Member should be active initially");
        assertTrue(validMember.canBorrowBooks(), "Member should be able to borrow books initially");
        
        validMember.suspend();
        assertTrue(Member.MembershipStatus.SUSPENDED.equals(validMember.getMembershipStatus()), "Status should be SUSPENDED");
        assertTrue(!validMember.isActive(), "Member should not be active when suspended");
        assertTrue(!validMember.canBorrowBooks(), "Member should not be able to borrow books when suspended");
        
        validMember.activate();
        assertTrue(Member.MembershipStatus.ACTIVE.equals(validMember.getMembershipStatus()), "Status should be ACTIVE");
        assertTrue(validMember.isActive(), "Member should be active when activated");
        assertTrue(validMember.canBorrowBooks(), "Member should be able to borrow books when activated");
        
        validMember.expire();
        assertTrue(Member.MembershipStatus.EXPIRED.equals(validMember.getMembershipStatus()), "Status should be EXPIRED");
        assertTrue(!validMember.isActive(), "Member should not be active when expired");
        assertTrue(!validMember.canBorrowBooks(), "Member should not be able to borrow books when expired");
    }

    public void testMemberValidation() {
        setUp();
        assertTrue(validMember.isValid(), "Valid member should be valid");
        assertTrue(validMember.isValidEmail(validMember.getEmail()), "Valid email should be valid");
        assertTrue(validMember.isValidPhone(validMember.getPhone()), "Valid phone should be valid");
        assertTrue(validMember.isValidMemberCode(validMember.getMemberCode()), "Valid member code should be valid");
        assertTrue(validMember.isValidMaxBooksAllowed(validMember.getMaxBooksAllowed()), "Valid max books should be valid");
    }

    public void testMemberEmailValidation() {
        setUp();
        assertTrue(validMember.isValidEmail("test@email.com"), "Valid email should pass validation");
        assertTrue(validMember.isValidEmail("test.name@domain.co.uk"), "Valid email with dots should pass validation");
        assertTrue(!validMember.isValidEmail("invalid-email"), "Invalid email should fail validation");
        assertTrue(!validMember.isValidEmail(""), "Empty email should fail validation");
        assertTrue(!validMember.isValidEmail(null), "Null email should fail validation");
    }

    public void testMemberPhoneValidation() {
        setUp();
        assertTrue(validMember.isValidPhone("555-0101"), "Valid phone should pass validation");
        assertTrue(validMember.isValidPhone("(555) 010-0101"), "Valid phone with parentheses should pass validation");
        assertTrue(!validMember.isValidPhone("555.010.0101"), "Phone with dots should fail validation (dots not allowed)");
        assertTrue(validMember.isValidPhone("123"), "Short phone should pass validation (any length allowed)");
        assertTrue(validMember.isValidPhone(""), "Empty phone should pass validation (optional field)");
        assertTrue(validMember.isValidPhone(null), "Null phone should pass validation (optional field)");
        assertTrue(!validMember.isValidPhone("abc-def-ghij"), "Phone with letters should fail validation");
    }

    public void testMemberCodeValidation() {
        setUp();
        assertTrue(validMember.isValidMemberCode("MEM001"), "Valid member code should pass validation");
        assertTrue(validMember.isValidMemberCode("M001"), "Valid short member code should pass validation");
        assertTrue(validMember.isValidMemberCode("123"), "Numeric member code should pass validation (alphanumeric allowed)");
        assertTrue(!validMember.isValidMemberCode(""), "Empty member code should fail validation");
        assertTrue(!validMember.isValidMemberCode(null), "Null member code should fail validation");
        assertTrue(!validMember.isValidMemberCode("ME"), "Too short member code should fail validation");
        
        // Test with exactly 20 characters (should pass)
        String validLongCode = "MEM001MEM001MEM001ME";
        assertTrue(validMember.isValidMemberCode(validLongCode), "20-character member code should pass validation");
        
        // Test with 21 characters (should fail)
        String invalidLongCode = "MEM001MEM001MEM001MEM";
        assertTrue(!validMember.isValidMemberCode(invalidLongCode), "21-character member code should fail validation");
    }

    public void testMemberMaxBooksValidation() {
        setUp();
        assertTrue(validMember.isValidMaxBooksAllowed(5), "Valid max books should pass validation");
        assertTrue(validMember.isValidMaxBooksAllowed(1), "Valid min max books should pass validation");
        assertTrue(validMember.isValidMaxBooksAllowed(10), "Valid high max books should pass validation");
        assertTrue(!validMember.isValidMaxBooksAllowed(0), "Zero max books should fail validation");
        assertTrue(!validMember.isValidMaxBooksAllowed(-1), "Negative max books should fail validation");
    }

    public void testMemberSettersAndGetters() {
        setUp();
        LocalDate newDate = LocalDate.now().minusDays(30);
        validMember.setMembershipDate(newDate);
        validMember.setMaxBooksAllowed(3);
        
        assertTrue(newDate.equals(validMember.getMembershipDate()), "Membership date should be updated");
        assertTrue(validMember.getMaxBooksAllowed() == 3, "Max books should be updated");
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
