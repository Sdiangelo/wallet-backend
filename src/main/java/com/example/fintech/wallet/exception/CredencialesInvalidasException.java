package com.example.fintech.wallet.exception;

/**
 * Excepción lanzada cuando las credenciales de login son incorrectas.
 */
public class CredencialesInvalidasException extends RuntimeException {
    public CredencialesInvalidasException(String mensaje) {
        super(mensaje);
    }
} 