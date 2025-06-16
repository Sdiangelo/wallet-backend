package com.example.fintech.wallet.repository;

import com.example.fintech.wallet.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 * Repositorio para la entidad Usuario.
 * Permite operaciones CRUD y consultas personalizadas.
 */
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    /**
     * Buscar usuario por email.
     */
    Optional<Usuario> findByEmail(String email);

    /**
     * Buscar usuario por username.
     */
    Optional<Usuario> findByUsername(String username);

    /**
     * Buscar usuario por username o email.
     */
    Optional<Usuario> findByUsernameOrEmail(String username, String email);

    /**
     * Verificar si existe un usuario con un email dado.
     */
    boolean existsByEmail(String email);

    /**
     * Verificar si existe un usuario con un username dado.
     */
    boolean existsByUsername(String username);
} 