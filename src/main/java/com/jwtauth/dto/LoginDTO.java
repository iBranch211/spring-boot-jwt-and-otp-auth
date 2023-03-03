package com.jwtauth.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class LoginDTO {

	private String username;

	private String password;

}
