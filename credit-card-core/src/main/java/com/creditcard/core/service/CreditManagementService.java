package com.creditcard.core.service;

import com.creditcard.core.domain.Member;
import com.creditcard.core.domain.Transaction;
import com.creditcard.core.domain.FraudRule;
import com.creditcard.core.domain.FraudRule.RiskLevel;
import com.creditcard.core.exception.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class CreditManagementService {
    
    @Transactional
    public Transaction authorize(String memberNumber, BigDecimal amount, 
                                  String merchantName, String merchantCategory) {
        
        // 1. Validate member
        Member member = validateMember(memberNumber);
        
        // 2. Check credit limit
        BigDecimal availableCredit = member.getCreditLimit().subtract(member.getCurrentBalance());
        if (availableCredit.compareTo(amount) < 0) {
            throw new CreditLimitExceededException("Credit limit exceeded");
        }
        
        // 3. Check fraud risk
        List<String> fraudAlerts = checkFraudRisk(memberNumber, amount, merchantCategory);
        if (!fraudAlerts.isEmpty()) {
            throw new FraudDetectedException("Fraud detected: " + String.join(", ", fraudAlerts));
        }
        
        // 4. Create transaction
        Transaction tx = new Transaction();
        tx.setTransactionId(generateTransactionId());
        tx.setMemberNumber(memberNumber);
        tx.setType(Transaction.TransactionType.AUTH);
        tx.setAmount(amount);
        tx.setMerchantName(merchantName);
        tx.setMerchantCategory(merchantCategory);
        tx.setAuthorizationCode(generateAuthCode());
        tx.setStatus(Transaction.TransactionStatus.APPROVED);
        tx.setTransactionDatetime(LocalDateTime.now());
        
        // 5. Reserve credit
        member.setCurrentBalance(member.getCurrentBalance().add(amount));
        
        return tx;
    }
    
    private Member validateMember(String memberNumber) {
        Member member = new Member();
        member.setMemberNumber(memberNumber);
        member.setNameKanji("Test User");
        member.setEmail("test@example.com");
        member.setStatus(Member.MemberStatus.ACTIVE);
        member.setCreditLimit(new BigDecimal("500000"));
        member.setCurrentBalance(new BigDecimal("100000"));
        return member;
    }
    
    private List<String> checkFraudRisk(String memberNumber, BigDecimal amount, String merchantCategory) {
        List<String> alerts = new ArrayList<>();
        if (amount.compareTo(new BigDecimal("100000")) > 0) {
            alerts.add("High amount transaction");
        }
        if (merchantCategory != null && 
            (merchantCategory.equalsIgnoreCase("gambling") || 
             merchantCategory.equalsIgnoreCase("casino") ||
             merchantCategory.equalsIgnoreCase("adult"))) {
            alerts.add("Risky merchant category");
        }
        return alerts;
    }
    
    private String generateTransactionId() {
        return "TX" + UUID.randomUUID().toString().substring(0, 9).toUpperCase();
    }
    
    private String generateAuthCode() {
        return String.valueOf((int)(Math.random() * 900000) + 100000);
    }
    
    @Transactional
    public Transaction capture(String transactionId, BigDecimal captureAmount) {
        Transaction tx = new Transaction();
        tx.setTransactionId(transactionId);
        tx.setType(Transaction.TransactionType.CAPTURE);
        tx.setAmount(captureAmount);
        tx.setStatus(Transaction.TransactionStatus.SETTLED);
        return tx;
    }
    
    @Transactional
    public Transaction voidTransaction(String transactionId) {
        Transaction tx = new Transaction();
        tx.setTransactionId(transactionId);
        tx.setStatus(Transaction.TransactionStatus.CANCELLED);
        return tx;
    }
}
