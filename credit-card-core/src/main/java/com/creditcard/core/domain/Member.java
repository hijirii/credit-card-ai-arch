package com.creditcard.core.domain;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 会員管理ドメイン
 * Member Management Domain
 */
@Entity
@Table(name = "members")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "member_number", unique = true, nullable = false)
    private String memberNumber;
    
    @Column(name = "name_kanji", nullable = false)
    private String nameKanji;
    
    @Column(name = "name_kana")
    private String nameKana;
    
    @Column(name = "email")
    private String email;
    
    @Column(name = "phone")
    private String phone;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "member_status")
    private MemberStatus status;
    
    @Column(name = "credit_limit")
    private BigDecimal creditLimit;
    
    @Column(name = "current_balance")
    private BigDecimal currentBalance;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (status == null) status = MemberStatus.PENDING;
        if (creditLimit == null) creditLimit = BigDecimal.ZERO;
        if (currentBalance == null) currentBalance = BigDecimal.ZERO;
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    public enum MemberStatus {
        PENDING,      # 入会審査中
        ACTIVE,       # 有効
        SUSPENDED,    # 停止
        CLOSED        # 退会
    }
}
