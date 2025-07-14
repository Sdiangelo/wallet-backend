package com.example.fintech.wallet.repository;

import com.example.fintech.wallet.entity.RefreshToken;
import com.example.fintech.wallet.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);
    Optional<RefreshToken> findByUser(User user);
    void deleteByUser(User user);
} 