package com.jwtauth.exception;

public class EmployeeNotFoundException extends RuntimeException {

	private static final long serialVersionUID = -2825829090485475510L;

	public EmployeeNotFoundException(String string) {
		super(string);
	}

}
