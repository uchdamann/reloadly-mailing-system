package com.reloadly.devops.utilities;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data
@Configuration
@ConfigurationProperties("app.mail")
public class MailProperties {
	private int port;
	private String host;
	private String username;
	private String password;
	private String protocol;
	private Boolean enableStarttls;
	private Boolean smtpAuth;
	private Boolean debug;
}