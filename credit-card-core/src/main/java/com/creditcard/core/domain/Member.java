package com.creditcard.core.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "members")
public class Member {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "member_number", unique = true, nullable = false)
    private String memberNumber;
    
    @Column(name = "name_kanji")
    private String nameKanji;
    
    @Column(name = "name_kana")
    private String nameKana;
    
    @Column(nullable = false, unique = true)
    private String email;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MemberStatus status;
    
    @Column(name = "credit_limit", precision = 10, scale = 2)
    private BigDecimal creditLimit;
    
    @Column(name = "current_balance", precision = 10, scale = 2)
    private BigDecimal currentBalance;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    public enum MemberStatus {
        PENDING, ACTIVE, SUSPENDED, CLOSED
    }
    
    public Member() {}
    
    public Member(String memberNumber, String nameKanji, String email, 
                  MemberStatus status, BigDecimal creditLimit, BigDecimal currentBalance) {
        this.memberNumber = memberNumber;
        this.nameKanji = nameKanji;
        this.email = email;
        this.status = status;
        this.creditLimit = creditLimit;
        this.currentBalance = currentBalance;
    }
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getMemberNumber() { return memberNumber; }
    public void setMemberNumber(String memberNumber) { this.memberNumber = memberNumber; }
    public String getNameKanji() { return nameKanji; }
    public void setNameKanji(String nameKanji) { this.nameKanji = nameKanji; }
    public String getNameKana() { return nameKana; }
    public void setNameKana(String nameKana) { this.nameKana = nameKana; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public MemberStatus getStatus() { return status; }
    public void setStatus(MemberStatus status) { this.status = status; }
    public BigDecimal getCreditLimit() { return creditLimit; }
    public void setCreditLimit(BigDecimal creditLimit) { this.creditLimit = creditLimit; }
    public BigDecimal getCurrentBalance() { return currentBalance; }
    public void setCurrentBalance(BigDecimal currentBalance) { this.currentBalance = currentBalance; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    public static Builder builder() {
        return new Builder();
    }
    
    public static class Builder {
        private String memberNumber;
        private String nameKanji;
        private String nameKana;
        private String email;
        private MemberStatus status;
        private BigDecimal creditLimit;
        private BigDecimal currentBalance;
        
        public Builder memberNumber(String v) { memberNumber = v; return this; }
        public Builder nameKanji(String v) { nameKanji = v; return this; }
        public Builder nameKana(String v) { nameKana = v; return this; }
        public Builder email(String v) { email = v; return this; }
        public Builder status(MemberStatus v) { status = v; return this; }
        public Builder creditLimit(BigDecimal v) { creditLimit = v; return this; }
        public Builder currentBalance(BigDecimal v) { currentBalance = v; return this; }
        public Member build() {
            return new Member(memberNumber, nameKanji, email, status, creditLimit, currentBalance);
        }
    }
}
