package com.miguelmunozh.config;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.miguelmunozh.constants.Constants.*;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.miguelmunozh.services.JwtUserDetailsService;

/**
 * authorize requests
 */
@Component
public class JwtRequestFilter extends OncePerRequestFilter {

	private JwtUserDetailsService jwtUserDetailsService;
	private JwtTokenUtil jwtTokenUtil;

	// setter injection
	@Autowired
	public void setJwtUserDetailsService(JwtUserDetailsService jwtUserDetailsService) {
		this.jwtUserDetailsService = jwtUserDetailsService;
	}
	@Autowired
	public void setJwtTokenUtil(JwtTokenUtil jwtTokenUtil) {
		this.jwtTokenUtil = jwtTokenUtil;
	}

	/**
	 * check if the user and token are valid, if so authenticate user
	 */
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {

		//else check for the token
		// the token is located at the authorization header
		final String requestTokenHeader = request.getHeader(AUTHORIZATION);

		String username = null;
		String jwtToken = null;
		// if a valid token is sent in the request
		// JWT Token is in the form "Bearer token". Remove Bearer word and get
		// only the Token
		if (requestTokenHeader != null && requestTokenHeader.startsWith(TOKEN_PREFIX)) {
			// token without the bearer
			jwtToken = requestTokenHeader.substring(7);
			try {
				// get the username from the token
				username = jwtTokenUtil.getUsernameFromToken(jwtToken);
			} catch (IllegalArgumentException e) {
				System.out.println("Unable to get JWT Token");
			} catch (AccountExpiredException e) {
				System.out.println("JWT Token has expired");
			}
		}

		// If we get the token validate it.
		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			// authenticate the user on every request
			// find the user sending the request, find user in db to compare with the user in the token?
			UserDetails userDetails = this.jwtUserDetailsService.loadUserByUsername(username);

			// if token is valid configure Spring Security to manually set
			// authentication
			if (jwtTokenUtil.validateToken(jwtToken, userDetails)) {
				jwtTokenUtil.authentication(userDetails, request);
			}
		}
		chain.doFilter(request, response);
	}
}