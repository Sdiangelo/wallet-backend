package com.example.fintech.wallet.exception;

/**
 * Excepción lanzada cuando el usuario está inactivo y no puede operar.
 */
public class UsuarioInactivoException extends RuntimeException {
    public UsuarioInactivoException(String mensaje) {
        super(mensaje);
    }
} 