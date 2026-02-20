package com.creditcard.core.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "fraud_rules")
public class FraudRule {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "rule_name", nullable = false)
    private String ruleName;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "risk_level", nullable = false)
    private RiskLevel riskLevel;
    
    @Column(precision = 10, scale = 2)
    private BigDecimal threshold;
    
    @Column(nullable = false)
    private Boolean enabled;
    
    public enum RiskLevel {
        LOW, MEDIUM, HIGH, CRITICAL
    }
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getRuleName() { return ruleName; }
    public void setRuleName(String ruleName) { this.ruleName = ruleName; }
    public RiskLevel getRiskLevel() { return riskLevel; }
    public void setRiskLevel(RiskLevel riskLevel) { this.riskLevel = riskLevel; }
    public BigDecimal getThreshold() { return threshold; }
    public void setThreshold(BigDecimal threshold) { this.threshold = threshold; }
    public Boolean getEnabled() { return enabled; }
    public void setEnabled(Boolean enabled) { this.enabled = enabled; }
}
