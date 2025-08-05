package com.example.fintech.wallet.dto;

import java.math.BigDecimal;

public class TransferDTO {

    private String toEmail;
    private BigDecimal amount;

    public String getToEmail() {
        return toEmail;
    }

    public void setToEmail(String toEmail) {
        this.toEmail = toEmail;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
} 