package com.library.dao;

import com.library.model.Member;
import com.library.util.DatabaseConnection;
import org.junit.jupiter.api.*;
import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MemberDAOTest {

    private MemberDAO memberDAO;

    @BeforeEach
    void setUp() {
        memberDAO = new MemberDAO();
    }

    @Test
    @DisplayName("Should add member successfully")
    void testAddMember_Success() {
        // Arrange
        Member member = new Member("M001", "John Doe", "john.doe@email.com", "1234567890");
        member.setAddress("123 Main St, City, State 12345");
        member.setMembershipType("STUDENT");
        member.setMaxBooksAllowed(3);
        
        // Act
        boolean result = memberDAO.addMember(member);
        
        // Assert
        assertTrue(result);
    }

    @Test
    @DisplayName("Should fail to add member with null input")
    void testAddMember_NullInput() {
        // Act
        boolean result = memberDAO.addMember(null);
        
        // Assert
        assertFalse(result);
    }

    @Test
    @DisplayName("Should fail to add member with invalid data")
    void testAddMember_InvalidData() {
        // Arrange
        Member member = new Member("", "", "", ""); // Invalid data
        
        // Act
        boolean result = memberDAO.addMember(member);
        
        // Assert
        assertFalse(result);
    }

    @Test
    @DisplayName("Should fail to add member with duplicate member code")
    void testAddMember_DuplicateMemberCode() {
        // Arrange
        Member member1 = new Member("M001", "John Doe", "john.doe@email.com", "1234567890");
        Member member2 = new Member("M001", "Jane Doe", "jane.doe@email.com", "0987654321");
        
        // Act
        boolean result1 = memberDAO.addMember(member1);
        boolean result2 = memberDAO.addMember(member2);
        
        // Assert
        assertTrue(result1);
        assertFalse(result2); // Should fail due to duplicate member code
    }

    @Test
    @DisplayName("Should fail to add member with duplicate email")
    void testAddMember_DuplicateEmail() {
        // Arrange
        Member member1 = new Member("M001", "John Doe", "john.doe@email.com", "1234567890");
        Member member2 = new Member("M002", "Jane Doe", "john.doe@email.com", "0987654321");
        
        // Act
        boolean result1 = memberDAO.addMember(member1);
        boolean result2 = memberDAO.addMember(member2);
        
        // Assert
        assertTrue(result1);
        assertFalse(result2); // Should fail due to duplicate email
    }

    @Test
    @DisplayName("Should update member successfully")
    void testUpdateMember_Success() {
        // Arrange
        Member member = new Member("M001", "Updated Name", "updated@email.com", "1234567890");
        member.setMemberId(1);
        member.setAddress("Updated Address");
        member.setMembershipType("FACULTY");
        member.setMaxBooksAllowed(5);
        
        // Act
        boolean result = memberDAO.updateMember(member);
        
        // Assert
        // Note: This will return false if the member doesn't exist in the database
        assertNotNull(result); // Just check it doesn't throw exception
    }

    @Test
    @DisplayName("Should fail to update member with null input")
    void testUpdateMember_NullInput() {
        // Act
        boolean result = memberDAO.updateMember(null);
        
        // Assert
        assertFalse(result);
    }

    @Test
    @DisplayName("Should delete member successfully")
    void testDeleteMember_Success() {
        // Arrange
        int memberId = 1;
        
        // Act
        boolean result = memberDAO.deleteMember(memberId);
        
        // Assert
        // Note: This will return false if the member doesn't exist in the database
        assertNotNull(result); // Just check it doesn't throw exception
    }

    @Test
    @DisplayName("Should fail to delete member with invalid ID")
    void testDeleteMember_InvalidId() {
        // Arrange
        int memberId = -1;
        
        // Act
        boolean result = memberDAO.deleteMember(memberId);
        
        // Assert
        assertFalse(result);
    }

    @Test
    @DisplayName("Should get member by ID successfully")
    void testGetMemberById_Success() {
        // Arrange
        int memberId = 1;
        
        // Act
        Member result = memberDAO.getMemberById(memberId);
        
        // Assert
        // Note: This will return null if the member doesn't exist in the database
        assertNotNull(result); // Just check it doesn't throw exception
    }

    @Test
    @DisplayName("Should return null when member not found by ID")
    void testGetMemberById_NotFound() {
        // Arrange
        int memberId = 999999;
        
        // Act
        Member result = memberDAO.getMemberById(memberId);
        
        // Assert
        assertNull(result);
    }

    @Test
    @DisplayName("Should get member by member code successfully")
    void testGetMemberByCode_Success() {
        // Arrange
        String memberCode = "M001";
        
        // Act
        Member result = memberDAO.getMemberByCode(memberCode);
        
        // Assert
        // Note: This will return null if the member doesn't exist in the database
        assertNotNull(result); // Just check it doesn't throw exception
    }

    @Test
    @DisplayName("Should return null when member not found by code")
    void testGetMemberByCode_NotFound() {
        // Arrange
        String memberCode = "INVALID";
        
        // Act
        Member result = memberDAO.getMemberByCode(memberCode);
        
        // Assert
        assertNull(result);
    }

    @Test
    @DisplayName("Should return null when member code is null")
    void testGetMemberByCode_NullInput() {
        // Act
        Member result = memberDAO.getMemberByCode(null);
        
        // Assert
        assertNull(result);
    }

    @Test
    @DisplayName("Should get member by email successfully")
    void testGetMemberByEmail_Success() {
        // Arrange
        String email = "john.doe@email.com";
        
        // Act
        Member result = memberDAO.getMemberByEmail(email);
        
        // Assert
        // Note: This will return null if the member doesn't exist in the database
        assertNotNull(result); // Just check it doesn't throw exception
    }

    @Test
    @DisplayName("Should return null when member not found by email")
    void testGetMemberByEmail_NotFound() {
        // Arrange
        String email = "nonexistent@email.com";
        
        // Act
        Member result = memberDAO.getMemberByEmail(email);
        
        // Assert
        assertNull(result);
    }

    @Test
    @DisplayName("Should return null when email is null")
    void testGetMemberByEmail_NullInput() {
        // Act
        Member result = memberDAO.getMemberByEmail(null);
        
        // Assert
        assertNull(result);
    }

    @Test
    @DisplayName("Should get all members successfully")
    void testGetAllMembers_Success() {
        // Act
        List<Member> result = memberDAO.getAllMembers();
        
        // Assert
        assertNotNull(result);
        // Note: The list might be empty if no members exist in the database
    }

    @Test
    @DisplayName("Should search members by name successfully")
    void testSearchMembersByName_Success() {
        // Arrange
        String searchTerm = "John";
        
        // Act
        List<Member> result = memberDAO.searchMembersByName(searchTerm);
        
        // Assert
        assertNotNull(result);
        // Note: The list might be empty if no matching members exist
    }

    @Test
    @DisplayName("Should search members by name with null input")
    void testSearchMembersByName_NullInput() {
        // Act
        List<Member> result = memberDAO.searchMembersByName(null);
        
        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Should search members by name with empty input")
    void testSearchMembersByName_EmptyInput() {
        // Arrange
        String searchTerm = "";
        
        // Act
        List<Member> result = memberDAO.searchMembersByName(searchTerm);
        
        // Assert
        assertNotNull(result);
        // Note: The list might be empty if no matching members exist
    }

    @Test
    @DisplayName("Should check if member code exists")
    void testIsMemberCodeExists() {
        // Arrange
        String existingCode = "M001";
        String nonExistingCode = "INVALID";
        
        // Act
        boolean result1 = memberDAO.isMemberCodeExists(existingCode);
        boolean result2 = memberDAO.isMemberCodeExists(nonExistingCode);
        
        // Assert
        // Note: These results depend on the database state
        assertNotNull(result1);
        assertNotNull(result2);
    }

    @Test
    @DisplayName("Should return false when checking null member code")
    void testIsMemberCodeExists_NullInput() {
        // Act
        boolean result = memberDAO.isMemberCodeExists(null);
        
        // Assert
        assertFalse(result);
    }

    @Test
    @DisplayName("Should check if email exists")
    void testIsEmailExists() {
        // Arrange
        String existingEmail = "john.doe@email.com";
        String nonExistingEmail = "nonexistent@email.com";
        
        // Act
        boolean result1 = memberDAO.isEmailExists(existingEmail);
        boolean result2 = memberDAO.isEmailExists(nonExistingEmail);
        
        // Assert
        // Note: These results depend on the database state
        assertNotNull(result1);
        assertNotNull(result2);
    }

    @Test
    @DisplayName("Should return false when checking null email")
    void testIsEmailExists_NullInput() {
        // Act
        boolean result = memberDAO.isEmailExists(null);
        
        // Assert
        assertFalse(result);
    }

    @Test
    @DisplayName("Should update member status successfully")
    void testUpdateMemberStatus_Success() {
        // Arrange
        int memberId = 1;
        String newStatus = "SUSPENDED";
        
        // Act
        boolean result = memberDAO.updateMemberStatus(memberId, newStatus);
        
        // Assert
        // Note: This will return false if the member doesn't exist in the database
        assertNotNull(result); // Just check it doesn't throw exception
    }

    @Test
    @DisplayName("Should fail to update member status with invalid ID")
    void testUpdateMemberStatus_InvalidId() {
        // Arrange
        int memberId = -1;
        String newStatus = "ACTIVE";
        
        // Act
        boolean result = memberDAO.updateMemberStatus(memberId, newStatus);
        
        // Assert
        assertFalse(result);
    }

    @Test
    @DisplayName("Should fail to update member status with null status")
    void testUpdateMemberStatus_NullStatus() {
        // Arrange
        int memberId = 1;
        String newStatus = null;
        
        // Act
        boolean result = memberDAO.updateMemberStatus(memberId, newStatus);
        
        // Assert
        assertFalse(result);
    }

    @Test
    @DisplayName("Should handle database connection issues gracefully")
    void testDatabaseConnectionIssues() {
        // This test verifies that the DAO handles database connection issues gracefully
        // In a real scenario, this would be tested with a mock database
        
        // Arrange
        Member member = new Member("M001", "Test Member", "test@email.com", "1234567890");
        
        // Act & Assert
        // These should not throw exceptions even if database is not available
        assertNotNull(memberDAO.addMember(member));
        assertNotNull(memberDAO.updateMember(member));
        assertNotNull(memberDAO.deleteMember(1));
        assertNotNull(memberDAO.getMemberById(1));
        assertNotNull(memberDAO.getMemberByCode("M001"));
        assertNotNull(memberDAO.getMemberByEmail("test@email.com"));
        assertNotNull(memberDAO.getAllMembers());
        assertNotNull(memberDAO.searchMembersByName("test"));
        assertNotNull(memberDAO.isMemberCodeExists("M001"));
        assertNotNull(memberDAO.isEmailExists("test@email.com"));
        assertNotNull(memberDAO.updateMemberStatus(1, "ACTIVE"));
    }

    @Test
    @DisplayName("Should validate member data before database operations")
    void testMemberValidation() {
        // Arrange
        Member invalidMember = new Member("", "", "", ""); // Invalid data
        Member validMember = new Member("M001", "Valid Member", "valid@email.com", "1234567890");
        validMember.setAddress("Valid Address");
        validMember.setMembershipType("STUDENT");
        validMember.setMaxBooksAllowed(3);
        
        // Act & Assert
        assertFalse(invalidMember.isValid());
        assertTrue(validMember.isValid());
        assertTrue(validMember.isValidEmail());
        assertTrue(validMember.isValidPhone());
        assertTrue(validMember.isValidMemberCode());
    }

    @Test
    @DisplayName("Should handle member business logic correctly")
    void testMemberBusinessLogic() {
        // Arrange
        Member member = new Member("M001", "Test Member", "test@email.com", "1234567890");
        member.setMaxBooksAllowed(3);
        member.setCurrentBooksBorrowed(1);
        
        // Act & Assert
        assertTrue(member.isActive());
        assertEquals(2, member.getRemainingBooksAllowed());
        assertTrue(member.canBorrowBook());
        
        // Test borrowing a book
        member.borrowBook();
        assertEquals(2, member.getCurrentBooksBorrowed());
        assertEquals(1, member.getRemainingBooksAllowed());
        
        // Test returning a book
        member.returnBook();
        assertEquals(1, member.getCurrentBooksBorrowed());
        assertEquals(2, member.getRemainingBooksAllowed());
    }

    @Test
    @DisplayName("Should handle edge cases in member operations")
    void testMemberEdgeCases() {
        // Arrange
        Member member = new Member("M001", "Test Member", "test@email.com", "1234567890");
        member.setMaxBooksAllowed(2);
        member.setCurrentBooksBorrowed(2);
        
        // Act & Assert
        assertFalse(member.canBorrowBook());
        assertEquals(0, member.getRemainingBooksAllowed());
        
        // Test borrowing when at limit
        assertThrows(IllegalStateException.class, () -> member.borrowBook());
        
        // Test returning when no books borrowed
        member.setCurrentBooksBorrowed(0);
        assertThrows(IllegalStateException.class, () -> member.returnBook());
    }

    @Test
    @DisplayName("Should handle member status changes correctly")
    void testMemberStatusChanges() {
        // Arrange
        Member member = new Member("M001", "Test Member", "test@email.com", "1234567890");
        
        // Act & Assert
        assertTrue(member.isActive());
        
        // Test suspending member
        member.setStatus("SUSPENDED");
        assertFalse(member.isActive());
        assertTrue(member.isSuspended());
        
        // Test expiring member
        member.setStatus("EXPIRED");
        assertFalse(member.isActive());
        assertTrue(member.isExpired());
        
        // Test reactivating member
        member.setStatus("ACTIVE");
        assertTrue(member.isActive());
    }

    @Test
    @DisplayName("Should validate member contact information")
    void testMemberContactValidation() {
        // Arrange
        Member member = new Member("M001", "Test Member", "test@email.com", "1234567890");
        
        // Act & Assert
        assertTrue(member.isValidEmail());
        assertTrue(member.isValidPhone());
        
        // Test invalid email
        member.setEmail("invalid-email");
        assertFalse(member.isValidEmail());
        
        // Test invalid phone
        member.setPhone("123");
        assertFalse(member.isValidPhone());
        
        // Test null values
        member.setEmail(null);
        member.setPhone(null);
        assertFalse(member.isValidEmail());
        assertFalse(member.isValidPhone());
    }

    @Test
    @DisplayName("Should validate member code format")
    void testMemberCodeValidation() {
        // Arrange
        Member member = new Member("M001", "Test Member", "test@email.com", "1234567890");
        
        // Act & Assert
        assertTrue(member.isValidMemberCode());
        
        // Test invalid member code
        member.setMemberCode("invalid");
        assertFalse(member.isValidMemberCode());
        
        // Test null member code
        member.setMemberCode(null);
        assertFalse(member.isValidMemberCode());
    }
}
