package com.reloadly.devops.utilities;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import static com.reloadly.devops.constants.TransactionType.*;
import com.reloadly.devops.request.dtos.AccountCreationNotificationDTO;
import com.reloadly.devops.request.dtos.LoginNotificationDTO;
import com.reloadly.devops.request.dtos.TransactionNotificationDTO;
import com.reloadly.devops.response.dtos.AccountCreationBackingObject;
import com.reloadly.devops.response.dtos.LoginBackingObject;
import com.reloadly.devops.response.dtos.TransactionBackingObject;
import org.springframework.stereotype.Component;

import com.reloadly.devops.exceptions.AppException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class UtilitiesAndTweaks {
	private final AppProperties props;
	
	public String formatDate(Date date) {
		return new SimpleDateFormat(props.getFormattedDate()).format(date);
	}

	public String formatTime(Date date) {
		return new SimpleDateFormat(props.getFormattedTime()).format(date);
	}
	
	public void channelCodeHandler(HttpServletRequest req) {
		log.info("---->>> Initializing request source check");
		log.info("---->>> Authenticated user is: {}",
				req.getUserPrincipal() == null ? "No user" : req.getUserPrincipal().getName());

		final String channelCode = req.getHeader("ChannelCode");
		final String webRequest = props.getWebChannelCode();
		final String mobileConReq = props.getMobileConsumerChannelCode();

		if (channelCode != null) {
			if (channelCode.equals(webRequest)) {
				log.info("---->>> Request source: WEB BROWSER");
			} else if (channelCode.equals(mobileConReq)) {
				log.info("---->>> Request source:  MOBILE APP - CONSUMERS");
			}  else {
				log.info("---->>> Error:  Invalid access code");
				throw new AppException("Access code is invalid"); // Access code is my ChannelCode
			}
		} else {
			log.info("---->>> Error:  Access code cannot be empty, blank or null");
			throw new AppException("Access code cannot be empty");
		}
	}

	public LoginBackingObject buildLoginBackingObject(LoginNotificationDTO loginNotificationDTO){
		LoginBackingObject loginBackingObject = new LoginBackingObject();
		loginBackingObject.setDate(formatDate(loginNotificationDTO.getCreatedOn()));
		loginBackingObject.setLocation(loginNotificationDTO.getLocation());
		loginBackingObject.setFirstname(loginNotificationDTO.getFirstname());
		loginBackingObject.setTime(formatTime(loginNotificationDTO.getCreatedOn()));

		return loginBackingObject;
	}

	public AccountCreationBackingObject buildAccountCreationBackingObject(
			AccountCreationNotificationDTO accountCreationNotificationDTO){

		AccountCreationBackingObject accountCreationBackingObject = new AccountCreationBackingObject();
		accountCreationBackingObject.setAccountType(accountCreationNotificationDTO.getAccountType().name());
		accountCreationBackingObject.setAccountNumber(accountCreationNotificationDTO.getAccountNumber());
		accountCreationBackingObject.setFirstname(accountCreationNotificationDTO.getFirstname());
		accountCreationBackingObject.setUsername(accountCreationNotificationDTO.getUsername());
		accountCreationBackingObject.setBalance(accountCreationNotificationDTO.getInitialBalance());
		accountCreationBackingObject.setLastname(accountCreationNotificationDTO.getLastname());

		return accountCreationBackingObject;
	}

	public TransactionBackingObject buildTransactionBackingObject(TransactionNotificationDTO transactionNotificationDTO){
		TransactionBackingObject transactionBackingObject = new TransactionBackingObject();

		switch (transactionNotificationDTO.getTransactionType()){
			case CREDIT:{
				transactionBackingObject.setLedgerBalance(transactionNotificationDTO.getInitialBalance());
				transactionBackingObject.setTransactionType(CREDIT.name());
				transactionBackingObject.setUsername(transactionNotificationDTO.getUsername());
				transactionBackingObject.setFirstname(transactionNotificationDTO.getFirstname());
				transactionBackingObject.setCurrentBalance(
						transactionNotificationDTO.getTransactionAmount().add(
								transactionNotificationDTO.getInitialBalance()
						)
				);
				transactionBackingObject.setTransactionAmount(transactionNotificationDTO.getTransactionAmount());
				transactionBackingObject.setInitialBalance(transactionNotificationDTO.getInitialBalance());
				break;
			}

			case DEBIT:{
				transactionBackingObject.setLedgerBalance(transactionNotificationDTO.getInitialBalance());
				transactionBackingObject.setTransactionType(DEBIT.name());
				transactionBackingObject.setUsername(transactionNotificationDTO.getUsername());
				transactionBackingObject.setFirstname(transactionNotificationDTO.getFirstname());
				transactionBackingObject.setCurrentBalance(
						transactionNotificationDTO.getInitialBalance().subtract(
								transactionNotificationDTO.getTransactionAmount()
						)
				);
				transactionBackingObject.setTransactionAmount(transactionNotificationDTO.getTransactionAmount());
				transactionBackingObject.setInitialBalance(transactionNotificationDTO.getInitialBalance());
				break;
			}

			default:{
				transactionBackingObject.setLedgerBalance(transactionNotificationDTO.getInitialBalance());
				transactionBackingObject.setTransactionType(BALANCE.name());
				transactionBackingObject.setUsername(transactionNotificationDTO.getUsername());
				transactionBackingObject.setFirstname(transactionNotificationDTO.getFirstname());
				transactionBackingObject.setCurrentBalance(
						transactionNotificationDTO.getInitialBalance()
				);
				transactionBackingObject.setTransactionAmount(BigDecimal.ZERO);
				transactionBackingObject.setInitialBalance(transactionNotificationDTO.getInitialBalance());
				break;
			}
		}

		return transactionBackingObject;
	}
}