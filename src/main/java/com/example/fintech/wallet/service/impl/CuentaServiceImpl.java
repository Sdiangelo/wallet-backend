package com.example.fintech.wallet.service.impl;

import com.example.fintech.wallet.dto.CuentaRespuestaDTO;
import com.example.fintech.wallet.entity.Cuenta;
import com.example.fintech.wallet.entity.Usuario;
import com.example.fintech.wallet.repository.CuentaRepository;
import com.example.fintech.wallet.repository.UsuarioRepository;
import com.example.fintech.wallet.service.CuentaService;
import com.example.fintech.wallet.exception.UsuarioNoEncontradoException;
import com.example.fintech.wallet.exception.CuentaNoEncontradaException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Implementación de CuentaService.
 * Contiene la lógica de negocio para consultar saldo y obtener datos de cuenta.
 */
@Service
public class CuentaServiceImpl implements CuentaService {

    private final CuentaRepository cuentaRepository;
    private final UsuarioRepository usuarioRepository;

    @Autowired
    public CuentaServiceImpl(CuentaRepository cuentaRepository, UsuarioRepository usuarioRepository) {
        this.cuentaRepository = cuentaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * Consulta el saldo de la cuenta asociada a un usuario.
     */
    @Override
    public BigDecimal consultarSaldoPorUsuario(String username) {
        // 1. Buscar usuario por username
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new UsuarioNoEncontradoException("Usuario no encontrado"));

        // 2. Buscar cuenta por usuario
        Cuenta cuenta = cuentaRepository.findByUsuario(usuario)
                .orElseThrow(() -> new CuentaNoEncontradaException("Cuenta no encontrada"));

        // 3. Devolver saldo
        return cuenta.getSaldo();
    }

    /**
     * Obtiene los datos de la cuenta asociada a un usuario.
     */
    @Override
    public CuentaRespuestaDTO obtenerCuentaPorUsuario(String username) {
        // 1. Buscar usuario por username
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new UsuarioNoEncontradoException("Usuario no encontrado"));

        // 2. Buscar cuenta por usuario
        Cuenta cuenta = cuentaRepository.findByUsuario(usuario)
                .orElseThrow(() -> new CuentaNoEncontradaException("Cuenta no encontrada"));

        // 3. Mapear a DTO y devolver
        CuentaRespuestaDTO dto = new CuentaRespuestaDTO();
        dto.setId(cuenta.getId());
        dto.setSaldo(cuenta.getSaldo());
        dto.setUsuarioId(usuario.getId());
        return dto;
    }
} 