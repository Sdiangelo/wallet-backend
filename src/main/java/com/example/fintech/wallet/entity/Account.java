package com.example.fintech.wallet.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;


@Entity
@Table(name = "accounts")
public class Account {

  
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

   
    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    
    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal balance;


    public Account() {
    }

    public Account(User user, BigDecimal balance) {
        this.user = user;
        this.balance = balance;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
} 