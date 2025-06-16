package com.example.fintech.wallet.service.impl;

import com.example.fintech.wallet.dto.TransferenciaDTO;
import com.example.fintech.wallet.dto.TransaccionRespuestaDTO;
import com.example.fintech.wallet.entity.Cuenta;
import com.example.fintech.wallet.entity.Transaccion;
import com.example.fintech.wallet.entity.Usuario;
import com.example.fintech.wallet.repository.CuentaRepository;
import com.example.fintech.wallet.repository.TransaccionRepository;
import com.example.fintech.wallet.repository.UsuarioRepository;
import com.example.fintech.wallet.service.TransaccionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementación de TransaccionService.
 * Contiene la lógica de negocio para transferencias y consulta de historial.
 */
@Service
public class TransaccionServiceImpl implements TransaccionService {

    private final UsuarioRepository usuarioRepository;
    private final CuentaRepository cuentaRepository;
    private final TransaccionRepository transaccionRepository;

    @Autowired
    public TransaccionServiceImpl(UsuarioRepository usuarioRepository,
                                  CuentaRepository cuentaRepository,
                                  TransaccionRepository transaccionRepository) {
        this.usuarioRepository = usuarioRepository;
        this.cuentaRepository = cuentaRepository;
        this.transaccionRepository = transaccionRepository;
    }

    /**
     * Realiza una transferencia de dinero desde el usuario autenticado a otra cuenta.
     */
    @Override
    @Transactional
    public TransaccionRespuestaDTO transferir(String username, TransferenciaDTO transferenciaDTO) {
        // 1. Buscar usuario y cuenta origen
        Usuario usuarioOrigen = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        Cuenta cuentaOrigen = cuentaRepository.findByUsuario(usuarioOrigen)
                .orElseThrow(() -> new RuntimeException("Cuenta de origen no encontrada"));

        // 2. Buscar cuenta destino
        Cuenta cuentaDestino = cuentaRepository.findById(transferenciaDTO.getCuentaDestinoId())
                .orElseThrow(() -> new RuntimeException("Cuenta de destino no encontrada"));

        // 3. Validar que la cuenta destino no sea la misma que la origen
        if (cuentaOrigen.getId().equals(cuentaDestino.getId())) {
            return crearTransaccionFallida(cuentaOrigen, cuentaDestino, transferenciaDTO.getMonto(), "No puedes transferir a tu propia cuenta");
        }

        // 4. Validar fondos suficientes
        if (cuentaOrigen.getSaldo().compareTo(transferenciaDTO.getMonto()) < 0) {
            return crearTransaccionFallida(cuentaOrigen, cuentaDestino, transferenciaDTO.getMonto(), "Fondos insuficientes");
        }

        // 5. Realizar la transferencia
        cuentaOrigen.setSaldo(cuentaOrigen.getSaldo().subtract(transferenciaDTO.getMonto()));
        cuentaDestino.setSaldo(cuentaDestino.getSaldo().add(transferenciaDTO.getMonto()));
        cuentaRepository.save(cuentaOrigen);
        cuentaRepository.save(cuentaDestino);

        // 6. Registrar la transacción como COMPLETADA
        Transaccion transaccion = new Transaccion();
        transaccion.setCuentaOrigen(cuentaOrigen);
        transaccion.setCuentaDestino(cuentaDestino);
        transaccion.setMonto(transferenciaDTO.getMonto());
        transaccion.setFecha(LocalDateTime.now());
        transaccion.setEstado("COMPLETADA");
        transaccion.setMotivoRechazo(null);
        transaccionRepository.save(transaccion);

        // 7. Devolver DTO de respuesta
        return mapearTransaccionADTO(transaccion);
    }

    /**
     * Obtiene el historial de transacciones de la cuenta del usuario.
     */
    @Override
    public List<TransaccionRespuestaDTO> obtenerHistorial(String username) {
        // 1. Buscar usuario y cuenta
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        Cuenta cuenta = cuentaRepository.findByUsuario(usuario)
                .orElseThrow(() -> new RuntimeException("Cuenta no encontrada"));

        // 2. Buscar transacciones donde la cuenta es origen o destino
        List<Transaccion> transacciones = transaccionRepository.findByCuentaOrigenOrCuentaDestino(cuenta, cuenta);

        // 3. Mapear a DTOs
        List<TransaccionRespuestaDTO> historial = new ArrayList<>();
        for (Transaccion t : transacciones) {
            historial.add(mapearTransaccionADTO(t));
        }
        return historial;
    }

    /**
     * Crea y guarda una transacción fallida (RECHAZADA) y devuelve el DTO.
     */
    private TransaccionRespuestaDTO crearTransaccionFallida(Cuenta cuentaOrigen, Cuenta cuentaDestino, BigDecimal monto, String motivo) {
        Transaccion transaccion = new Transaccion();
        transaccion.setCuentaOrigen(cuentaOrigen);
        transaccion.setCuentaDestino(cuentaDestino);
        transaccion.setMonto(monto);
        transaccion.setFecha(LocalDateTime.now());
        transaccion.setEstado("RECHAZADA");
        transaccion.setMotivoRechazo(motivo);
        transaccionRepository.save(transaccion);
        return mapearTransaccionADTO(transaccion);
    }

    /**
     * Mapea una entidad Transaccion a su DTO de respuesta.
     */
    private TransaccionRespuestaDTO mapearTransaccionADTO(Transaccion t) {
        TransaccionRespuestaDTO dto = new TransaccionRespuestaDTO();
        dto.setId(t.getId());
        dto.setCuentaOrigenId(t.getCuentaOrigen().getId());
        dto.setCuentaDestinoId(t.getCuentaDestino().getId());
        dto.setMonto(t.getMonto());
        dto.setFecha(t.getFecha());
        dto.setEstado(t.getEstado());
        dto.setMotivoRechazo(t.getMotivoRechazo());
        return dto;
    }
} 