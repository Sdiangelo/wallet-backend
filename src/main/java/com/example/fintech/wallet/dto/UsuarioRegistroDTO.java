package com.example.fintech.wallet.dto;

/**
 * DTO para la solicitud de registro de usuario.
 * Contiene los datos necesarios para crear un nuevo usuario.
 */
public class UsuarioRegistroDTO {

    /** Nombre completo del usuario */
    private String nombreCompleto;

    /** Email del usuario */
    private String email;

    /** Nombre de usuario */
    private String username;

    /** Contraseña en texto plano (será cifrada en el backend) */
    private String password;

    // Getters y setters

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
} 