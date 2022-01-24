package com.reloadly.devops.response.dtos;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AccountCreationBackingObject {
    private String firstname;
    private String lastname;
    private String username;
    private String accountNumber;
    private String accountType;
    private BigDecimal balance;
}