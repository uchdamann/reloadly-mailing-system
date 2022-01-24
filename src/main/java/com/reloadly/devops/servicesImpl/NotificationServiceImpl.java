package com.reloadly.devops.servicesImpl;

import org.springframework.stereotype.Service;

import com.reloadly.devops.exceptions.AppException;
import com.reloadly.devops.exceptions.UsernameMismatchException;
import com.reloadly.devops.request.dtos.AccountCreationNotificationDTO;
import com.reloadly.devops.request.dtos.LoginNotificationDTO;
import com.reloadly.devops.request.dtos.NotificationDTO;
import com.reloadly.devops.request.dtos.TransactionNotificationDTO;
import com.reloadly.devops.services.NotificationService;
import com.reloadly.devops.utilities.ExternalCalls;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
	private final EmailTemplateConfig templateConfigurer;
	private final ExternalCalls extCalls;

	@Override
	public <T extends NotificationDTO> void notifyUser(T notificationRequest) {
		Map<String, Object> validUser = extCalls.validateUserExternally(notificationRequest.getUsername())
			.orElseThrow(() -> new AppException("Error validating username: " + notificationRequest.getUsername()));
		
		if(!(Boolean) validUser.get("valid")) {
			throw new UsernameMismatchException();
		}
		
		log.info("--->> User validated successfully");
		notificationRequest.setFirstname((String) validUser.get("firstname"));

		switch (notificationRequest.getNotificationType()) {
		case LOGIN:
			log.info("--->> Sending notification email for login");
			
			LoginNotificationDTO loginNotificationDTO = (LoginNotificationDTO) notificationRequest;
			templateConfigurer.loginNotification(loginNotificationDTO);
			break;

		case SIGNUP:
			log.info("--->> Sending notification email for successful user registration");

			AccountCreationNotificationDTO accountCreationNotificationDTO = (AccountCreationNotificationDTO) notificationRequest;
			templateConfigurer.signupNotification(accountCreationNotificationDTO);
			break;

		default:
			log.info("--->> Sending notification email for transaction");

			TransactionNotificationDTO transactionNotificationDTO = (TransactionNotificationDTO) notificationRequest;
			templateConfigurer.transactionNotification(transactionNotificationDTO);
			break;
		}
	}
	
}





















