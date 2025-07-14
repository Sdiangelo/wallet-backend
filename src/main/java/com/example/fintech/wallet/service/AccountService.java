package com.example.fintech.wallet.service;

import com.example.fintech.wallet.dto.AccountResponseDTO;
import java.math.BigDecimal;


public interface AccountService {

   
    BigDecimal getBalanceByUsername(String username);

    AccountResponseDTO getAccountByUsername(String username);
} 