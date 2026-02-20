package com.creditcard.core.exception;
public class TransactionNotFoundException extends RuntimeException {
    public TransactionNotFoundException(String message) { super(message); }
}
