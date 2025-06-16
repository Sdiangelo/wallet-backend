package com.example.fintech.wallet.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entidad que representa una transacción bancaria entre cuentas.
 */
@Entity
@Table(name = "transacciones")
public class Transaccion {

    /**
     * Identificador único de la transacción (clave primaria).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Cuenta origen de la transacción.
     */
    @ManyToOne(optional = false)
    @JoinColumn(name = "cuenta_origen_id")
    private Cuenta cuentaOrigen;

    /**
     * Cuenta destino de la transacción.
     */
    @ManyToOne(optional = false)
    @JoinColumn(name = "cuenta_destino_id")
    private Cuenta cuentaDestino;

    /**
     * Monto transferido.
     */
    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal monto;

    /**
     * Fecha y hora de la transacción.
     */
    @Column(nullable = false)
    private LocalDateTime fecha;

    /**
     * Estado de la transacción (PENDIENTE, COMPLETADA, RECHAZADA).
     */
    @Column(nullable = false)
    private String estado;

    /**
     * Motivo de rechazo (opcional, solo si la transacción fue rechazada).
     */
    private String motivoRechazo;

    // Constructores

    public Transaccion() {
    }

    public Transaccion(Cuenta cuentaOrigen, Cuenta cuentaDestino, BigDecimal monto, LocalDateTime fecha, String estado, String motivoRechazo) {
        this.cuentaOrigen = cuentaOrigen;
        this.cuentaDestino = cuentaDestino;
        this.monto = monto;
        this.fecha = fecha;
        this.estado = estado;
        this.motivoRechazo = motivoRechazo;
    }

    // Getters y setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Cuenta getCuentaOrigen() {
        return cuentaOrigen;
    }

    public void setCuentaOrigen(Cuenta cuentaOrigen) {
        this.cuentaOrigen = cuentaOrigen;
    }

    public Cuenta getCuentaDestino() {
        return cuentaDestino;
    }

    public void setCuentaDestino(Cuenta cuentaDestino) {
        this.cuentaDestino = cuentaDestino;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getMotivoRechazo() {
        return motivoRechazo;
    }

    public void setMotivoRechazo(String motivoRechazo) {
        this.motivoRechazo = motivoRechazo;
    }
} 