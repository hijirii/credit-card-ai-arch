package com.creditcard.core.domain;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 不正検知ドメイン
 * Fraud Detection Domain
 */
@Entity
@Table(name = "fraud_rules")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FraudRule {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "rule_id", unique = true, nullable = false)
    private String ruleId;
    
    @Column(name = "rule_name", nullable = false)
    private String ruleName;
    
    @Column(name = "rule_type", nullable = false)
    private String ruleType;
    
    @Column(name = "threshold_amount")
    private BigDecimal thresholdAmount;
    
    @Column(name = "threshold_count")
    private Integer thresholdCount;
    
    @Column(name = "time_window_minutes")
    private Integer timeWindowMinutes;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "risk_level")
    private RiskLevel riskLevel;
    
    @Column(name = "is_active")
    private Boolean isActive;
    
    @Column(name = "description")
    private String description;
    
    @PrePersist
    protected void onCreate() {
        if (isActive == null) isActive = true;
    }
    
    public enum RiskLevel {
        LOW,
        MEDIUM,
        HIGH,
        CRITICAL
    }
}
