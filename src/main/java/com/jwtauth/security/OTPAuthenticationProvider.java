package com.jwtauth.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import com.jwtauth.dto.LoginDTO;
import com.jwtauth.dto.UserPrincipal;
import com.jwtauth.security.token.OTPAuthorizationToken;
import com.jwtauth.service.OTPMailService;

import lombok.Data;

@Data
public class OTPAuthenticationProvider implements AuthenticationProvider {

	private UserDetailsService userDetailsService;

	private OTPMailService otpMailService;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		boolean result = otpMailService.validateMailOTP(LoginDTO.builder().username(authentication.getName())
				.password(authentication.getCredentials().toString()).build());

		if (Boolean.FALSE.equals(result)) {
			throw new BadCredentialsException("");
		}

		UserPrincipal user = (UserPrincipal) userDetailsService.loadUserByUsername(authentication.getName());
		Authentication auth = new UsernamePasswordAuthenticationToken(user, authentication.getCredentials(),
				user.getAuthorities());
		return auth;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		if (authentication.isAssignableFrom(OTPAuthorizationToken.class)) {
			return true;
		}
		return false;
	}

}
