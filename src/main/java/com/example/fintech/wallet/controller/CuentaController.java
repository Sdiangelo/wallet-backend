package com.example.fintech.wallet.controller;

import com.example.fintech.wallet.dto.CuentaRespuestaDTO;
import com.example.fintech.wallet.service.CuentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

/**
 * Controlador para operaciones sobre la cuenta del usuario autenticado.
 */
@RestController
@RequestMapping("/api/accounts/me")
public class CuentaController {

    @Autowired
    private CuentaService cuentaService;

    /**
     * Endpoint para consultar el saldo de la cuenta del usuario autenticado.
     */
    @GetMapping("/saldo")
    public BigDecimal getSaldo(@AuthenticationPrincipal UserDetails userDetails) {
        return cuentaService.consultarSaldoPorUsuario(userDetails.getUsername());
    }

    /**
     * Endpoint para obtener los datos de la cuenta del usuario autenticado.
     */
    @GetMapping
    public CuentaRespuestaDTO getCuenta(@AuthenticationPrincipal UserDetails userDetails) {
        return cuentaService.obtenerCuentaPorUsuario(userDetails.getUsername());
    }
} 