package com.miguelmunozh.dto;

import lombok.Data;

// DTO (data transfer object) pattern
@Data
public class UserDTO {
	private String firstName;
	private String lastName;
	private String username;
	private String password;
	private String email;
	private String imageUrl;
}