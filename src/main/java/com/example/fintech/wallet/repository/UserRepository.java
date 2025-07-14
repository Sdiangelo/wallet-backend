package com.example.fintech.wallet.repository;

import com.example.fintech.wallet.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long> {

    
    Optional<User> findByEmail(String email);

   
    Optional<User> findByUsername(String username);

   
    Optional<User> findByUsernameOrEmail(String username, String email);

   
    boolean existsByEmail(String email);


    boolean existsByUsername(String username);
} 