package com.jwtauth.controller;

import java.util.Arrays;
import java.util.stream.Collectors;

import javax.mail.MessagingException;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jwtauth.dto.JwtResponse;
import com.jwtauth.dto.JwtResponse2;
import com.jwtauth.dto.LoginDTO;
import com.jwtauth.dto.UserPrincipal;
import com.jwtauth.jwt.JwtProvider;
import com.jwtauth.service.OTPMailService;

import lombok.extern.log4j.Log4j2;

@RestController
@RequestMapping("/auth")
@Log4j2
@CrossOrigin
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

	@PostMapping("/signinV2")
	public ResponseEntity<JwtResponse2> authenticateUserV2(@Valid @RequestBody LoginDTO login) {
		Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(login.getUsername(), login.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = tokenProvider.generateToken(authentication);
		UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
		return ResponseEntity.ok(JwtResponse2.builder().jwt(jwt).username(authentication.getName())
				.firstName(authentication.getName()).lastName(authentication.getName()).id("id")
				.roles(userPrincipal.getAuthorities().stream().map(GrantedAuthority::getAuthority)
						.map(role -> role.split("_")[1]).collect(Collectors.toList()))
				.build());

	}

	@PostMapping("/sendOTP")
	public ResponseEntity<String> sendOTP(@Valid @RequestBody LoginDTO login) {
		boolean result = otpMailService.sendOTPToEmail(login.getUsername());
		if (Boolean.TRUE.equals(result)) {
			return ResponseEntity.ok("OTP Sent successfully");
		} else {
			return ResponseEntity.ok("Unable to sent otp");
		}

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
