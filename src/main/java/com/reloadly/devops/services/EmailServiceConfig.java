package com.reloadly.devops.services;

import org.springframework.mail.MailException;

public interface EmailServiceConfig {
	void sendMail(String message, String recipientEmail, String subject) throws MailException;
}