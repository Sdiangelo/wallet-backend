package com.example.fintech.wallet.repository;

import com.example.fintech.wallet.entity.Transaction;
import com.example.fintech.wallet.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    
    List<Transaction> findBySourceAccount(Account sourceAccount);

   
    List<Transaction> findByDestinationAccount(Account destinationAccount);


    List<Transaction> findBySourceAccountOrDestinationAccount(Account sourceAccount, Account destinationAccount);
} 