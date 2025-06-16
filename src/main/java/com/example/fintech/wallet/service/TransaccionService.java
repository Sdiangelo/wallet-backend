package com.example.fintech.wallet.service;

import com.example.fintech.wallet.dto.TransferenciaDTO;
import com.example.fintech.wallet.dto.TransaccionRespuestaDTO;
import java.util.List;

/**
 * Servicio para la gestión de transacciones bancarias.
 */
public interface TransaccionService {

    /**
     * Realiza una transferencia de dinero desde el usuario autenticado a otra cuenta.
     * @param username nombre de usuario que transfiere
     * @param transferenciaDTO datos de la transferencia
     * @return DTO con la información de la transacción realizada
     */
    TransaccionRespuestaDTO transferir(String username, TransferenciaDTO transferenciaDTO);

    /**
     * Obtiene el historial de transacciones de la cuenta del usuario.
     * @param username nombre de usuario
     * @return lista de transacciones (enviadas y recibidas)
     */
    List<TransaccionRespuestaDTO> obtenerHistorial(String username);
} 