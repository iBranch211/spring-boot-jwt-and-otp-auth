package com.jwtauth.service;

import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

@Service
public class OTPService {

	private static ConcurrentHashMap<String, OTP> inMemOTP = new ConcurrentHashMap<>();

	public OTP generateOTPForUser(@NotNull String userName) {
		OTP otp = OTP.builder().userOTP(Integer.valueOf(RandomStringUtils.randomNumeric(6)))
				.issuedAt(LocalDateTime.now()).build();
		inMemOTP.put(userName, otp);
		return otp;
	}

	public Boolean validateOTP(@NotNull String userName, @NotNull Integer otp) {
		if (inMemOTP.containsKey(userName) && inMemOTP.get(userName).getUserOTP().equals(otp)) {
			inMemOTP.remove(userName);
			return true;
		}
		return false;
	}

}
