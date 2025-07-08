package com.example.fintech.wallet.exception;

/**
 * Excepción lanzada cuando el usuario está inactivo y no puede operar.
 */
public class InactiveUserException extends RuntimeException {
    public InactiveUserException(String message) {
        super(message);
    }
} 