package com.jwtauth.security.token;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import lombok.Getter;

@Getter
public class OTPAuthorizationToken extends UsernamePasswordAuthenticationToken {

	private static final long serialVersionUID = 5310873959240548867L;

	private String otp;

	public OTPAuthorizationToken(String username, String password, String otp) {
		super(username, password);
		this.otp = otp;
	}

}
