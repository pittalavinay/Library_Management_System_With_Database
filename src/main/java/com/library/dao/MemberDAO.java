package com.library.dao;

import com.library.model.Member;
import com.library.util.DatabaseConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Data Access Object for Member entity.
 */
public class MemberDAO {
    
    // SQL Queries
    private static final String INSERT_MEMBER = 
        "INSERT INTO members (member_code, first_name, last_name, email, phone, address, " +
        "membership_date, membership_status, max_books_allowed) " +
        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
    
    private static final String SELECT_BY_ID = 
        "SELECT * FROM members WHERE member_id = ?";
    
    private static final String SELECT_BY_CODE = 
        "SELECT * FROM members WHERE member_code = ?";
    
    private static final String SELECT_BY_EMAIL = 
        "SELECT * FROM members WHERE email = ?";
    
    private static final String SELECT_ALL = 
        "SELECT * FROM members ORDER BY last_name, first_name";
    
    private static final String UPDATE_MEMBER = 
        "UPDATE members SET member_code = ?, first_name = ?, last_name = ?, email = ?, " +
        "phone = ?, address = ?, membership_date = ?, membership_status = ?, max_books_allowed = ? " +
        "WHERE member_id = ?";
    
    private static final String DELETE_MEMBER = 
        "DELETE FROM members WHERE member_id = ?";
    
    private static final String SEARCH_BY_NAME = 
        "SELECT * FROM members WHERE first_name LIKE ? OR last_name LIKE ? ORDER BY last_name, first_name";
    
    private static final String SELECT_ACTIVE_MEMBERS = 
        "SELECT * FROM members WHERE membership_status = 'ACTIVE' ORDER BY last_name, first_name";
    
    private static final String UPDATE_MEMBERSHIP_STATUS = 
        "UPDATE members SET membership_status = ? WHERE member_id = ?";
    
    private static final String CHECK_MEMBER_CODE_EXISTS = 
        "SELECT COUNT(*) FROM members WHERE member_code = ?";
    
    private static final String CHECK_EMAIL_EXISTS = 
        "SELECT COUNT(*) FROM members WHERE email = ?";

    /**
     * Add a new member to the database.
     * 
     * @param member Member to add
     * @return true if successful, false otherwise
     */
    public boolean addMember(Member member) {
        if (!member.isValid()) {
            System.err.println("Invalid member data: " + member);
            return false;
        }

        Connection connection = null;
        PreparedStatement pstmt = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            pstmt = connection.prepareStatement(INSERT_MEMBER, Statement.RETURN_GENERATED_KEYS);
            
            pstmt.setString(1, member.getMemberCode());
            pstmt.setString(2, member.getFirstName());
            pstmt.setString(3, member.getLastName());
            pstmt.setString(4, member.getEmail());
            pstmt.setString(5, member.getPhone());
            pstmt.setString(6, member.getAddress());
            pstmt.setObject(7, member.getMembershipDate());
            pstmt.setString(8, member.getMembershipStatus().name());
            pstmt.setInt(9, member.getMaxBooksAllowed());
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    member.setMemberId(rs.getInt(1));
                }
                DatabaseConnection.commit(connection);
                System.out.println("Member added successfully: " + member.getFullName());
                return true;
            }
            
        } catch (SQLException e) {
            DatabaseConnection.rollback(connection);
            System.err.println("Error adding member: " + e.getMessage());
        } finally {
            closeResources(pstmt, connection);
        }
        return false;
    }

    /**
     * Get member by ID.
     * 
     * @param memberId Member ID
     * @return Optional containing member if found
     */
    public Optional<Member> getMemberById(int memberId) {
        Connection connection = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            pstmt = connection.prepareStatement(SELECT_BY_ID);
            pstmt.setInt(1, memberId);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return Optional.of(mapResultSetToMember(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error retrieving member by ID " + memberId + ": " + e.getMessage());
        } finally {
            closeResources(rs, pstmt, connection);
        }
        return Optional.empty();
    }

    /**
     * Get member by member code.
     * 
     * @param memberCode Member code
     * @return Optional containing member if found
     */
    public Optional<Member> getMemberByCode(String memberCode) {
        Connection connection = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            pstmt = connection.prepareStatement(SELECT_BY_CODE);
            pstmt.setString(1, memberCode);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return Optional.of(mapResultSetToMember(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error retrieving member by code " + memberCode + ": " + e.getMessage());
        } finally {
            closeResources(rs, pstmt, connection);
        }
        return Optional.empty();
    }

    /**
     * Get member by email.
     * 
     * @param email Email address
     * @return Optional containing member if found
     */
    public Optional<Member> getMemberByEmail(String email) {
        Connection connection = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            pstmt = connection.prepareStatement(SELECT_BY_EMAIL);
            pstmt.setString(1, email);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return Optional.of(mapResultSetToMember(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error retrieving member by email " + email + ": " + e.getMessage());
        } finally {
            closeResources(rs, pstmt, connection);
        }
        return Optional.empty();
    }

    /**
     * Get all members.
     * 
     * @return List of all members
     */
    public List<Member> getAllMembers() {
        List<Member> members = new ArrayList<>();
        Connection connection = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            pstmt = connection.prepareStatement(SELECT_ALL);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                members.add(mapResultSetToMember(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error retrieving all members: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Unexpected error retrieving members: " + e.getMessage());
        } finally {
            closeResources(rs, pstmt, connection);
        }
        return members;
    }

    /**
     * Update an existing member.
     * 
     * @param member Member to update
     * @return true if successful, false otherwise
     */
    public boolean updateMember(Member member) {
        if (!member.isValid() || member.getMemberId() <= 0) {
            System.err.println("Invalid member data for update: " + member);
            return false;
        }

        Connection connection = null;
        PreparedStatement pstmt = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            pstmt = connection.prepareStatement(UPDATE_MEMBER);
            
            pstmt.setString(1, member.getMemberCode());
            pstmt.setString(2, member.getFirstName());
            pstmt.setString(3, member.getLastName());
            pstmt.setString(4, member.getEmail());
            pstmt.setString(5, member.getPhone());
            pstmt.setString(6, member.getAddress());
            pstmt.setObject(7, member.getMembershipDate());
            pstmt.setString(8, member.getMembershipStatus().name());
            pstmt.setInt(9, member.getMaxBooksAllowed());
            pstmt.setInt(10, member.getMemberId());
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                DatabaseConnection.commit(connection);
                System.out.println("Member updated successfully: " + member.getFullName());
                return true;
            }
            
        } catch (SQLException e) {
            DatabaseConnection.rollback(connection);
            System.err.println("Error updating member: " + e.getMessage());
        } finally {
            closeResources(pstmt, connection);
        }
        return false;
    }

    /**
     * Delete a member by ID.
     * 
     * @param memberId Member ID to delete
     * @return true if successful, false otherwise
     */
    public boolean deleteMember(int memberId) {
        Connection connection = null;
        PreparedStatement pstmt = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            pstmt = connection.prepareStatement(DELETE_MEMBER);
            pstmt.setInt(1, memberId);
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                DatabaseConnection.commit(connection);
                System.out.println("Member deleted successfully: ID " + memberId);
                return true;
            }
            
        } catch (SQLException e) {
            DatabaseConnection.rollback(connection);
            System.err.println("Error deleting member ID " + memberId + ": " + e.getMessage());
        } finally {
            closeResources(pstmt, connection);
        }
        return false;
    }

    /**
     * Search members by name.
     * 
     * @param name Name to search for
     * @return List of matching members
     */
    public List<Member> searchByName(String name) {
        List<Member> members = new ArrayList<>();
        Connection connection = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            pstmt = connection.prepareStatement(SEARCH_BY_NAME);
            pstmt.setString(1, "%" + name + "%");
            pstmt.setString(2, "%" + name + "%");
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                members.add(mapResultSetToMember(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error searching members by name: " + e.getMessage());
        } finally {
            closeResources(rs, pstmt, connection);
        }
        return members;
    }

    /**
     * Get all active members.
     * 
     * @return List of active members
     */
    public List<Member> getActiveMembers() {
        List<Member> members = new ArrayList<>();
        Connection connection = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            pstmt = connection.prepareStatement(SELECT_ACTIVE_MEMBERS);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                members.add(mapResultSetToMember(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error retrieving active members: " + e.getMessage());
        } finally {
            closeResources(rs, pstmt, connection);
        }
        return members;
    }

    /**
     * Update member's membership status.
     * 
     * @param memberId Member ID
     * @param status New status
     * @return true if successful, false otherwise
     */
    public boolean updateMembershipStatus(int memberId, Member.MembershipStatus status) {
        Connection connection = null;
        PreparedStatement pstmt = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            pstmt = connection.prepareStatement(UPDATE_MEMBERSHIP_STATUS);
            pstmt.setString(1, status.name());
            pstmt.setInt(2, memberId);
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                DatabaseConnection.commit(connection);
                System.out.println("Membership status updated for member ID " + memberId + ": " + status);
                return true;
            }
            
        } catch (SQLException e) {
            DatabaseConnection.rollback(connection);
            System.err.println("Error updating membership status for member ID " + memberId + ": " + e.getMessage());
        } finally {
            closeResources(pstmt, connection);
        }
        return false;
    }

    /**
     * Check if member code already exists.
     * 
     * @param memberCode Member code to check
     * @return true if exists, false otherwise
     */
    public boolean isMemberCodeExists(String memberCode) {
        Connection connection = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            pstmt = connection.prepareStatement(CHECK_MEMBER_CODE_EXISTS);
            pstmt.setString(1, memberCode);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            
        } catch (SQLException e) {
            System.err.println("Error checking member code existence: " + e.getMessage());
        } finally {
            closeResources(rs, pstmt, connection);
        }
        return false;
    }

    /**
     * Check if email already exists.
     * 
     * @param email Email to check
     * @return true if exists, false otherwise
     */
    public boolean isEmailExists(String email) {
        Connection connection = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            pstmt = connection.prepareStatement(CHECK_EMAIL_EXISTS);
            pstmt.setString(1, email);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            
        } catch (SQLException e) {
            System.err.println("Error checking email existence: " + e.getMessage());
        } finally {
            closeResources(rs, pstmt, connection);
        }
        return false;
    }

    /**
     * Map ResultSet to Member object.
     * 
     * @param rs ResultSet
     * @return Member object
     * @throws SQLException if mapping fails
     */
    private Member mapResultSetToMember(ResultSet rs) throws SQLException {
        Member member = new Member();
        member.setMemberId(rs.getInt("member_id"));
        member.setMemberCode(rs.getString("member_code"));
        member.setFirstName(rs.getString("first_name"));
        member.setLastName(rs.getString("last_name"));
        member.setEmail(rs.getString("email"));
        member.setPhone(rs.getString("phone"));
        member.setAddress(rs.getString("address"));
        member.setMembershipDate(rs.getObject("membership_date", LocalDate.class));
        member.setMembershipStatus(Member.MembershipStatus.valueOf(rs.getString("membership_status")));
        member.setMaxBooksAllowed(rs.getInt("max_books_allowed"));
        return member;
    }

    /**
     * Close database resources.
     * 
     * @param rs ResultSet
     * @param pstmt PreparedStatement
     * @param connection Connection
     */
    private void closeResources(ResultSet rs, PreparedStatement pstmt, Connection connection) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                System.err.println("Error closing ResultSet: " + e.getMessage());
            }
        }
        closeResources(pstmt, connection);
    }

    /**
     * Close database resources.
     * 
     * @param pstmt PreparedStatement
     * @param connection Connection
     */
    private void closeResources(PreparedStatement pstmt, Connection connection) {
        if (pstmt != null) {
            try {
                pstmt.close();
            } catch (SQLException e) {
                System.err.println("Error closing PreparedStatement: " + e.getMessage());
            }
        }
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.err.println("Error closing PreparedStatement: " + e.getMessage());
            }
        }
    }
}
