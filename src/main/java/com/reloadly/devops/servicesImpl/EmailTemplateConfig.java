package com.reloadly.devops.servicesImpl;

import com.reloadly.devops.response.dtos.TransactionBackingObject;
import com.reloadly.devops.utilities.UtilitiesAndTweaks;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.reloadly.devops.request.dtos.AccountCreationNotificationDTO;
import com.reloadly.devops.request.dtos.LoginNotificationDTO;
import com.reloadly.devops.request.dtos.TransactionNotificationDTO;
import com.reloadly.devops.services.EmailServiceConfig;
import com.reloadly.devops.utilities.AppProperties;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailTemplateConfig {
	private final AppProperties props;
	private Context context = new Context();
	private final TemplateEngine templateEngine;
	private final EmailServiceConfig emailServiceConfig;
	private final UtilitiesAndTweaks utils;

	@Async
	public void loginNotification(LoginNotificationDTO loginNotificationDTO) {
		log.info("--->> Login notifier configurer");

		try {
			context.setVariable("loginNotifier", utils.buildLoginBackingObject(loginNotificationDTO));

			String template = "login_template";
			final String mailSubject = "Login Notification";

			String message = templateEngine.process(template, context);

			log.info("---->>> Mail context, template and subject setup completed");
			emailServiceConfig.sendMail(message, loginNotificationDTO.getUsername(), mailSubject);
		} catch (Exception ex) {
			log.info("---->>> Error: {}", ex.getMessage());
		}
	}
	
	@Async
	public void signupNotification(AccountCreationNotificationDTO accountCreationNotificationDTO) {

		try {
			context.setVariable("signupNotifier", utils.buildAccountCreationBackingObject(accountCreationNotificationDTO));

			String template = "signup_template";
			final String mailSubject = "Welcome To " + props.getBankName();

			String message = templateEngine.process(template, context);

			log.info("---->>> Mail context, template and subject setup completed");
			emailServiceConfig.sendMail(message, accountCreationNotificationDTO.getUsername(), mailSubject);
		} catch (Exception ex) {
			log.info("---->>> Error: {}", ex.getMessage());
		}
	}

	public void transactionNotification(TransactionNotificationDTO transactionNotificationDTO) {
		TransactionBackingObject transactionBackingObject = utils.buildTransactionBackingObject(transactionNotificationDTO);

		switch (transactionNotificationDTO.getTransactionType()) {
		case DEBIT:
			sendDebitNotification(transactionBackingObject);
			break;

		case CREDIT:
			sendCreditNotification(transactionBackingObject);
			break;

		default:
			checkBalanceNotification(transactionBackingObject);
			break;
		}
	}

	@Async
	private void sendDebitNotification(TransactionBackingObject transactionBackingObject) {
		try {
			context.setVariable("transactionNotifier", transactionBackingObject);

			String template = "transaction_template";
			final String mailSubject = "Debit Notification";

			String message = templateEngine.process(template, context);

			log.info("---->>> Mail context, template and subject setup completed");
			emailServiceConfig.sendMail(message, transactionBackingObject.getUsername(), mailSubject);
		} catch (Exception ex) {
			log.info("---->>> Error: {}", ex.getMessage());
		}
	}

	@Async
	private void sendCreditNotification(TransactionBackingObject transactionBackingObject) {
		try {
			context.setVariable("transactionNotifier", transactionBackingObject);

			String template = "transaction_template";
			final String mailSubject = "Credit Notification";

			String message = templateEngine.process(template, context);

			log.info("---->>> Mail context, template and subject setup completed");
			emailServiceConfig.sendMail(message, transactionBackingObject.getUsername(), mailSubject);
		} catch (Exception ex) {
			log.info("---->>> Error: {}", ex.getMessage());
		}
	}

	@Async
	private void checkBalanceNotification(TransactionBackingObject transactionBackingObject) {
		try {
			context.setVariable("transactionNotifier", transactionBackingObject);

			String template = "transaction_template";
			final String mailSubject = "Balance Notification";

			String message = templateEngine.process(template, context);

			log.info("---->>> Mail context, template and subject setup completed");
			emailServiceConfig.sendMail(message, transactionBackingObject.getUsername(), mailSubject);
		} catch (Exception ex) {
			log.info("---->>> Error: {}", ex.getMessage());
		}
	}
}