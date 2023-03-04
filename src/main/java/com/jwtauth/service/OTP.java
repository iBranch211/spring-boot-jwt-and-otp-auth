package com.jwtauth.service;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@Builder
@EqualsAndHashCode
@ToString
public class OTP {

	private Integer userOTP;

	private LocalDateTime issuedAt;

}
