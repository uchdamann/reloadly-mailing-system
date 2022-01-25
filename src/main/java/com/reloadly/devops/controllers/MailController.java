package com.reloadly.devops.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.reloadly.devops.request.dtos.AccountCreationNotificationDTO;
import com.reloadly.devops.request.dtos.LoginNotificationDTO;
import com.reloadly.devops.request.dtos.TransactionNotificationDTO;
import com.reloadly.devops.utilities.UtilitiesAndTweaks;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.reloadly.devops.services.NotificationService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("api/mail/v1")
@CrossOrigin(origins = "*", maxAge = 3600)
//@SecurityRequirement(name = "Authorization")
@SecurityRequirement(name = "ChannelCode")
public class MailController {
	private final NotificationService notificationService;
	private final UtilitiesAndTweaks utils;
	
	@PostMapping("/send-login")
	public void loginNotification(@RequestBody @Valid LoginNotificationDTO notificationDTO,
												   HttpServletRequest req) {
		log.info("--->> Initializing mail notification");
		utils.channelCodeHandler(req);
		notificationService.notifyUser(notificationDTO);
	}

	@PostMapping("/send-create")
	public void creationNotification(@RequestBody @Valid AccountCreationNotificationDTO notificationDTO,
								  HttpServletRequest req) {
		log.info("--->> Initializing mail notification");
		utils.channelCodeHandler(req);
		notificationService.notifyUser(notificationDTO);
	}

	@PostMapping("/send-transact")
	public void transactionNotification(@RequestBody @Valid TransactionNotificationDTO notificationDTO,
								  HttpServletRequest req) {
		log.info("--->> Initializing mail notification");
		utils.channelCodeHandler(req);
		notificationService.notifyUser(notificationDTO);
	}
}