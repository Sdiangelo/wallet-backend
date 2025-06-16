package com.example.fintech.wallet.dto;

/**
 * DTO para la solicitud de login de usuario.
 * Contiene los datos necesarios para autenticarse.
 */
public class UsuarioLoginDTO {

    /** Nombre de usuario o email */
    private String usernameOrEmail;

    /** Contraseña en texto plano */
    private String password;

    // Getters y setters

    public String getUsernameOrEmail() {
        return usernameOrEmail;
    }

    public void setUsernameOrEmail(String usernameOrEmail) {
        this.usernameOrEmail = usernameOrEmail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
} 