package com.creditcard.core.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "billings")
public class Billing {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "billing_id", unique = true, nullable = false)
    private String billingId;
    
    @Column(name = "member_number", nullable = false)
    private String memberNumber;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BillingStatus status;
    
    @Column(precision = 10, scale = 2)
    private BigDecimal amount;
    
    @Column(name = "due_date")
    private LocalDateTime dueDate;
    
    @Column(name = "paid_at")
    private LocalDateTime paidAt;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    public enum BillingStatus {
        ISSUED, PAID, OVERDUE, PARTIAL_PAID, CANCELLED
    }
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getBillingId() { return billingId; }
    public void setBillingId(String billingId) { this.billingId = billingId; }
    public String getMemberNumber() { return memberNumber; }
    public void setMemberNumber(String memberNumber) { this.memberNumber = memberNumber; }
    public BillingStatus getStatus() { return status; }
    public void setStatus(BillingStatus status) { this.status = status; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public LocalDateTime getDueDate() { return dueDate; }
    public void setDueDate(LocalDateTime dueDate) { this.dueDate = dueDate; }
    public LocalDateTime getPaidAt() { return paidAt; }
    public void setPaidAt(LocalDateTime paidAt) { this.paidAt = paidAt; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
