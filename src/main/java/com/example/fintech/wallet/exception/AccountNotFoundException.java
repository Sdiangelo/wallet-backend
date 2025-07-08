package com.example.fintech.wallet.exception;

/**
 * Excepción lanzada cuando no se encuentra una cuenta bancaria.
 */
public class AccountNotFoundException extends RuntimeException {
    public AccountNotFoundException(String message) {
        super(message);
    }
} 