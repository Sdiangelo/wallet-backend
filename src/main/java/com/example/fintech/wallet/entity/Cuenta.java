package com.example.fintech.wallet.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

/**
 * Entidad que representa la cuenta bancaria de un usuario.
 */
@Entity
@Table(name = "cuentas")
public class Cuenta {

    /**
     * Identificador único de la cuenta (clave primaria).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Relación uno a uno con Usuario. Cada cuenta pertenece a un usuario.
     */
    @OneToOne
    @JoinColumn(name = "usuario_id", nullable = false, unique = true)
    private Usuario usuario;

    /**
     * Saldo actual de la cuenta.
     */
    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal saldo;

    // Constructores

    public Cuenta() {
    }

    public Cuenta(Usuario usuario, BigDecimal saldo) {
        this.usuario = usuario;
        this.saldo = saldo;
    }

    // Getters y setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }
} 