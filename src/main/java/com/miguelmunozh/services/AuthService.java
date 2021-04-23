package com.miguelmunozh.services;

import com.miguelmunozh.config.JwtTokenUtil;
import com.miguelmunozh.dto.JwtRequest;
import com.miguelmunozh.dto.JwtResponse;
import com.miguelmunozh.models.User;
import com.miguelmunozh.dto.UserDTO;
import com.miguelmunozh.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * User service class
 */
@Service
@AllArgsConstructor
public class AuthService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtTokenUtil jwtTokenUtil;
    private final AuthenticationManager authenticationManager;

    public User signUp(UserDTO user) {
        User newUser = new User();
        newUser.setUsername(user.getUsername());
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));
        newUser.setFirstName(user.getFirstName());
        newUser.setLastName(user.getLastName());
        newUser.setEmail(user.getEmail());
        newUser.setImageUrl(user.getImageUrl());

        // already throws an error if the username already exists
        return userRepository.save(newUser);
    }

    public ResponseEntity<?> login(JwtRequest authenticationRequest) throws Exception {
        // authenticate user on login to create a token
        Authentication auth = authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());

        // then generate the token and return it
        final String token = jwtTokenUtil.generateToken(auth);
        final String username = jwtTokenUtil.getUsernameFromToken(token);
        final Date expiresAt = jwtTokenUtil.getExpirationDateFromToken(token);
        return ResponseEntity.ok(new JwtResponse(token, username, expiresAt));
    }

    private Authentication authenticate(String username, String password) throws Exception {
        try {
            Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username,
                    password));
            SecurityContextHolder.getContext().setAuthentication(authenticate);
            return authenticate;

        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }
    // should authenticate the user correctly to use this
    public User getCurrentUser() {
        org.springframework.security.core.userdetails.User principal = (org.springframework.security.core.userdetails.User) SecurityContextHolder.
                getContext().getAuthentication().getPrincipal();
        return userRepository.findByUsername(principal.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User name not found - " + principal.getUsername()));
    }
}
