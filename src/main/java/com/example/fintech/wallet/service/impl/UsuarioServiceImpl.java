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

/**
 * Implementación de UsuarioService.
 * Contiene la lógica de negocio para registro, login y consulta de usuario.
 */
@Service
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    // Aquí luego inyectaremos el componente para JWT

    @Autowired
    public UsuarioServiceImpl(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Registra un nuevo usuario en el sistema.
     * Valida unicidad de email y username, cifra la contraseña y guarda el usuario.
     */
    @Override
    public UsuarioRespuestaDTO registrarUsuario(UsuarioRegistroDTO registroDTO) {
        // 1. Validar unicidad de email y username
        if (usuarioRepository.existsByEmail(registroDTO.getEmail())) {
            // Excepción personalizada: Email ya registrado
            throw new RuntimeException("El email ya está registrado"); // Reemplazar por EmailYaRegistradoException
        }
        if (usuarioRepository.existsByUsername(registroDTO.getUsername())) {
            // Excepción personalizada: Username ya registrado
            throw new RuntimeException("El nombre de usuario ya está en uso"); // Reemplazar por UsernameYaRegistradoException
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
                .orElseThrow(() -> new RuntimeException("Usuario o contraseña incorrectos")); // Reemplazar por CredencialesInvalidasException

        // 2. Verificar contraseña
        if (!passwordEncoder.matches(loginDTO.getPassword(), usuario.getPassword())) {
            throw new RuntimeException("Usuario o contraseña incorrectos"); // Reemplazar por CredencialesInvalidasException
        }

        // 3. Verificar estado activo
        if (!"activo".equalsIgnoreCase(usuario.getEstado())) {
            throw new RuntimeException("El usuario no está activo"); // Reemplazar por UsuarioInactivoException
        }

        // 4. Generar token JWT (lógica a implementar en la parte de seguridad)
        String token = "JWT_TOKEN_AQUI"; // TODO: Generar JWT real

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
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado")); // Reemplazar por UsuarioNoEncontradoException

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