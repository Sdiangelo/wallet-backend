package com.example.fintech.wallet.service.impl;

import com.example.fintech.wallet.dto.UsuarioRegistroDTO;
import com.example.fintech.wallet.dto.UsuarioLoginDTO;
import com.example.fintech.wallet.dto.UsuarioRespuestaDTO;
import com.example.fintech.wallet.dto.JwtRespuestaDTO;
import com.example.fintech.wallet.entity.Usuario;
import com.example.fintech.wallet.repository.UsuarioRepository;
import com.example.fintech.wallet.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.fintech.wallet.exception.UsuarioNoEncontradoException;
import com.example.fintech.wallet.exception.EmailYaRegistradoException;
import com.example.fintech.wallet.exception.UsernameYaRegistradoException;
import com.example.fintech.wallet.exception.CredencialesInvalidasException;
import com.example.fintech.wallet.exception.UsuarioInactivoException;
import com.example.fintech.wallet.security.JwtUtil;

/**
 * Implementación de UsuarioService.
 * Contiene la lógica de negocio para registro, login y consulta de usuario.
 */
@Service
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Autowired
    public UsuarioServiceImpl(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    /**
     * Registra un nuevo usuario en el sistema.
     * Valida unicidad de email y username, cifra la contraseña y guarda el usuario.
     */
    @Override
    public UsuarioRespuestaDTO registrarUsuario(UsuarioRegistroDTO registroDTO) {
        // 1. Validar unicidad de email y username
        if (usuarioRepository.existsByEmail(registroDTO.getEmail())) {
            throw new EmailYaRegistradoException("El email ya está registrado");
        }
        if (usuarioRepository.existsByUsername(registroDTO.getUsername())) {
            throw new UsernameYaRegistradoException("El nombre de usuario ya está en uso");
        }

        // 2. Cifrar la contraseña
        String passwordCifrada = passwordEncoder.encode(registroDTO.getPassword());

        // 3. Crear el usuario (rol USER, estado activo)
        Usuario usuario = new Usuario();
        usuario.setNombreCompleto(registroDTO.getNombreCompleto());
        usuario.setEmail(registroDTO.getEmail());
        usuario.setUsername(registroDTO.getUsername());
        usuario.setPassword(passwordCifrada);
        usuario.setRol("USER");
        usuario.setEstado("activo");

        // 4. Guardar el usuario
        usuarioRepository.save(usuario);

        // 5. Devolver DTO de respuesta
        UsuarioRespuestaDTO respuesta = new UsuarioRespuestaDTO();
        respuesta.setId(usuario.getId());
        respuesta.setNombreCompleto(usuario.getNombreCompleto());
        respuesta.setEmail(usuario.getEmail());
        respuesta.setUsername(usuario.getUsername());
        respuesta.setRol(usuario.getRol());
        respuesta.setEstado(usuario.getEstado());
        return respuesta;
    }

    /**
     * Autentica un usuario y genera un token JWT si las credenciales son válidas.
     */
    @Override
    public JwtRespuestaDTO login(UsuarioLoginDTO loginDTO) {
        // 1. Buscar usuario por username o email
        Usuario usuario = usuarioRepository.findByUsernameOrEmail(
                loginDTO.getUsernameOrEmail(), loginDTO.getUsernameOrEmail())
                .orElseThrow(() -> new CredencialesInvalidasException("Usuario o contraseña incorrectos"));

        // 2. Verificar contraseña
        if (!passwordEncoder.matches(loginDTO.getPassword(), usuario.getPassword())) {
            throw new CredencialesInvalidasException("Usuario o contraseña incorrectos");
        }

        // 3. Verificar estado activo
        if (!"activo".equalsIgnoreCase(usuario.getEstado())) {
            throw new UsuarioInactivoException("El usuario no está activo");
        }

        // 4. Generar token JWT real
        org.springframework.security.core.userdetails.User userDetails =
                new org.springframework.security.core.userdetails.User(
                        usuario.getUsername(), usuario.getPassword(),
                        java.util.Collections.singletonList(
                                new org.springframework.security.core.authority.SimpleGrantedAuthority("ROLE_" + usuario.getRol())
                        )
                );
        String token = jwtUtil.generateToken(userDetails);

        // 5. Devolver DTO con el token
        return new JwtRespuestaDTO(token);
    }

    /**
     * Obtiene los datos del usuario autenticado.
     */
    @Override
    public UsuarioRespuestaDTO obtenerUsuarioPorUsername(String username) {
        // 1. Buscar usuario por username
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new UsuarioNoEncontradoException("Usuario no encontrado"));

        // 2. Devolver DTO de respuesta
        UsuarioRespuestaDTO respuesta = new UsuarioRespuestaDTO();
        respuesta.setId(usuario.getId());
        respuesta.setNombreCompleto(usuario.getNombreCompleto());
        respuesta.setEmail(usuario.getEmail());
        respuesta.setUsername(usuario.getUsername());
        respuesta.setRol(usuario.getRol());
        respuesta.setEstado(usuario.getEstado());
        return respuesta;
    }
} 