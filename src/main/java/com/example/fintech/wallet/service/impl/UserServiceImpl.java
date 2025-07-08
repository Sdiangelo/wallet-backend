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

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AccountRepository accountRepository;
    private final RefreshTokenService refreshTokenService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil, AccountRepository accountRepository, RefreshTokenService refreshTokenService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.accountRepository = accountRepository;
        this.refreshTokenService = refreshTokenService;
    }

    /**
     * Registers a new user in the system.
     * Validates uniqueness of email and username, encrypts the password and saves the user.
     */
    @Override
    public UserResponseDTO registerUser(UserRegisterDTO registerDTO) {
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
        user.setStatus("active");
        userRepository.save(user);
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
        return response;
    }

    /**
     * Authenticates a user and generates a JWT token if the credentials are valid.
     */
    @Override
    public AuthResponseDTO login(UserLoginDTO loginDTO) {
        User user = userRepository.findByUsernameOrEmail(
                loginDTO.getUsernameOrEmail(), loginDTO.getUsernameOrEmail())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid username or password"));
        if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid username or password");
        }
        if (!"active".equalsIgnoreCase(user.getStatus())) {
            throw new InactiveUserException("User is not active");
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

    /**
     * Gets the data of the authenticated user.
     */
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