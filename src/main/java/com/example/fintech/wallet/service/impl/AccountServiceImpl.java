package com.example.fintech.wallet.service.impl;

import com.example.fintech.wallet.dto.AccountResponseDTO;
import com.example.fintech.wallet.entity.Account;
import com.example.fintech.wallet.entity.User;
import com.example.fintech.wallet.repository.AccountRepository;
import com.example.fintech.wallet.repository.UserRepository;
import com.example.fintech.wallet.service.AccountService;
import com.example.fintech.wallet.exception.UserNotFoundException;
import com.example.fintech.wallet.exception.AccountNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    @Autowired
    public AccountServiceImpl(AccountRepository accountRepository, UserRepository userRepository) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
    }


    @Override
    public java.math.BigDecimal getBalanceByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        Account account = accountRepository.findByUser(user)
                .orElseThrow(() -> new AccountNotFoundException("Account not found"));
        return account.getBalance();
    }


    @Override
    public AccountResponseDTO getAccountByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        Account account = accountRepository.findByUser(user)
                .orElseThrow(() -> new AccountNotFoundException("Account not found"));
        AccountResponseDTO dto = new AccountResponseDTO();
        dto.setId(account.getId());
        dto.setBalance(account.getBalance());
        dto.setUserId(user.getId());
        return dto;
    }
} 