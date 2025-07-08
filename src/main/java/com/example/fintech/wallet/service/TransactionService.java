package com.example.fintech.wallet.service;

import com.example.fintech.wallet.dto.TransferDTO;
import com.example.fintech.wallet.dto.TransactionResponseDTO;
import java.util.List;


public interface TransactionService {

   
    TransactionResponseDTO transfer(String username, TransferDTO transferDTO);

   
    List<TransactionResponseDTO> getHistory(String username);
} 