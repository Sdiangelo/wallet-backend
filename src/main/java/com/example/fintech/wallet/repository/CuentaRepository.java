package com.example.fintech.wallet.repository;

import com.example.fintech.wallet.entity.Cuenta;
import com.example.fintech.wallet.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 * Repositorio para la entidad Cuenta.
 * Permite operaciones CRUD y consultas personalizadas.
 */
public interface CuentaRepository extends JpaRepository<Cuenta, Long> {

    /**
     * Buscar cuenta por usuario.
     */
    Optional<Cuenta> findByUsuario(Usuario usuario);
} 