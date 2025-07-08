package com.example.fintech.wallet.exception;

/**
 * Excepción lanzada cuando las credenciales de login son incorrectas.
 */
public class InvalidCredentialsException extends RuntimeException {
    public InvalidCredentialsException(String message) {
        super(message);
    }
} 