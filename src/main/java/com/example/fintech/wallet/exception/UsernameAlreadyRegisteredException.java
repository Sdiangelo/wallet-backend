package com.example.fintech.wallet.exception;

/**
 * Excepción lanzada cuando el nombre de usuario ya está registrado en el sistema.
 */
public class UsernameAlreadyRegisteredException extends RuntimeException {
    public UsernameAlreadyRegisteredException(String message) {
        super(message);
    }
} 