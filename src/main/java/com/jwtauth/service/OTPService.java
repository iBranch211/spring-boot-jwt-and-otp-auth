package com.jwtauth.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class OTPService {

	private static ConcurrentHashMap<String, OTP> inMemOTP = new ConcurrentHashMap<>();

	@Value("${otpvalidityinmin}")
	private Integer otpValidityInMin;

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

	@Scheduled(cron = "${app.otp.invalidateCron}")
	private void invalidateOTP() {
		LocalDateTime now = LocalDateTime.now();
		List<String> userIdsToRemove = new ArrayList<>();
		for (Entry<String, OTP> entry : inMemOTP.entrySet()) {
			Duration duration = Duration.between(entry.getValue().getIssuedAt(), now);
			if (duration.toMinutes() >= otpValidityInMin) {
				log.info("OTP will be removed {}", entry);
				userIdsToRemove.add(entry.getKey());
			}
		}
		userIdsToRemove.forEach(id -> inMemOTP.remove(id));
	}
}
