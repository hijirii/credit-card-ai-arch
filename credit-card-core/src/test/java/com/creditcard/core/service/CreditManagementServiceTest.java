package com.creditcard.core.service;

import com.creditcard.core.domain.Transaction;
import com.creditcard.core.exception.CreditLimitExceededException;
import com.creditcard.core.exception.FraudDetectedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 与信管理サービス テスト
 * Credit Management Service Test
 */
class CreditManagementServiceTest {
    
    private CreditManagementService creditService;
    
    @BeforeEach
    void setUp() {
        creditService = new CreditManagementService();
    }
    
    @Test
    @DisplayName("正常系：オーソリゼーション成功")
    void testAuthorize_Success() {
        // Given
        String memberNumber = "M123456789";
        BigDecimal amount = new BigDecimal("10000");
        String merchantName = "Amazon Japan";
        String merchantCategory = "retail";
        
        // When
        Transaction result = creditService.authorize(memberNumber, amount, merchantName, merchantCategory);
        
        // Then
        assertNotNull(result);
        assertNotNull(result.getTransactionId());
        assertNotNull(result.getAuthorizationCode());
        assertEquals(Transaction.TransactionStatus.APPROVED, result.getStatus());
        assertEquals(Transaction.TransactionType.AUTH, result.getType());
    }
    
    @Test
    @DisplayName("異常系：与信限度額超過")
    void testAuthorize_CreditLimitExceeded() {
        // Given
        String memberNumber = "M123456789";
        BigDecimal amount = new BigDecimal("1000000"); // 限度額超
        String merchantName = "Amazon Japan";
        String merchantCategory = "retail";
        
        // When & Then
        assertThrows(CreditLimitExceededException.class, () -> {
            creditService.authorize(memberNumber, amount, merchantName, merchantCategory);
        });
    }
    
    @Test
    @DisplayName("異常系：不正検知（高額）」")
    void testAuthorize_FraudDetected_HighAmount() {
        // Given
        String memberNumber = "M123456789";
        BigDecimal amount = new BigDecimal("200000"); // 高額
        String merchantName = "Luxury Store";
        String merchantCategory = "retail";
        
        // When & Then
        assertThrows(FraudDetectedException.class, () -> {
            creditService.authorize(memberNumber, amount, merchantName, merchantCategory);
        });
    }
    
    @Test
    @DisplayName("異常系：不正検知（リスク業種）」")
    void testAuthorize_FraudDetected_RiskyCategory() {
        // Given
        String memberNumber = "M123456789";
        BigDecimal amount = new BigDecimal("50000");
        String merchantName = "Casino";
        String merchantCategory = "gambling";
        
        // When & Then
        assertThrows(FraudDetectedException.class, () -> {
            creditService.authorize(memberNumber, amount, merchantName, merchantCategory);
        });
    }
    
    @Test
    @DisplayName("正常系：取消処理成功")
    void testVoidTransaction_Success() {
        // Given
        String transactionId = "TX20260101123456789";
        
        // When
        Transaction result = creditService.voidTransaction(transactionId);
        
        // Then
        assertNotNull(result);
        assertEquals(Transaction.TransactionStatus.CANCELLED, result.getStatus());
    }
}
