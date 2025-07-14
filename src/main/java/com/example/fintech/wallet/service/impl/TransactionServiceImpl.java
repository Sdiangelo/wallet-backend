package com.example.fintech.wallet.service.impl;

import com.example.fintech.wallet.dto.TransferDTO;
import com.example.fintech.wallet.dto.TransactionResponseDTO;
import com.example.fintech.wallet.entity.Account;
import com.example.fintech.wallet.entity.Transaction;
import com.example.fintech.wallet.entity.User;
import com.example.fintech.wallet.repository.AccountRepository;
import com.example.fintech.wallet.repository.TransactionRepository;
import com.example.fintech.wallet.repository.UserRepository;
import com.example.fintech.wallet.service.TransactionService;
import com.example.fintech.wallet.exception.UserNotFoundException;
import com.example.fintech.wallet.exception.AccountNotFoundException;
import com.example.fintech.wallet.exception.InsufficientFundsException;
import com.example.fintech.wallet.exception.TransactionNotAllowedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Service
public class TransactionServiceImpl implements TransactionService {

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    @Autowired
    public TransactionServiceImpl(UserRepository userRepository, AccountRepository accountRepository, TransactionRepository transactionRepository) {
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }


    @Override
    @Transactional
    public TransactionResponseDTO transfer(String username, TransferDTO transferDTO) {
        User sourceUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        Account sourceAccount = accountRepository.findByUser(sourceUser)
                .orElseThrow(() -> new AccountNotFoundException("Source account not found"));
        Account destinationAccount = accountRepository.findById(transferDTO.getDestinationAccountId())
                .orElseThrow(() -> new AccountNotFoundException("Destination account not found"));
        if (sourceAccount.getId().equals(destinationAccount.getId())) {
            throw new TransactionNotAllowedException("You cannot transfer to your own account");
        }
        if (sourceAccount.getBalance().compareTo(transferDTO.getAmount()) < 0) {
            throw new InsufficientFundsException("Insufficient funds");
        }
        sourceAccount.setBalance(sourceAccount.getBalance().subtract(transferDTO.getAmount()));
        destinationAccount.setBalance(destinationAccount.getBalance().add(transferDTO.getAmount()));
        accountRepository.save(sourceAccount);
        accountRepository.save(destinationAccount);
        Transaction transaction = new Transaction();
        transaction.setSourceAccount(sourceAccount);
        transaction.setDestinationAccount(destinationAccount);
        transaction.setAmount(transferDTO.getAmount());
        transaction.setDate(LocalDateTime.now());
        transaction.setStatus("COMPLETED");
        transaction.setRejectionReason(null);
        transactionRepository.save(transaction);
        return mapTransactionToDTO(transaction);
    }


    @Override
    public List<TransactionResponseDTO> getHistory(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        Account account = accountRepository.findByUser(user)
                .orElseThrow(() -> new AccountNotFoundException("Account not found"));
        List<Transaction> transactions = transactionRepository.findBySourceAccountOrDestinationAccount(account, account);
        List<TransactionResponseDTO> history = new ArrayList<>();
        for (Transaction t : transactions) {
            history.add(mapTransactionToDTO(t));
        }
        return history;
    }


    private TransactionResponseDTO createFailedTransaction(Account sourceAccount, Account destinationAccount, BigDecimal amount, String reason) {
        Transaction transaction = new Transaction();
        transaction.setSourceAccount(sourceAccount);
        transaction.setDestinationAccount(destinationAccount);
        transaction.setAmount(amount);
        transaction.setDate(LocalDateTime.now());
        transaction.setStatus("REJECTED");
        transaction.setRejectionReason(reason);
        transactionRepository.save(transaction);
        return mapTransactionToDTO(transaction);
    }


    private TransactionResponseDTO mapTransactionToDTO(Transaction transaction) {
        TransactionResponseDTO dto = new TransactionResponseDTO();
        dto.setId(transaction.getId());
        dto.setSourceAccountId(transaction.getSourceAccount().getId());
        dto.setDestinationAccountId(transaction.getDestinationAccount().getId());
        dto.setAmount(transaction.getAmount());
        dto.setDate(transaction.getDate());
        dto.setStatus(transaction.getStatus());
        dto.setRejectionReason(transaction.getRejectionReason());
        return dto;
    }
} 