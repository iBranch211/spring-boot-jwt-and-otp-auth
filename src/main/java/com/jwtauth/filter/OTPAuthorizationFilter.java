package com.jwtauth.filter;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import com.jwtauth.security.token.OTPAuthorizationToken;

public class OTPAuthorizationFilter extends AbstractAuthenticationProcessingFilter {

	public OTPAuthorizationFilter() {
		super("/validateOTP");
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException, IOException, ServletException {
		if (request.getParameter("username") == null || request.getParameter("otp") == null) {
			return null;
		}
		return new OTPAuthorizationToken(request.getParameter("username"), "", request.getParameter("otp"));
	}
}
