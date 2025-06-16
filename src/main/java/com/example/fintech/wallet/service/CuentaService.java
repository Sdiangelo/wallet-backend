package com.example.fintech.wallet.service;

import com.example.fintech.wallet.dto.CuentaRespuestaDTO;
import java.math.BigDecimal;

/**
 * Servicio para la gestión de cuentas bancarias.
 */
public interface CuentaService {

    /**
     * Consulta el saldo de la cuenta asociada a un usuario.
     * @param username nombre de usuario
     * @return saldo de la cuenta
     */
    BigDecimal consultarSaldoPorUsuario(String username);

    /**
     * Obtiene los datos de la cuenta asociada a un usuario.
     * @param username nombre de usuario
     * @return DTO con los datos de la cuenta
     */
    CuentaRespuestaDTO obtenerCuentaPorUsuario(String username);
} 