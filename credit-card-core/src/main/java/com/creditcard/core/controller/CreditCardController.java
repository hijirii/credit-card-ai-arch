package com.creditcard.core.controller;

import com.creditcard.core.domain.Transaction;
import com.creditcard.core.service.CreditManagementService;
import lombok.RequiredArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

/**
 * クレジットカード API コントローラー
 * Credit Card API Controller
 */
@RestController
@RequestMapping("/api/v1/credit")
@RequiredArgsConstructor
@Slf4j
public class CreditCardController {
    
    private final CreditManagementService creditService;
    
    /**
     * オーソリゼーション（与信確保）
     * POST /api/v1/credit/authorize
     */
    @PostMapping("/authorize")
    public ResponseEntity<AuthorizationResponse> authorize(
            @RequestBody AuthorizationRequest request) {
        log.info("Received authorization request: {}", request);
        
        try {
            Transaction tx = creditService.authorize(
                    request.getMemberNumber(),
                    request.getAmount(),
                    request.getMerchantName(),
                    request.getMerchantCategory()
            );
            
            AuthorizationResponse response = new AuthorizationResponse();
            response.setSuccess(true);
            response.setTransactionId(tx.getTransactionId());
            response.setAuthorizationCode(tx.getAuthorizationCode());
            response.setStatus(tx.getStatus().name());
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("Authorization failed: {}", e.getMessage());
            AuthorizationResponse response = new AuthorizationResponse();
            response.setSuccess(false);
            response.setErrorMessage(e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * 売上請求（キャプチャ）
     * POST /api/v1/credit/capture
     */
    @PostMapping("/capture")
    public ResponseEntity<CaptureResponse> capture(@RequestBody CaptureRequest request) {
        log.info("Received capture request: {}", request);
        
        try {
            Transaction tx = creditService.capture(request.getTransactionId(), request.getAmount());
            
            CaptureResponse response = new CaptureResponse();
            response.setSuccess(true);
            response.setTransactionId(tx.getTransactionId());
            response.setStatus(tx.getStatus().name());
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("Capture failed: {}", e.getMessage());
            CaptureResponse response = new CaptureResponse();
            response.setSuccess(false);
            response.setErrorMessage(e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * 取引取消
     * POST /api/v1/credit/void
     */
    @PostMapping("/void")
    public ResponseEntity<VoidResponse> voidTransaction(@RequestBody VoidRequest request) {
        log.info("Received void request: {}", request);
        
        try {
            creditService.voidTransaction(request.getTransactionId());
            
            VoidResponse response = new VoidResponse();
            response.setSuccess(true);
            response.setTransactionId(request.getTransactionId());
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("Void failed: {}", e.getMessage());
            VoidResponse response = new VoidResponse();
            response.setSuccess(false);
            response.setErrorMessage(e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    // Request/Response DTOs
    
    @Data
    public static class AuthorizationRequest {
        private String memberNumber;
        private BigDecimal amount;
        private String merchantName;
        private String merchantCategory;
    }
    
    @Data
    public static class AuthorizationResponse {
        private boolean success;
        private String transactionId;
        private String authorizationCode;
        private String status;
        private String errorMessage;
    }
    
    @Data
    public static class CaptureRequest {
        private String transactionId;
        private BigDecimal amount;
    }
    
    @Data
    public static class CaptureResponse {
        private boolean success;
        private String transactionId;
        private String status;
        private String errorMessage;
    }
    
    @Data
    public static class VoidRequest {
        private String transactionId;
    }
    
    @Data
    public static class VoidResponse {
        private boolean success;
        private String transactionId;
        private String errorMessage;
    }
}
