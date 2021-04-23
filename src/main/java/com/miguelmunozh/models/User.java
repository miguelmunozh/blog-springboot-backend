package com.miguelmunozh.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Entity
@Table(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long userId;
	@NotBlank(message = "First name is required")
	private String firstName;
	@NotBlank(message = "Last name is required")
	private String lastName;

	@NotBlank(message = "Username is required")
	@Column(unique=true)
	private String username;

	@JsonIgnore
	@NotBlank(message = "Password is required")
	private String password;
	@NotBlank(message = "Email is required")
	private String email;
	private String imageUrl;

}