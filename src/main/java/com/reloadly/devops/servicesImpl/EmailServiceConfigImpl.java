package com.reloadly.devops.servicesImpl;

import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

import com.reloadly.devops.exceptions.AppException;
import com.reloadly.devops.services.EmailServiceConfig;
import com.reloadly.devops.utilities.AppProperties;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailServiceConfigImpl implements EmailServiceConfig {
	private final JavaMailSender mailSender;
	private final AppProperties props;

	@Override
	public void sendMail(String message, String recipientEmail, String subject) throws MailException {
		log.info("---->>> Preparing mail content");
		
		try {
			MimeMessagePreparator messagePreparator = mimeMessage -> {
				MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
				messageHelper.setFrom(props.getMailSender());
				messageHelper.setTo(recipientEmail);
				messageHelper.setSubject(subject);
				messageHelper.setText(message, true);
				messageHelper.addCc(props.getMailCopy());
			};

			mailSender.send(messagePreparator);
			log.info("---->>> Email successfully sent to {}", recipientEmail);
		} catch (MailException e) {
			e.printStackTrace();
			throw new AppException(e.getMessage());
		}
	}
}