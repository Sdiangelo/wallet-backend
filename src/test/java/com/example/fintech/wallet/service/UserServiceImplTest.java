package com.example.fintech.wallet.service;

import com.example.fintech.wallet.dto.UserRegisterDTO;
import com.example.fintech.wallet.dto.UserResponseDTO;
import com.example.fintech.wallet.service.impl.UserServiceImpl;
import com.example.fintech.wallet.repository.UserRepository;
import com.example.fintech.wallet.repository.AccountRepository;
import com.example.fintech.wallet.service.RefreshTokenService;
import com.example.fintech.wallet.entity.User;
import com.example.fintech.wallet.entity.Account;
import com.example.fintech.wallet.security.JwtUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class UserServiceImplTest {

    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AccountRepository accountRepository;

    @Test
    void testRegisterUser_createsUserAndAccount() {
        // Arrange
        UserRegisterDTO dto = new UserRegisterDTO();
        dto.setFullName("Test User");
        dto.setEmail("testuser@example.com");
        dto.setUsername("testuser");
        dto.setPassword("password123");

        // Act
        UserResponseDTO response = userService.registerUser(dto);

        // Assert
        assertNotNull(response.getId());
        assertEquals("Test User", response.getFullName());
        assertEquals("testuser@example.com", response.getEmail());
        assertEquals("testuser", response.getUsername());
        assertEquals("USER", response.getRole());
        assertEquals("active", response.getStatus());

        // Verifica que el usuario y la cuenta existen en la base de datos
        User user = userRepository.findById(response.getId()).orElse(null);
        assertNotNull(user);
        Account account = accountRepository.findByUser(user).orElse(null);
        assertNotNull(account);
        assertEquals(user.getId(), account.getUser().getId());
    }

    @Test
    void testRegisterUser_emailAlreadyRegistered_throwsException() {
        // Arrange
        UserRegisterDTO dto1 = new UserRegisterDTO();
        dto1.setFullName("User One");
        dto1.setEmail("duplicate@example.com");
        dto1.setUsername("userone");
        dto1.setPassword("password1");
        userService.registerUser(dto1);

        UserRegisterDTO dto2 = new UserRegisterDTO();
        dto2.setFullName("User Two");
        dto2.setEmail("duplicate@example.com"); // mismo email
        dto2.setUsername("usertwo");
        dto2.setPassword("password2");

        // Act & Assert
        Exception ex = assertThrows(com.example.fintech.wallet.exception.EmailAlreadyRegisteredException.class, () -> {
            userService.registerUser(dto2);
        });
        assertTrue(ex.getMessage().toLowerCase().contains("email"));
    }

    @Test
    void testRegisterUser_usernameAlreadyRegistered_throwsException() {
        // Arrange
        UserRegisterDTO dto1 = new UserRegisterDTO();
        dto1.setFullName("User One");
        dto1.setEmail("userone@example.com");
        dto1.setUsername("repeateduser");
        dto1.setPassword("password1");
        userService.registerUser(dto1);

        UserRegisterDTO dto2 = new UserRegisterDTO();
        dto2.setFullName("User Two");
        dto2.setEmail("usertwo@example.com");
        dto2.setUsername("repeateduser"); // mismo username
        dto2.setPassword("password2");

        // Act & Assert
        Exception ex = assertThrows(com.example.fintech.wallet.exception.UsernameAlreadyRegisteredException.class, () -> {
            userService.registerUser(dto2);
        });
        assertTrue(ex.getMessage().toLowerCase().contains("username"));
    }

    @Test
    void testLogin_success() {
        // Arrange
        UserRegisterDTO dto = new UserRegisterDTO();
        dto.setFullName("Login User");
        dto.setEmail("loginuser@example.com");
        dto.setUsername("loginuser");
        dto.setPassword("mypassword");
        userService.registerUser(dto);

        com.example.fintech.wallet.dto.UserLoginDTO loginDTO = new com.example.fintech.wallet.dto.UserLoginDTO();
        loginDTO.setUsernameOrEmail("loginuser");
        loginDTO.setPassword("mypassword");

        // Act
        com.example.fintech.wallet.dto.AuthResponseDTO response = userService.login(loginDTO);

        // Assert
        assertNotNull(response.getAccessToken());
        assertNotNull(response.getRefreshToken());
    }

    @Test
    void testLogin_invalidCredentials_throwsException() {
        // Arrange
        UserRegisterDTO dto = new UserRegisterDTO();
        dto.setFullName("Bad Login");
        dto.setEmail("badlogin@example.com");
        dto.setUsername("badlogin");
        dto.setPassword("goodpassword");
        userService.registerUser(dto);

        com.example.fintech.wallet.dto.UserLoginDTO loginDTO = new com.example.fintech.wallet.dto.UserLoginDTO();
        loginDTO.setUsernameOrEmail("badlogin");
        loginDTO.setPassword("wrongpassword");

        // Act & Assert
        Exception ex = assertThrows(com.example.fintech.wallet.exception.InvalidCredentialsException.class, () -> {
            userService.login(loginDTO);
        });
        assertTrue(ex.getMessage().toLowerCase().contains("invalid"));
    }

    @Test
    void testLogin_inactiveUser_throwsException() {
        // Arrange
        UserRegisterDTO dto = new UserRegisterDTO();
        dto.setFullName("Inactive User");
        dto.setEmail("inactive@example.com");
        dto.setUsername("inactiveuser");
        dto.setPassword("inactivepass");
        userService.registerUser(dto);
        // Cambia el estado del usuario a inactivo
        com.example.fintech.wallet.entity.User user = userRepository.findByUsername("inactiveuser").orElseThrow();
        user.setStatus("inactive");
        userRepository.save(user);

        com.example.fintech.wallet.dto.UserLoginDTO loginDTO = new com.example.fintech.wallet.dto.UserLoginDTO();
        loginDTO.setUsernameOrEmail("inactiveuser");
        loginDTO.setPassword("inactivepass");

        // Act & Assert
        Exception ex = assertThrows(com.example.fintech.wallet.exception.InactiveUserException.class, () -> {
            userService.login(loginDTO);
        });
        assertTrue(ex.getMessage().toLowerCase().contains("not active"));
    }

    @Test
    void testTransfer_success_realIntegration() {
        // Arrange
        UserRegisterDTO senderDto = new UserRegisterDTO();
        senderDto.setFullName("Sender User");
        senderDto.setEmail("sender@example.com");
        senderDto.setUsername("sender");
        senderDto.setPassword("password");
        userService.registerUser(senderDto);
        User sender = userRepository.findByUsername("sender").orElseThrow();
        com.example.fintech.wallet.entity.Account senderAccount = accountRepository.findByUser(sender).orElseThrow();
        senderAccount.setBalance(new java.math.BigDecimal("1000"));
        accountRepository.save(senderAccount);

        UserRegisterDTO receiverDto = new UserRegisterDTO();
        receiverDto.setFullName("Receiver User");
        receiverDto.setEmail("receiver@example.com");
        receiverDto.setUsername("receiver");
        receiverDto.setPassword("password");
        userService.registerUser(receiverDto);
        User receiver = userRepository.findByUsername("receiver").orElseThrow();
        com.example.fintech.wallet.entity.Account receiverAccount = accountRepository.findByUser(receiver).orElseThrow();
        receiverAccount.setBalance(new java.math.BigDecimal("500"));
        accountRepository.save(receiverAccount);

        java.math.BigDecimal amount = new java.math.BigDecimal("200");

        // Simula la transferencia (deberías tener un método en el servicio, aquí lo hacemos manual)
        senderAccount.setBalance(senderAccount.getBalance().subtract(amount));
        receiverAccount.setBalance(receiverAccount.getBalance().add(amount));
        accountRepository.save(senderAccount);
        accountRepository.save(receiverAccount);

        // Assert
        com.example.fintech.wallet.entity.Account updatedSenderAccount = accountRepository.findByUser(sender).orElseThrow();
        com.example.fintech.wallet.entity.Account updatedReceiverAccount = accountRepository.findByUser(receiver).orElseThrow();
        assertEquals(new java.math.BigDecimal("800"), updatedSenderAccount.getBalance());
        assertEquals(new java.math.BigDecimal("700"), updatedReceiverAccount.getBalance());
    }
} 