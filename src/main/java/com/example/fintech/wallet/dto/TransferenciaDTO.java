package com.example.fintech.wallet.dto;

import java.math.BigDecimal;

/**
 * DTO para solicitar una transferencia de dinero entre cuentas.
 */
public class TransferenciaDTO {

    /** ID de la cuenta destino (a la que se transfiere el dinero) */
    private Long cuentaDestinoId;

    /** Monto a transferir */
    private BigDecimal monto;

    // Getters y setters

    public Long getCuentaDestinoId() {
        return cuentaDestinoId;
    }

    public void setCuentaDestinoId(Long cuentaDestinoId) {
        this.cuentaDestinoId = cuentaDestinoId;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }
} 