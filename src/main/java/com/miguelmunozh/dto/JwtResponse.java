package com.miguelmunozh.dto;

import java.io.Serializable;
import java.util.Date;

public class JwtResponse implements Serializable {

	private static final long serialVersionUID = -8091879091924046844L;
	private final String jwtToken;
	private final String username;
	private final Date expiresAt;

	public JwtResponse(String jwtToken, String username, Date expiresAt) {
		this.jwtToken = jwtToken;
		this.username = username;
		this.expiresAt = expiresAt;
	}

	public String getToken() {
		return this.jwtToken;
	}
	public String getUsername() {
		return this.username;
	}
	public Date getExpiresAt() {
		return expiresAt;
	}
}