package com.example.fintech.wallet.entity;

import jakarta.persistence.*;

/**
 * Entidad que representa a un usuario del sistema bancario.
 */
@Entity
@Table(name = "usuarios")
public class Usuario {

    /**
     * Identificador único del usuario (clave primaria).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nombre de usuario único para autenticación.
     */
    @Column(nullable = false, unique = true)
    private String username;

    /**
     * Contraseña cifrada del usuario.
     */
    @Column(nullable = false)
    private String password;

    /**
     * Nombre completo del usuario.
     */
    @Column(nullable = false)
    private String nombreCompleto;

    /**
     * Correo electrónico del usuario.
     */
    @Column(nullable = false, unique = true)
    private String email;

    /**
     * Rol del usuario (USER o ADMIN).
     */
    @Column(nullable = false)
    private String rol;

    /**
     * Estado del usuario (activo/inactivo).
     */
    @Column(nullable = false)
    private String estado;

    /**
     * Relación uno a uno con Cuenta. Cada usuario tiene una sola cuenta.
     */
    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private Cuenta cuenta;

    // Constructores

    public Usuario() {
    }

    public Usuario(String username, String password, String nombreCompleto, String email, String rol, String estado) {
        this.username = username;
        this.password = password;
        this.nombreCompleto = nombreCompleto;
        this.email = email;
        this.rol = rol;
        this.estado = estado;
    }

    // Getters y setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Cuenta getCuenta() {
        return cuenta;
    }

    public void setCuenta(Cuenta cuenta) {
        this.cuenta = cuenta;
    }
} 