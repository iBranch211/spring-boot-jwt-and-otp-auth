package com.jwtauth.dto;

import java.io.Serializable;

public class JwtResponse implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 744689680486730629L;

	String jwt;

	public JwtResponse(String jwt) {
		this.jwt = jwt;
	}

	public String getJwt() {
		return jwt;
	}

	public void setJwt(String jwt) {
		this.jwt = jwt;
	}

}
