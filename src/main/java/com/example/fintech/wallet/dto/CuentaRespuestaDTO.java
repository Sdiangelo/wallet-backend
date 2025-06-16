package com.example.fintech.wallet.dto;

import java.math.BigDecimal;

/**
 * DTO para devolver información de la cuenta bancaria al cliente.
 */
public class CuentaRespuestaDTO {

    private Long id;
    private BigDecimal saldo;
    private Long usuarioId;

    // Getters y setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }
} 