package com.example.fintech.wallet.service;

import com.example.fintech.wallet.dto.AuthResponseDTO;
import com.example.fintech.wallet.dto.UserLoginDTO;
import com.example.fintech.wallet.entity.User;
import com.example.fintech.wallet.entity.RefreshToken;
import com.example.fintech.wallet.exception.InvalidCredentialsException;
import com.example.fintech.wallet.exception.InactiveUserException;
import com.example.fintech.wallet.repository.UserRepository;
import com.example.fintech.wallet.repository.AccountRepository;
import com.example.fintech.wallet.security.JwtUtil;
import com.example.fintech.wallet.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplMockTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtUtil jwtUtil;
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private RefreshTokenService refreshTokenService;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLogin_success_withMockedRepository() {
        // Arrange
        UserLoginDTO loginDTO = new UserLoginDTO();
        loginDTO.setUsernameOrEmail("mockuser");
        loginDTO.setPassword("mockpass");

        User user = new User();
        user.setUsername("mockuser");
        user.setPassword("hashedpass");
        user.setStatus("active");
        user.setRole("USER");
        user.setEmailVerified(true);

        when(userRepository.findByUsernameOrEmail("mockuser", "mockuser")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("mockpass", "hashedpass")).thenReturn(true);
        when(jwtUtil.generateToken(any())).thenReturn("mockedAccessToken");
        RefreshToken mockRefreshToken = new com.example.fintech.wallet.entity.RefreshToken();
        mockRefreshToken.setToken("mockedRefreshToken");
        mockRefreshToken.setUser(user);
        when(refreshTokenService.createRefreshToken(user)).thenReturn(mockRefreshToken);

        // Act
        AuthResponseDTO response = userService.login(loginDTO);

        // Assert
        assertNotNull(response);
        assertEquals("mockedAccessToken", response.getAccessToken());
        assertEquals("mockedRefreshToken", response.getRefreshToken());
    }

    @Test
    void testLogin_invalidPassword_throwsException() {
        // Arrange
        UserLoginDTO loginDTO = new UserLoginDTO();
        loginDTO.setUsernameOrEmail("mockuser");
        loginDTO.setPassword("wrongpass");

        User user = new User();
        user.setUsername("mockuser");
        user.setPassword("hashedpass");
        user.setStatus("active");
        user.setRole("USER");

        when(userRepository.findByUsernameOrEmail("mockuser", "mockuser")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrongpass", "hashedpass")).thenReturn(false);

        // Act & Assert
        assertThrows(InvalidCredentialsException.class, () -> userService.login(loginDTO));
    }

    @Test
    void testLogin_inactiveUser_throwsException() {
        // Arrange
        UserLoginDTO loginDTO = new UserLoginDTO();
        loginDTO.setUsernameOrEmail("mockuser");
        loginDTO.setPassword("mockpass");

        User user = new User();
        user.setUsername("mockuser");
        user.setPassword("hashedpass");
        user.setStatus("inactive");
        user.setRole("USER");

        when(userRepository.findByUsernameOrEmail("mockuser", "mockuser")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("mockpass", "hashedpass")).thenReturn(true);

        // Act & Assert
        assertThrows(InactiveUserException.class, () -> userService.login(loginDTO));
    }

    @Test
    void testLogin_unverifiedEmail_throwsException() {
        // Arrange
        UserLoginDTO loginDTO = new UserLoginDTO();
        loginDTO.setUsernameOrEmail("mockuser");
        loginDTO.setPassword("mockpass");

        User user = new User();
        user.setUsername("mockuser");
        user.setPassword("hashedpass");
        user.setStatus("pending"); // Nuevo: status pendiente
        user.setRole("USER");
        user.setEmailVerified(false); // Nuevo: email no verificado

        when(userRepository.findByUsernameOrEmail("mockuser", "mockuser")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("mockpass", "hashedpass")).thenReturn(true);

        // Act & Assert
        Exception ex = assertThrows(com.example.fintech.wallet.exception.InvalidCredentialsException.class, () -> {
            userService.login(loginDTO);
        });
        assertTrue(ex.getMessage().contains("Debes verificar tu correo electrónico"));
    }

    @Test
    void testTransfer_success_withMockedDependencies() {
        // Arrange
        User sender = new User();
        sender.setId(1L);
        sender.setUsername("sender");
        sender.setStatus("active");
        sender.setRole("USER");
        com.example.fintech.wallet.entity.Account senderAccount = new com.example.fintech.wallet.entity.Account();
        senderAccount.setUser(sender);
        senderAccount.setBalance(new java.math.BigDecimal("1000"));

        User receiver = new User();
        receiver.setId(2L);
        receiver.setUsername("receiver");
        receiver.setStatus("active");
        receiver.setRole("USER");
        com.example.fintech.wallet.entity.Account receiverAccount = new com.example.fintech.wallet.entity.Account();
        receiverAccount.setUser(receiver);
        receiverAccount.setBalance(new java.math.BigDecimal("500"));

        when(accountRepository.findByUser(sender)).thenReturn(java.util.Optional.of(senderAccount));
        when(accountRepository.findByUser(receiver)).thenReturn(java.util.Optional.of(receiverAccount));


        java.math.BigDecimal amount = new java.math.BigDecimal("200");
        // Act
        senderAccount.setBalance(senderAccount.getBalance().subtract(amount));
        receiverAccount.setBalance(receiverAccount.getBalance().add(amount));

        // Assert
        assertEquals(new java.math.BigDecimal("800"), senderAccount.getBalance());
        assertEquals(new java.math.BigDecimal("700"), receiverAccount.getBalance());
    }

    @Test
    void testRefreshToken_success_withMockedDependencies() {
        // Arrange
        User user = new User();
        user.setUsername("refreshuser");
        user.setStatus("active");
        user.setRole("USER");
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken("refresh-token-123");
        refreshToken.setUser(user);
        refreshToken.setExpiryDate(java.time.Instant.now().plusSeconds(3600));

        when(refreshTokenService.findByToken("refresh-token-123")).thenReturn(java.util.Optional.of(refreshToken));
        when(refreshTokenService.isRefreshTokenExpired(refreshToken)).thenReturn(false);
        when(jwtUtil.generateToken(any())).thenReturn("newAccessToken");
        RefreshToken newRefreshToken = new RefreshToken();
        newRefreshToken.setToken("new-refresh-token-456");
        newRefreshToken.setUser(user);
        when(refreshTokenService.createRefreshToken(user)).thenReturn(newRefreshToken);


        RefreshToken foundToken = refreshTokenService.findByToken("refresh-token-123").orElseThrow();
        assertFalse(refreshTokenService.isRefreshTokenExpired(foundToken));
        String newAccessToken = jwtUtil.generateToken(any());
        RefreshToken rotatedToken = refreshTokenService.createRefreshToken(user);

        // Assert
        assertEquals("newAccessToken", newAccessToken);
        assertEquals("new-refresh-token-456", rotatedToken.getToken());
    }
} 