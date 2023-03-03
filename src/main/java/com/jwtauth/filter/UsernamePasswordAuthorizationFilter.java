package com.jwtauth.filter;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

public class UsernamePasswordAuthorizationFilter extends AbstractAuthenticationProcessingFilter {
	public UsernamePasswordAuthorizationFilter() {
		super("/signin");
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException, IOException, ServletException {
		return new UsernamePasswordAuthenticationToken(request.getParameter("username"),
				request.getParameter("password"));
	}
}
