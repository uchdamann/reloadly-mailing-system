package com.reloadly.devops.configs;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import com.reloadly.devops.utilities.AppProperties;
import com.reloadly.devops.utilities.EncryptUtil;
import com.reloadly.devops.utilities.MailProperties;

@Configuration
public class UtilConfig {
	@Autowired
	private AppProperties prop;
	@Autowired
	private MailProperties mailProperties;

//	Configure encryption
	@Bean
	public EncryptUtil encryptUtil() {
		return new EncryptUtil(prop.getEncryptKey());
	}
	
//	Configure mailing
	@Bean
	public JavaMailSender getJavaMailSender() {
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		mailSender.setHost(mailProperties.getHost());
		mailSender.setPort(mailProperties.getPort());

		mailSender.setUsername(mailProperties.getUsername());
		mailSender.setPassword(mailProperties.getPassword());

		Properties props = mailSender.getJavaMailProperties();
		props.put("mail.transport.protocol", mailProperties.getProtocol());
		props.put("mail.smtp.auth", mailProperties.getSmtpAuth());
		props.put("mail.smtp.starttls.enable", mailProperties.getEnableStarttls());
		props.put("mail.debug", mailProperties.getDebug());
		props.put("mail.smtp.ssl.trust", mailProperties.getHost());

		return mailSender;
	}
}