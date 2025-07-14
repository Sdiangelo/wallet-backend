package com.example.fintech.wallet.exception;


public class TransactionNotAllowedException extends RuntimeException {
    public TransactionNotAllowedException(String message) {
        super(message);
    }
} 