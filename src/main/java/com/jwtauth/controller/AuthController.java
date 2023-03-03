package com.jwtauth.controller;

import javax.mail.MessagingException;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jwtauth.dto.JwtResponse;
import com.jwtauth.dto.LoginDTO;
import com.jwtauth.jwt.JwtProvider;
import com.jwtauth.service.OTPMailService;

import lombok.extern.log4j.Log4j2;

@RestController
@RequestMapping("/auth")
@Log4j2
public class AuthController {
	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	JwtProvider tokenProvider;

	@Autowired
	OTPMailService otpMailService;

	@PostMapping("/signin")
	public ResponseEntity<JwtResponse> authenticateUser(@Valid @RequestBody LoginDTO login) {
		Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(login.getUsername(), login.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = tokenProvider.generateToken(authentication);
		return ResponseEntity.ok(new JwtResponse(jwt));
	}

	@PostMapping("/sendOTP")
	public ResponseEntity<String> sendOTP(@Valid @RequestBody LoginDTO login) {
		try {
			otpMailService.sendOTPToEmail(login.getUsername());
		} catch (MessagingException e) {
			log.error("Error sending otp via mail {}", e.getMessage());
			e.printStackTrace();
			ResponseEntity.internalServerError();
		}
		return ResponseEntity.ok("OTP Sent successfully");
	}

	@PostMapping("/validateOTP")
	public ResponseEntity<JwtResponse> validateOTP(@Valid @RequestBody LoginDTO login) {

		Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(login.getUsername(), login.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = tokenProvider.generateToken(authentication);
		return ResponseEntity.ok(new JwtResponse(jwt));

	}
}
