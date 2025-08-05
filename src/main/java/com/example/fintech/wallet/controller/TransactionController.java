package com.example.fintech.wallet.controller;

import com.example.fintech.wallet.dto.TransferDTO;
import com.example.fintech.wallet.dto.TransactionResponseDTO;
import com.example.fintech.wallet.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;



    
    @PostMapping("/transfer")
    public TransactionResponseDTO transfer(@AuthenticationPrincipal UserDetails userDetails,
                                              @Valid @RequestBody TransferDTO transferDTO) {
        return transactionService.transfer(userDetails.getUsername(), transferDTO);
    }

    @GetMapping("/history")
    public List<TransactionResponseDTO> history(@AuthenticationPrincipal UserDetails userDetails) {
        return transactionService.getHistory(userDetails.getUsername());
    }
} 