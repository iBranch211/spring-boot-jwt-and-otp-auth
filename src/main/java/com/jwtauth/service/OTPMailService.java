package com.jwtauth.service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.jwtauth.dto.LoginDTO;

@Service
public class OTPMailService {

	@Autowired
	private TemplateEngine templateEngine;

	@Autowired
	private JavaMailSender emailSender;

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private OTPService otpService;

	public void sendOTPToEmail(@NotNull String userMail) throws MessagingException {
		final Context ctx = new Context();
		ctx.setVariable("name", userMail.split("@")[0]);
		ctx.setVariable("otp", otpService.generateOTPForUser(userMail).getUserOTP());

		final String htmlContent = this.templateEngine.process("html/otp-mail.html", ctx);

		final MimeMessage mimeMessage = this.emailSender.createMimeMessage();
		final MimeMessageHelper message = new MimeMessageHelper(mimeMessage, "UTF-8");
		message.setFrom("no-reply@localhost.com");
		message.setTo(userMail);
		message.setSubject("Your OTP");
		message.setText(htmlContent, true);
		this.emailSender.send(mimeMessage);
	}

	public boolean validateMailOTP(@NotNull LoginDTO loginDTO) {
		try {
			return otpService.validateOTP(loginDTO.getUsername(), Integer.valueOf(loginDTO.getPassword()));
		} catch (RuntimeException e) {
			return false;
		}

	}
}
