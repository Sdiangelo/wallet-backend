package com.example.fintech.wallet.service.impl;

import com.example.fintech.wallet.entity.Usuario;
import com.example.fintech.wallet.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;

/**
 * Implementación de UserDetailsService para cargar usuarios desde la base de datos.
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));

        // Asignar el rol con el prefijo "ROLE_" (Spring Security lo requiere)
        Collection<? extends GrantedAuthority> authorities =
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + usuario.getRol()));

        // Devolver un UserDetails con username, password y roles
        return new org.springframework.security.core.userdetails.User(
                usuario.getUsername(),
                usuario.getPassword(),
                authorities
        );
    }
} 