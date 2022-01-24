package com.reloadly.devops.request.dtos;

import java.math.BigDecimal;

import com.reloadly.devops.constants.TransactionType;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class TransactionNotificationDTO extends NotificationDTO {
	private TransactionType transactionType;
	private BigDecimal transactionAmount;
	private BigDecimal initialBalance;
}
