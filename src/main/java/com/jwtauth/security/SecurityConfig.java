package com.jwtauth.security;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.jwtauth.filter.JwtAuthenticationFilter;
import com.jwtauth.filter.OTPAuthorizationFilter;
import com.jwtauth.filter.UsernamePasswordAuthorizationFilter;
import com.jwtauth.service.OTPMailService;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class SecurityConfig {

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	OTPMailService otpMailService;

	@Autowired
	JwtAuthenticationFilter getJwtAuthenticationFilter;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.headers().frameOptions().sameOrigin().and().csrf().disable().authorizeRequests()
				.antMatchers(HttpMethod.OPTIONS, "/**").permitAll().and().authorizeRequests().antMatchers("/auth/**")
				.permitAll().antMatchers("/protected/**").authenticated().and().exceptionHandling()
				.authenticationEntryPoint((req, res, ex) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED,
						"UNAUTHORIZED : " + ex.getMessage()))
				.and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		http.addFilterBefore(getJwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
		http.addFilterBefore(new UsernamePasswordAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
		http.addFilterBefore(new OTPAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);

		DaoAuthenticationProvider usernamePasswordAuthProvider = new DaoAuthenticationProvider();
		usernamePasswordAuthProvider.setUserDetailsService(userDetailsService);
		usernamePasswordAuthProvider.setPasswordEncoder(passwordEncoder());

		http.authenticationProvider(usernamePasswordAuthProvider);

		OTPAuthenticationProvider otpAuthenticationProvider = new OTPAuthenticationProvider();
		otpAuthenticationProvider.setUserDetailsService(userDetailsService);
		otpAuthenticationProvider.setOtpMailService(otpMailService);

		http.authenticationProvider(otpAuthenticationProvider);

		return http.build();
	}

	@Bean
	public AuthenticationManager authenticationManager() {
		DaoAuthenticationProvider usernamePasswordAuthProvider = new DaoAuthenticationProvider();
		usernamePasswordAuthProvider.setUserDetailsService(userDetailsService);
		usernamePasswordAuthProvider.setPasswordEncoder(passwordEncoder());

		OTPAuthenticationProvider otpAuthenticationProvider = new OTPAuthenticationProvider();
		otpAuthenticationProvider.setUserDetailsService(userDetailsService);
		otpAuthenticationProvider.setOtpMailService(otpMailService);

		List<AuthenticationProvider> providers = List.of(otpAuthenticationProvider, usernamePasswordAuthProvider);

		return new ProviderManager(providers);
	}
}
