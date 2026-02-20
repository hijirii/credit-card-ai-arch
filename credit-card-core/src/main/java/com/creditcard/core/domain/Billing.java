package com.creditcard.core.domain;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 請求・清算ドメイン
 * Billing and Settlement Domain
 */
@Entity
@Table(name = "billings")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Billing {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "billing_id", unique = true, nullable = false)
    private String billingId;
    
    @Column(name = "member_number", nullable = false)
    private String memberNumber;
    
    @Column(name = "billing_month", nullable = false)
    private LocalDate billingMonth;
    
    @Column(name = "billing_amount", nullable = false)
    private BigDecimal billingAmount;
    
    @Column(name = "minimum_payment")
    private BigDecimal minimumPayment;
    
    @Column(name = "previous_balance")
    private BigDecimal previousBalance;
    
    @Column(name = "new_charges")
    private BigDecimal newCharges;
    
    @Column(name = "payments")
    private BigDecimal payments;
    
    @Column(name = "adjustments")
    private BigDecimal adjustments;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private BillingStatus status;
    
    @Column(name = "due_date")
    private LocalDate dueDate;
    
    @Column(name = "paid_date")
    private LocalDate paidDate;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (status == null) status = BillingStatus.ISSUED;
    }
    
    public enum BillingStatus {
        ISSUED,         # 請求書発行済
        PAID,           # 支払済
        OVERDUE,        # 期限超過
        PARTIAL_PAID,   # 一部支払
        CANCELLED       # 取消
    }
}
