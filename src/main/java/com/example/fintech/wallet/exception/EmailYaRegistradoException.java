package com.example.fintech.wallet.exception;

/**
 * Excepción lanzada cuando el email ya está registrado en el sistema.
 */
public class EmailYaRegistradoException extends RuntimeException {
    public EmailYaRegistradoException(String mensaje) {
        super(mensaje);
    }
} 