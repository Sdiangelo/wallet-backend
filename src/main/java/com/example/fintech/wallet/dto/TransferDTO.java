package com.example.fintech.wallet.dto;

import java.math.BigDecimal;

public class TransferDTO {

    private Long destinationAccountId;


    private BigDecimal amount;


    public Long getDestinationAccountId() {
        return destinationAccountId;
    }

    public void setDestinationAccountId(Long destinationAccountId) {
        this.destinationAccountId = destinationAccountId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
} 