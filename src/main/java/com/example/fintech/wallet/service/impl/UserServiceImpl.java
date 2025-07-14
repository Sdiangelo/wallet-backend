package com.example.fintech.wallet.service.impl;

import com.example.fintech.wallet.dto.*;
import com.example.fintech.wallet.entity.User;
import com.example.fintech.wallet.entity.Account;
import com.example.fintech.wallet.repository.UserRepository;
import com.example.fintech.wallet.repository.AccountRepository;
import com.example.fintech.wallet.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.fintech.wallet.exception.UserNotFoundException;
import com.example.fintech.wallet.exception.EmailAlreadyRegisteredException;
import com.example.fintech.wallet.exception.UsernameAlreadyRegisteredException;
import com.example.fintech.wallet.exception.InvalidCredentialsException;
import com.example.fintech.wallet.exception.InactiveUserException;
import com.example.fintech.wallet.security.JwtUtil;
import java.math.BigDecimal;
import com.example.fintech.wallet.service.RefreshTokenService;
import com.example.fintech.wallet.entity.RefreshToken;
import java.util.UUID;
import java.time.LocalDateTime;
import com.example.fintech.wallet.service.EmailService;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AccountRepository accountRepository;
    private final RefreshTokenService refreshTokenService;
    private final EmailService emailService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil, AccountRepository accountRepository, RefreshTokenService refreshTokenService, EmailService emailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.accountRepository = accountRepository;
        this.refreshTokenService = refreshTokenService;
        this.emailService = emailService;
    }

    @Override
    public UserResponseDTO registerUser(UserRegisterDTO registerDTO) {
        String[] allowedDomains = {"gmail.com", "yahoo.com", "outlook.com", "hotmail.com", "protonmail.com"};
        String email = registerDTO.getEmail();
        String domain = email.substring(email.indexOf("@") + 1).toLowerCase();
        boolean validDomain = java.util.Arrays.stream(allowedDomains).anyMatch(domain::equals);
        if (!validDomain) {
            throw new IllegalArgumentException("Email domain is not allowed. Allowed domains: gmail, yahoo, outlook, hotmail, protonmail");
        }
        if (userRepository.existsByEmail(registerDTO.getEmail())) {
            throw new EmailAlreadyRegisteredException("Email is already registered");
        }
        if (userRepository.existsByUsername(registerDTO.getUsername())) {
            throw new UsernameAlreadyRegisteredException("Username is already in use");
        }
        String encodedPassword = passwordEncoder.encode(registerDTO.getPassword());
        User user = new User();
        user.setFullName(registerDTO.getFullName());
        user.setEmail(registerDTO.getEmail());
        user.setUsername(registerDTO.getUsername());
        user.setPassword(encodedPassword);
        user.setRole("USER");
        user.setStatus("pending");
        String verificationToken = UUID.randomUUID().toString();
        user.setVerificationToken(verificationToken);
        user.setVerificationTokenExpiry(LocalDateTime.now().plusHours(24));
        userRepository.save(user);
        emailService.sendVerificationEmail(user.getEmail(), user.getVerificationToken());
        Account account = new Account();
        account.setUser(user);
        account.setBalance(BigDecimal.valueOf(500));
        accountRepository.save(account);
        user.setAccount(account);
        userRepository.save(user);
        UserResponseDTO response = new UserResponseDTO();
        response.setId(user.getId());
        response.setFullName(user.getFullName());
        response.setEmail(user.getEmail());
        response.setUsername(user.getUsername());
        response.setRole(user.getRole());
        response.setStatus(user.getStatus());
        response.setMessage("Te enviamos un correo para verificar tu cuenta. Por favor, revisa tu bandeja de entrada.");
        return response;
    }


    @Override
    public AuthResponseDTO login(UserLoginDTO loginDTO) {
        User user = userRepository.findByUsernameOrEmail(
                loginDTO.getUsernameOrEmail(), loginDTO.getUsernameOrEmail())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid username or password"));
        if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid username or password");
        }
        // Cambia la lógica: si está pending y no verificado, mensaje claro; si está inactive, mensaje actual
        if ("pending".equalsIgnoreCase(user.getStatus()) && !user.isEmailVerified()) {
            throw new InvalidCredentialsException("Debes verificar tu correo electrónico antes de iniciar sesión.");
        }
        if ("inactive".equalsIgnoreCase(user.getStatus())) {
            throw new InactiveUserException("User is not active");
        }
        if (!user.isEmailVerified()) {
            throw new InvalidCredentialsException("Debes verificar tu correo electrónico antes de iniciar sesión.");
        }
        org.springframework.security.core.userdetails.User userDetails =
                new org.springframework.security.core.userdetails.User(
                        user.getUsername(), user.getPassword(),
                        java.util.Collections.singletonList(
                                new org.springframework.security.core.authority.SimpleGrantedAuthority("ROLE_" + user.getRole())
                        )
                );
        String accessToken = jwtUtil.generateToken(userDetails);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);
        return new AuthResponseDTO(accessToken, refreshToken.getToken());
    }


    @Override
    public UserResponseDTO getUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        UserResponseDTO response = new UserResponseDTO();
        response.setId(user.getId());
        response.setFullName(user.getFullName());
        response.setEmail(user.getEmail());
        response.setUsername(user.getUsername());
        response.setRole(user.getRole());
        response.setStatus(user.getStatus());
        return response;
    }
} 