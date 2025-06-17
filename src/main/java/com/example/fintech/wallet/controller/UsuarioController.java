package com.example.fintech.wallet.controller;

import com.example.fintech.wallet.dto.UsuarioRespuestaDTO;
import com.example.fintech.wallet.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador para operaciones del usuario autenticado.
 */
@RestController
@RequestMapping("/api/users/me")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    /**
     * Endpoint para obtener los datos del usuario autenticado.
     */
    @GetMapping
    public UsuarioRespuestaDTO getUsuarioActual(@AuthenticationPrincipal UserDetails userDetails) {
        return usuarioService.obtenerUsuarioPorUsername(userDetails.getUsername());
    }
} 