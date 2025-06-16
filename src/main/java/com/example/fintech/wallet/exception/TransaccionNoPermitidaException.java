package com.example.fintech.wallet.exception;

/**
 * Excepción lanzada cuando se intenta realizar una transacción no permitida (por ejemplo, transferir a la misma cuenta).
 */
public class TransaccionNoPermitidaException extends RuntimeException {
    public TransaccionNoPermitidaException(String mensaje) {
        super(mensaje);
    }
} 