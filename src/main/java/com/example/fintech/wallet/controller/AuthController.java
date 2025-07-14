package com.example.fintech.wallet.controller;

import com.example.fintech.wallet.dto.UserRegisterDTO;
import com.example.fintech.wallet.dto.UserLoginDTO;
import com.example.fintech.wallet.dto.UserResponseDTO;
import com.example.fintech.wallet.dto.AuthResponseDTO;
import com.example.fintech.wallet.dto.RefreshRequestDTO;
import com.example.fintech.wallet.service.UserService;
import com.example.fintech.wallet.service.RefreshTokenService;
import com.example.fintech.wallet.service.EmailService;
import com.example.fintech.wallet.entity.RefreshToken;
import com.example.fintech.wallet.entity.User;
import com.example.fintech.wallet.repository.UserRepository;
import com.example.fintech.wallet.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> register(@Valid @RequestBody UserRegisterDTO registerDTO) {
        UserResponseDTO user = userService.registerUser(registerDTO);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody UserLoginDTO loginDTO) {
        AuthResponseDTO auth = userService.login(loginDTO);
        return ResponseEntity.ok(auth);
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponseDTO> refresh(@RequestBody RefreshRequestDTO request) {
        String requestRefreshToken = request.getRefreshToken();
        RefreshToken refreshToken = refreshTokenService.findByToken(requestRefreshToken)
                .orElseThrow(() -> new RuntimeException("Refresh token not found"));
        if (refreshTokenService.isRefreshTokenExpired(refreshToken)) {
            refreshTokenService.deleteByUser(refreshToken.getUser());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        User user = refreshToken.getUser();
        org.springframework.security.core.userdetails.User userDetails =
                new org.springframework.security.core.userdetails.User(
                        user.getUsername(), user.getPassword(),
                        java.util.Collections.singletonList(
                                new org.springframework.security.core.authority.SimpleGrantedAuthority("ROLE_" + user.getRole())
                        )
                );
        String newAccessToken = jwtUtil.generateToken(userDetails);
        // Opcional: rotar refresh token (generar uno nuevo y eliminar el anterior)
        refreshTokenService.deleteByUser(user);
        RefreshToken newRefreshToken = refreshTokenService.createRefreshToken(user);
        return ResponseEntity.ok(new AuthResponseDTO(newAccessToken, newRefreshToken.getToken()));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody RefreshRequestDTO request) {
        String requestRefreshToken = request.getRefreshToken();
        RefreshToken refreshToken = refreshTokenService.findByToken(requestRefreshToken)
                .orElse(null);
        if (refreshToken != null) {
            refreshTokenService.deleteByUser(refreshToken.getUser());
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("/resend-verification")
    public ResponseEntity<String> resendVerification(@RequestParam("email") String email) {
        var userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("No existe un usuario con ese correo electrónico.");
        }
        var user = userOpt.get();
        if (user.isEmailVerified()) {
            return ResponseEntity.ok("El correo ya está verificado. Puedes iniciar sesión.");
        }

        String newToken = UUID.randomUUID().toString();
        user.setVerificationToken(newToken);
        user.setVerificationTokenExpiry(java.time.LocalDateTime.now().plusHours(24));
        userRepository.save(user);
        emailService.sendVerificationEmail(user.getEmail(), newToken);
        return ResponseEntity.ok("Se ha reenviado el correo de verificación. Revisa tu bandeja de entrada.");
    }

    @GetMapping("/verify-email")
    public ResponseEntity<String> verifyEmail(@RequestParam("token") String token) {
        var userOpt = userRepository.findAll().stream()
            .filter(u -> token.equals(u.getVerificationToken()))
            .findFirst();
        if (userOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Token inválido o usuario no encontrado.");
        }
        var user = userOpt.get();
        if (user.isEmailVerified()) {
            return ResponseEntity.ok("El correo ya fue verificado.");
        }
        if (user.getVerificationTokenExpiry() == null || user.getVerificationTokenExpiry().isBefore(LocalDateTime.now())) {
            return ResponseEntity.badRequest().body("El token ha expirado. Solicita uno nuevo.");
        }
        user.setEmailVerified(true);
        user.setVerificationToken(null);
        user.setVerificationTokenExpiry(null);
        user.setStatus("active");
        userRepository.save(user);
        return ResponseEntity.ok("¡Correo verificado exitosamente! Ahora puedes iniciar sesión.");
    }
} 