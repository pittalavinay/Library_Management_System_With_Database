package com.library.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Model class representing a library member.
 */
public class Member {
    private int memberId;
    private String memberCode;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String address;
    private LocalDate membershipDate;
    private MembershipStatus membershipStatus;
    private int maxBooksAllowed;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public enum MembershipStatus {
        ACTIVE, SUSPENDED, EXPIRED
    }

    // Default constructor
    public Member() {
        this.membershipStatus = MembershipStatus.ACTIVE;
        this.maxBooksAllowed = 5;
    }

    // Constructor with essential fields
    public Member(String memberCode, String firstName, String lastName, String email) {
        this();
        this.memberCode = memberCode;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.membershipDate = LocalDate.now();
    }

    // Full constructor
    public Member(String memberCode, String firstName, String lastName, String email,
                  String phone, String address, LocalDate membershipDate, int maxBooksAllowed) {
        this(memberCode, firstName, lastName, email);
        this.phone = phone;
        this.address = address;
        this.membershipDate = membershipDate;
        this.maxBooksAllowed = maxBooksAllowed;
    }

    // Getters and Setters
    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public String getMemberCode() {
        return memberCode;
    }

    public void setMemberCode(String memberCode) {
        this.memberCode = memberCode;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public LocalDate getMembershipDate() {
        return membershipDate;
    }

    public void setMembershipDate(LocalDate membershipDate) {
        this.membershipDate = membershipDate;
    }

    public MembershipStatus getMembershipStatus() {
        return membershipStatus;
    }

    public void setMembershipStatus(MembershipStatus membershipStatus) {
        this.membershipStatus = membershipStatus;
    }

    public int getMaxBooksAllowed() {
        return maxBooksAllowed;
    }

    public void setMaxBooksAllowed(int maxBooksAllowed) {
        this.maxBooksAllowed = maxBooksAllowed;
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
    public String getFullName() {
        return firstName + " " + lastName;
    }

    public boolean isActive() {
        return membershipStatus == MembershipStatus.ACTIVE;
    }

    public boolean canBorrowBooks() {
        return isActive();
    }

    public void suspend() {
        this.membershipStatus = MembershipStatus.SUSPENDED;
    }

    public void activate() {
        this.membershipStatus = MembershipStatus.ACTIVE;
    }

    public void expire() {
        this.membershipStatus = MembershipStatus.EXPIRED;
    }

    // Validation methods
    public boolean isValid() {
        return memberCode != null && !memberCode.trim().isEmpty() &&
               firstName != null && !firstName.trim().isEmpty() &&
               lastName != null && !lastName.trim().isEmpty() &&
               email != null && !email.trim().isEmpty() &&
               isValidEmail(email) &&
               membershipDate != null &&
               maxBooksAllowed >= 1 && maxBooksAllowed <= 10;
    }

    public boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        // Basic email validation pattern
        String emailPattern = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return Pattern.matches(emailPattern, email);
    }

    public boolean isValidPhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            return true; // Optional field
        }
        // Basic phone validation (digits, spaces, dashes, parentheses)
        String phonePattern = "^[\\d\\s\\-\\(\\)\\+]+$";
        return Pattern.matches(phonePattern, phone);
    }

    public boolean isValidMemberCode(String memberCode) {
        if (memberCode == null || memberCode.trim().isEmpty()) {
            return false;
        }
        // Member code should be alphanumeric and 3-20 characters
        String codePattern = "^[A-Za-z0-9]{3,20}$";
        return Pattern.matches(codePattern, memberCode);
    }

    public boolean isValidMaxBooksAllowed(int maxBooks) {
        return maxBooks >= 1 && maxBooks <= 10;
    }

    // Override methods
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Member member = (Member) obj;
        return Objects.equals(memberCode, member.memberCode) &&
               Objects.equals(email, member.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(memberCode, email);
    }

    @Override
    public String toString() {
        return String.format("Member{memberId=%d, memberCode='%s', name='%s %s', " +
                           "email='%s', status=%s, maxBooks=%d}", 
                           memberId, memberCode, firstName, lastName, 
                           email, membershipStatus, maxBooksAllowed);
    }
}
