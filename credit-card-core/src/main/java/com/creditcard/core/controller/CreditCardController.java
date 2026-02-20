package com.creditcard.core.controller;

import com.creditcard.core.domain.Transaction;
import com.creditcard.core.service.CreditManagementService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/v1/credit")
public class CreditCardController {
    
    private final CreditManagementService creditService;
    
    public CreditCardController(CreditManagementService creditService) {
        this.creditService = creditService;
    }
    
    @PostMapping("/authorize")
    public ResponseEntity<AuthorizationResponse> authorize(@RequestBody AuthorizationRequest request) {
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
            AuthorizationResponse response = new AuthorizationResponse();
            response.setSuccess(false);
            response.setErrorMessage(e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    // DTOs with getters and setters
    public static class AuthorizationRequest {
        private String memberNumber;
        private BigDecimal amount;
        private String merchantName;
        private String merchantCategory;
        
        public String getMemberNumber() { return memberNumber; }
        public void setMemberNumber(String v) { memberNumber = v; }
        public BigDecimal getAmount() { return amount; }
        public void setAmount(BigDecimal v) { amount = v; }
        public String getMerchantName() { return merchantName; }
        public void setMerchantName(String v) { merchantName = v; }
        public String getMerchantCategory() { return merchantCategory; }
        public void setMerchantCategory(String v) { merchantCategory = v; }
    }
    
    public static class AuthorizationResponse {
        private boolean success;
        private String transactionId;
        private String authorizationCode;
        private String status;
        private String errorMessage;
        
        public boolean isSuccess() { return success; }
        public void setSuccess(boolean v) { success = v; }
        public String getTransactionId() { return transactionId; }
        public void setTransactionId(String v) { transactionId = v; }
        public String getAuthorizationCode() { return authorizationCode; }
        public void setAuthorizationCode(String v) { authorizationCode = v; }
        public String getStatus() { return status; }
        public void setStatus(String v) { status = v; }
        public String getErrorMessage() { return errorMessage; }
        public void setErrorMessage(String v) { errorMessage = v; }
    }
}
