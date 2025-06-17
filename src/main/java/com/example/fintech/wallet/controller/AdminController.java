package com.example.fintech.wallet.controller;

import com.example.fintech.wallet.dto.UsuarioRespuestaDTO;
import com.example.fintech.wallet.dto.TransaccionRespuestaDTO;
import com.example.fintech.wallet.entity.Usuario;
import com.example.fintech.wallet.entity.Transaccion;
import com.example.fintech.wallet.repository.UsuarioRepository;
import com.example.fintech.wallet.repository.TransaccionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Controlador para endpoints de administración (solo ADMIN).
 */
@RestController
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private TransaccionRepository transaccionRepository;

    /**
     * Endpoint para ver todos los usuarios del sistema.
     */
    @GetMapping("/users")
    public List<UsuarioRespuestaDTO> getAllUsers() {
        List<Usuario> usuarios = usuarioRepository.findAll();
        return usuarios.stream().map(u -> {
            UsuarioRespuestaDTO dto = new UsuarioRespuestaDTO();
            dto.setId(u.getId());
            dto.setNombreCompleto(u.getNombreCompleto());
            dto.setEmail(u.getEmail());
            dto.setUsername(u.getUsername());
            dto.setRol(u.getRol());
            dto.setEstado(u.getEstado());
            return dto;
        }).collect(Collectors.toList());
    }

    /**
     * Endpoint para ver todas las transacciones del sistema.
     */
    @GetMapping("/transactions")
    public List<TransaccionRespuestaDTO> getAllTransactions() {
        List<Transaccion> transacciones = transaccionRepository.findAll();
        return transacciones.stream().map(t -> {
            TransaccionRespuestaDTO dto = new TransaccionRespuestaDTO();
            dto.setId(t.getId());
            dto.setCuentaOrigenId(t.getCuentaOrigen().getId());
            dto.setCuentaDestinoId(t.getCuentaDestino().getId());
            dto.setMonto(t.getMonto());
            dto.setFecha(t.getFecha());
            dto.setEstado(t.getEstado());
            dto.setMotivoRechazo(t.getMotivoRechazo());
            return dto;
        }).collect(Collectors.toList());
    }
} 