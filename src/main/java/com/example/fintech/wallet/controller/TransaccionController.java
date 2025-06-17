package com.example.fintech.wallet.controller;

import com.example.fintech.wallet.dto.TransferenciaDTO;
import com.example.fintech.wallet.dto.TransaccionRespuestaDTO;
import com.example.fintech.wallet.service.TransaccionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

/**
 * Controlador para operaciones de transacciones del usuario autenticado.
 */
@RestController
@RequestMapping("/api/transactions")
public class TransaccionController {

    @Autowired
    private TransaccionService transaccionService;

    /**
     * Endpoint para transferir dinero a otra cuenta.
     */
    @PostMapping("/transfer")
    public TransaccionRespuestaDTO transferir(@AuthenticationPrincipal UserDetails userDetails,
                                              @Valid @RequestBody TransferenciaDTO transferenciaDTO) {
        return transaccionService.transferir(userDetails.getUsername(), transferenciaDTO);
    }

    /**
     * Endpoint para ver el historial de transacciones del usuario autenticado.
     */
    @GetMapping("/history")
    public List<TransaccionRespuestaDTO> historial(@AuthenticationPrincipal UserDetails userDetails) {
        return transaccionService.obtenerHistorial(userDetails.getUsername());
    }
} 