package com.example.fintech.wallet.exception;

/**
 * Excepción lanzada cuando no se encuentra un usuario en el sistema.
 */
public class UsuarioNoEncontradoException extends RuntimeException {
    public UsuarioNoEncontradoException(String mensaje) {
        super(mensaje);
    }
} 