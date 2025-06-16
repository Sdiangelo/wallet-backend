package com.example.fintech.wallet.dto;

/**
 * DTO para devolver el token JWT tras el login exitoso.
 */
public class JwtRespuestaDTO {

    /** Token JWT generado */
    private String token;

    public JwtRespuestaDTO() {}

    public JwtRespuestaDTO(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
} 