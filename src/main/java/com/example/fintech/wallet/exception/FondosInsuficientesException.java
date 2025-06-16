package com.example.fintech.wallet.exception;

/**
 * Excepción lanzada cuando la cuenta no tiene fondos suficientes para una operación.
 */
public class FondosInsuficientesException extends RuntimeException {
    public FondosInsuficientesException(String mensaje) {
        super(mensaje);
    }
} 