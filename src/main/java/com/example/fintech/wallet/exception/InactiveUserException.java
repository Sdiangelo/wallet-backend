package com.example.fintech.wallet.exception;


public class InactiveUserException extends RuntimeException {
    public InactiveUserException(String message) {
        super(message);
    }
} 