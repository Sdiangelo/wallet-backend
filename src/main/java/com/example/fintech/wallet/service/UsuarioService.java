package com.example.fintech.wallet.service;

import com.example.fintech.wallet.dto.UsuarioRegistroDTO;
import com.example.fintech.wallet.dto.UsuarioLoginDTO;
import com.example.fintech.wallet.dto.UsuarioRespuestaDTO;
import com.example.fintech.wallet.dto.JwtRespuestaDTO;

/**
 * Servicio para la gestión de usuarios: registro, login y consulta de datos.
 */
public interface UsuarioService {

    /**
     * Registra un nuevo usuario en el sistema.
     * @param registroDTO datos del usuario a registrar
     * @return datos del usuario registrado
     */
    UsuarioRespuestaDTO registrarUsuario(UsuarioRegistroDTO registroDTO);

    /**
     * Autentica un usuario y genera un token JWT si las credenciales son válidas.
     * @param loginDTO datos de login
     * @return token JWT
     */
    JwtRespuestaDTO login(UsuarioLoginDTO loginDTO);

    /**
     * Obtiene los datos del usuario autenticado.
     * @param username nombre de usuario
     * @return datos del usuario
     */
    UsuarioRespuestaDTO obtenerUsuarioPorUsername(String username);
} 