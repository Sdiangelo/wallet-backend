package com.example.fintech.wallet.exception;

/**
 * Excepción lanzada cuando el email ya está registrado en el sistema.
 */
public class EmailAlreadyRegisteredException extends RuntimeException {
    public EmailAlreadyRegisteredException(String message) {
        super(message);
    }
} 