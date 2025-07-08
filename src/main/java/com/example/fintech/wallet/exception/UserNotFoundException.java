package com.example.fintech.wallet.exception;

/**
 * Excepción lanzada cuando no se encuentra un usuario en el sistema.
 */
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
} 