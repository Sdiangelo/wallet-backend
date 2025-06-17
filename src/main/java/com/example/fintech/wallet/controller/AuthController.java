package com.example.fintech.wallet.controller;

import com.example.fintech.wallet.dto.UsuarioRegistroDTO;
import com.example.fintech.wallet.dto.UsuarioLoginDTO;
import com.example.fintech.wallet.dto.UsuarioRespuestaDTO;
import com.example.fintech.wallet.dto.JwtRespuestaDTO;
import com.example.fintech.wallet.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

/**
 * Controlador para registro y login de usuarios.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UsuarioService usuarioService;

    /**
     * Endpoint para registrar un nuevo usuario.
     */
    @PostMapping("/register")
    public ResponseEntity<UsuarioRespuestaDTO> registrar(@Valid @RequestBody UsuarioRegistroDTO registroDTO) {
        UsuarioRespuestaDTO usuario = usuarioService.registrarUsuario(registroDTO);
        return new ResponseEntity<>(usuario, HttpStatus.CREATED);
    }

    /**
     * Endpoint para login de usuario (devuelve JWT).
     */
    @PostMapping("/login")
    public ResponseEntity<JwtRespuestaDTO> login(@Valid @RequestBody UsuarioLoginDTO loginDTO) {
        JwtRespuestaDTO jwt = usuarioService.login(loginDTO);
        return ResponseEntity.ok(jwt);
    }
} 