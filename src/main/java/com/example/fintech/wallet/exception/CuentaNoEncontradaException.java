package com.example.fintech.wallet.exception;

/**
 * Excepción lanzada cuando no se encuentra una cuenta bancaria.
 */
public class CuentaNoEncontradaException extends RuntimeException {
    public CuentaNoEncontradaException(String mensaje) {
        super(mensaje);
    }
} 