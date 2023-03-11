package com.jwtauth.dto;

import java.io.Serializable;
import java.util.List;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class JwtResponse2 implements Serializable {
	private static final long serialVersionUID = 744689680486730629L;

	private String jwt;
	private String id;
	private String username;
	private String firstName;
	private String lastName;
	private List<String> roles;

}
