package com.example.fintech.wallet.repository;

import com.example.fintech.wallet.entity.Account;
import com.example.fintech.wallet.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;


public interface AccountRepository extends JpaRepository<Account, Long> {

   
    Optional<Account> findByUser(User user);
} 