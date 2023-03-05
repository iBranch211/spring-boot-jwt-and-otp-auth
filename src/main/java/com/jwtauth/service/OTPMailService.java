package com.jwtauth.service;

import javax.mail.MessagingException;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

import com.jwtauth.dto.LoginDTO;

@Service
public class OTPMailService {

	@Autowired
	MailService mailService;

	@Autowired
	private OTPService otpService;

	public boolean sendOTPToEmail(@NotNull String userMail) {
		final Context ctx = new Context();
		ctx.setVariable("name", userMail.split("@")[0]);
		ctx.setVariable("otp", otpService.generateOTPForUser(userMail).getUserOTP());

		return mailService.sendHTMLMail(ctx, "otp-mail", userMail, "Your OTP");

	}

	public boolean validateMailOTP(@NotNull LoginDTO loginDTO) {
		try {
			return otpService.validateOTP(loginDTO.getUsername(), Integer.valueOf(loginDTO.getPassword()));
		} catch (RuntimeException e) {
			return false;
		}

	}
}
