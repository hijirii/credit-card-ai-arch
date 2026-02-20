package com.creditcard.core.service;

import com.creditcard.core.domain.Member;
import com.creditcard.core.domain.Transaction;
import com.creditcard.core.domain.FraudRule;
import com.creditcard.core.domain.FraudRule.RiskLevel;
import com.creditcard.core.exception.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 与信管理サービス
 * Credit Management Service
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CreditManagementService {
    
    /**
     * オーソリゼーション（与信確保）処理
     * Authorization - Reserve credit for a transaction
     */
    @Transactional
    public Transaction authorize(String memberNumber, BigDecimal amount, 
                                  String merchantName, String merchantCategory) {
        log.info("Authorization request: member={}, amount={}", memberNumber, amount);
        
        // 1. 会員存在チェック
        Member member = validateMember(memberNumber);
        
        // 2. 与信限度額チェック
        BigDecimal availableCredit = member.getCreditLimit().subtract(member.getCurrentBalance());
        if (availableCredit.compareTo(amount) < 0) {
            log.warn("Credit limit exceeded: available={}, requested={}", availableCredit, amount);
            throw new CreditLimitExceededException("与信限度額を超過しました");
        }
        
        // 3. 不正検知チェック
        List<String> fraudAlerts = checkFraudRisk(memberNumber, amount, merchantCategory);
        if (!fraudAlerts.isEmpty()) {
            log.warn("Fraud alerts detected: {}", fraudAlerts);
            throw new FraudDetectedException("不正検知されました: " + String.join(", ", fraudAlerts));
        }
        
        // 4. オーソリゼーション生成
        Transaction tx = Transaction.builder()
                .transactionId(generateTransactionId())
                .memberNumber(memberNumber)
                .type(Transaction.TransactionType.AUTH)
                .amount(amount)
                .merchantName(merchantName)
                .merchantCategory(merchantCategory)
                .authorizationCode(generateAuthCode())
                .status(Transaction.TransactionStatus.APPROVED)
                .transactionDatetime(LocalDateTime.now())
                .build();
        
        // 5. 与信確保（利用可能枠減少）
        member.setCurrentBalance(member.getCurrentBalance().add(amount));
        
        log.info("Authorization approved: txId={}, authCode={}", 
                tx.getTransactionId(), tx.getAuthorizationCode());
        
        return tx;
    }
    
    /**
     * 売上請求（キャプチャ）処理
     * Capture - Confirm the authorized transaction
     */
    @Transactional
    public Transaction capture(String transactionId, BigDecimal captureAmount) {
        log.info("Capture request: txId={}, amount={}", transactionId, captureAmount);
        
        // 実装省略（ authorize からの確定処理）
        return null;
    }
    
    /**
     * 取消処理
     * Void/Cancel an authorized transaction
     */
    @Transactional
    public Transaction voidTransaction(String transactionId) {
        log.info("Void request: txId={}", transactionId);
        
        // 実装省略
        return null;
    }
    
    /**
     * 会員 validation
     */
    private Member validateMember(String memberNumber) {
        // 實際にはRepositoryから取得
        // 簡略化のためダミー実装
        return Member.builder()
                .memberNumber(memberNumber)
                .creditLimit(new BigDecimal("500000"))
                .currentBalance(new BigDecimal("100000"))
                .status(Member.MemberStatus.ACTIVE)
                .build();
    }
    
    /**
     * 不正リスクチェック
     */
    private List<String> checkFraudRisk(String memberNumber, BigDecimal amount, 
                                         String merchantCategory) {
        List<String> alerts = new ArrayList<>();
        
        // 1. 高額チェック
        if (amount.compareTo(new BigDecimal("100000")) > 0) {
            alerts.add("高額の取引です");
        }
        
        // 2. 短時間多頻度チェック（実装省略）
        
        // 3. 異常にカテゴリーチェック
        if (" gambling ".equalsIgnoreCase(merchantCategory) || 
            " casino ".equalsIgnoreCase(merchantCategory)) {
            alerts.add("リスクの高い業種です");
        }
        
        return alerts;
    }
    
    /**
     * 取引ID生成
     */
    private String generateTransactionId() {
        return "TX" + LocalDateTime.now().getYear() + 
               String.format("%05d", LocalDateTime.now().toLocalTime().toSecondOfDay()) +
               UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
    
    /**
     * 承認番号生成
     */
    private String generateAuthCode() {
        return String.format("%06d", (int)(Math.random() * 999999));
    }
}
