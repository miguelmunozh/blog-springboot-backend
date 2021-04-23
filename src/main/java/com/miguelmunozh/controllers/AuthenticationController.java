package com.miguelmunozh.controllers;

import com.miguelmunozh.repositories.UserRepository;
import com.miguelmunozh.services.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import com.miguelmunozh.services.JwtUserDetailsService;


import com.miguelmunozh.config.JwtTokenUtil;
import com.miguelmunozh.dto.JwtRequest;
import com.miguelmunozh.dto.JwtResponse;
import com.miguelmunozh.dto.UserDTO;

/**
 * log in and register controller
 */
@RestController
@AllArgsConstructor
public class AuthenticationController {

	private final AuthService authService;
	private final UserRepository userRepository;

	// login request, auth resource from spring-reddit-clone-master
	@PostMapping("/login")
	public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {
		return authService.login(authenticationRequest);
	}
	
	@PostMapping("/register")
	public ResponseEntity<?> saveUser(@RequestBody UserDTO user) {
		return ResponseEntity.ok(authService.signUp(user));
	}


	@GetMapping("/user/{username}")
	public ResponseEntity<?> get(@PathVariable String username) {
		return ResponseEntity.ok(userRepository.findByUsername(username));
	}
}