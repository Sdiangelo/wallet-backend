package com.example.fintech.wallet.exception;

/**
 * Excepción lanzada cuando el nombre de usuario ya está registrado en el sistema.
 */
public class UsernameYaRegistradoException extends RuntimeException {
    public UsernameYaRegistradoException(String mensaje) {
        super(mensaje);
    }
} 