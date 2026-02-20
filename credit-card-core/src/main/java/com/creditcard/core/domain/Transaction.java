package com.creditcard.core.domain;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 取引管理ドメイン
 * Transaction Management Domain
 */
@Entity
@Table(name = "transactions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "transaction_id", unique = true, nullable = false)
    private String transactionId;
    
    @Column(name = "member_number", nullable = false)
    private String memberNumber;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type", nullable = false)
    private TransactionType type;
    
    @Column(name = "amount", nullable = false)
    private BigDecimal amount;
    
    @Column(name = "currency")
    private String currency;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private TransactionStatus status;
    
    @Column(name = "merchant_name")
    private String merchantName;
    
    @Column(name = "merchant_category")
    private String merchantCategory;
    
    @Column(name = "authorization_code")
    private String authorizationCode;
    
    @Column(name = "settled_amount")
    private BigDecimal settledAmount;
    
    @Column(name = "installment_count")
    private Integer installmentCount;
    
    @Column(name = "transaction_datetime")
    private LocalDateTime transactionDatetime;
    
    @Column(name = "settlement_date")
    private LocalDateTime settlementDate;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (transactionDatetime == null) transactionDatetime = LocalDateTime.now();
        if (status == null) status = TransactionStatus.PENDING;
        if (currency == null) currency = "JPY";
    }
    
    public enum TransactionType {
        AUTH,           # オーソリゼーション（与信確保）
        CAPTURE,        # 売上請求
        REFUND,         # 取消・退款
        PAYMENT,        # 入金（カード支払い）
        CHARGEBACK,     # 退款申出
        INSTALLMENT     # 分割払い
    }
    
    public enum TransactionStatus {
        PENDING,    # 承認待ち
        APPROVED,   # 承認済
        DECLINED,   # 拒否
        SETTLED,    # 清算済
        CANCELLED,  # 取消済
        DISPUTED    # 紛争中
    }
}
