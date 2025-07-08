package com.example.fintech.wallet.controller;

import com.example.fintech.wallet.dto.AccountResponseDTO;
import com.example.fintech.wallet.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;


@RestController
@RequestMapping("/api/accounts/me")
public class AccountController {

    @Autowired
    private AccountService accountService;


    @GetMapping("/balance")
    public BigDecimal getBalance(@AuthenticationPrincipal UserDetails userDetails) {
        return accountService.getBalanceByUsername(userDetails.getUsername());
    }

    @GetMapping
    public AccountResponseDTO getAccount(@AuthenticationPrincipal UserDetails userDetails) {
        return accountService.getAccountByUsername(userDetails.getUsername());
    }
} 