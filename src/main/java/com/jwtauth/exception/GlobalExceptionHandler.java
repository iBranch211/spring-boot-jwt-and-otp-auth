package com.jwtauth.exception;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.jwtauth.dto.ApiError;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		List<String> fieldErrors = ex.getBindingResult().getFieldErrors().stream()
				.map(field -> field.getField() + ", " + field.getDefaultMessage()).collect(Collectors.toList());

		List<String> globalErrors = ex.getBindingResult().getGlobalErrors().stream()
				.map(field -> field.getObjectName() + ", " + field.getDefaultMessage()).collect(Collectors.toList());
		List<String> errors = new ArrayList<String>();
		errors.addAll(globalErrors);
		errors.addAll(fieldErrors);

		ApiError err = new ApiError(LocalDateTime.now(), "Validation Errors", errors);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
	}

	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<Object> handleConstraintViolationException(Exception ex, WebRequest request) {

		List<String> details = new ArrayList<String>();
		details.add(ex.getMessage());
		ApiError err = new ApiError(LocalDateTime.now(), "Constraint Violation", details);

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
	}

	@ExceptionHandler(EmployeeNotFoundException.class)
	public ResponseEntity<Object> handleResourceNotFoundException(EmployeeNotFoundException ex) {

		List<String> details = new ArrayList<String>();
		details.add(ex.getMessage());
		ApiError err = new ApiError(LocalDateTime.now(), "Employee Not Found", details);
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err);
	}
	
	@ExceptionHandler(BadCredentialsException.class)
	public ResponseEntity<Object> handleBadCredentialsException(BadCredentialsException ex) {
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
	}
	
}
