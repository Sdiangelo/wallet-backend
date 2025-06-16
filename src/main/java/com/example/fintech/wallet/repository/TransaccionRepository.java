package com.example.fintech.wallet.repository;

import com.example.fintech.wallet.entity.Transaccion;
import com.example.fintech.wallet.entity.Cuenta;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * Repositorio para la entidad Transaccion.
 * Permite operaciones CRUD y consultas personalizadas.
 */
public interface TransaccionRepository extends JpaRepository<Transaccion, Long> {

    /**
     * Buscar transacciones donde la cuenta es origen.
     */
    List<Transaccion> findByCuentaOrigen(Cuenta cuentaOrigen);

    /**
     * Buscar transacciones donde la cuenta es destino.
     */
    List<Transaccion> findByCuentaDestino(Cuenta cuentaDestino);

    /**
     * Buscar todas las transacciones de una cuenta (origen o destino).
     */
    List<Transaccion> findByCuentaOrigenOrCuentaDestino(Cuenta cuentaOrigen, Cuenta cuentaDestino);
} 