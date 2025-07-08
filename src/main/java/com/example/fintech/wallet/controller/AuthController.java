package com.example.fintech.wallet.controller;

import com.example.fintech.wallet.dto.UserRegisterDTO;
import com.example.fintech.wallet.dto.UserLoginDTO;
import com.example.fintech.wallet.dto.UserResponseDTO;
import com.example.fintech.wallet.dto.AuthResponseDTO;
import com.example.fintech.wallet.dto.RefreshRequestDTO;
import com.example.fintech.wallet.service.UserService;
import com.example.fintech.wallet.service.RefreshTokenService;
import com.example.fintech.wallet.entity.RefreshToken;
import com.example.fintech.wallet.entity.User;
import com.example.fintech.wallet.repository.UserRepository;
import com.example.fintech.wallet.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private RefreshTokenService refreshTokenService;

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
} 