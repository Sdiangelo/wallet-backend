package com.example.fintech.wallet.controller;

import com.example.fintech.wallet.dto.UserResponseDTO;
import com.example.fintech.wallet.dto.TransactionResponseDTO;
import com.example.fintech.wallet.entity.User;
import com.example.fintech.wallet.entity.Transaction;
import com.example.fintech.wallet.repository.UserRepository;
import com.example.fintech.wallet.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransactionRepository transactionRepository;


    @GetMapping("/users")
    public List<UserResponseDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream().map(u -> {
            UserResponseDTO dto = new UserResponseDTO();
            dto.setId(u.getId());
            dto.setFullName(u.getFullName());
            dto.setEmail(u.getEmail());
            dto.setUsername(u.getUsername());
            dto.setRole(u.getRole());
            dto.setStatus(u.getStatus());
            return dto;
        }).collect(Collectors.toList());
    }


    @GetMapping("/transactions")
    public List<TransactionResponseDTO> getAllTransactions() {
        List<Transaction> transactions = transactionRepository.findAll();
        return transactions.stream().map(t -> {
            TransactionResponseDTO dto = new TransactionResponseDTO();
            dto.setId(t.getId());
            dto.setSourceAccountId(t.getSourceAccount().getId());
            dto.setDestinationAccountId(t.getDestinationAccount().getId());
            dto.setAmount(t.getAmount());
            dto.setDate(t.getDate());
            dto.setStatus(t.getStatus());
            dto.setRejectionReason(t.getRejectionReason());
            return dto;
        }).collect(Collectors.toList());
    }
} 