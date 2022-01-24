package com.reloadly.devops.response.dtos;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransactionBackingObject {
    private String transactionType;
    private String firstname;
    private BigDecimal initialBalance;
    private BigDecimal currentBalance;
    private BigDecimal ledgerBalance;
    private String username;
    private BigDecimal transactionAmount;
}