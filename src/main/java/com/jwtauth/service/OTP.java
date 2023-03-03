package com.jwtauth.service;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Builder
@EqualsAndHashCode
public class OTP {

	private Integer userOTP;

	private LocalDateTime issuedAt;

}
