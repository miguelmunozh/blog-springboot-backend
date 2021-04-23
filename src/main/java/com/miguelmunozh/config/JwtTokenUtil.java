package com.miguelmunozh.config;

import java.io.Serializable;
import java.util.*;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import static com.miguelmunozh.constants.Constants.*;

import com.miguelmunozh.services.JwtUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class JwtTokenUtil implements Serializable {

	private static final long serialVersionUID = -2550185165626007488L;

	private JwtUserDetailsService jwtUserDetailsService;

	@Autowired
	public void setJwtUserDetailsService(JwtUserDetailsService jwtUserDetailsService) {
		this.jwtUserDetailsService = jwtUserDetailsService;
	}

	// located in a secured server maybe, used to decode and encode
	@Value("${jwt.secret}")
	private String secret;

	//retrieve username from jwt token
	public String getUsernameFromToken(String token) {
		JWTVerifier verifier = getJWTVerifier();
		return verifier.verify(token).getSubject();
	}

	//retrieve expiration date from jwt token
	public Date getExpirationDateFromToken(String token) {
		JWTVerifier verifier = getJWTVerifier();
		return verifier.verify(token).getExpiresAt();
	}

	private JWTVerifier getJWTVerifier() {
		JWTVerifier verifier;
		try {
			Algorithm algorithm = Algorithm.HMAC512(secret);
			verifier = JWT.require(algorithm).withIssuer(COMPANY_NAME).build();
		}catch (JWTVerificationException e){
				throw new JWTVerificationException(TOKEN_CANNOT_BE_VERIFIED);
		}
		return verifier;
	}

	//check if the token has expired
	private Boolean isTokenExpired(String token) {
		final Date expiration = getExpirationDateFromToken(token);
		return expiration.before(new Date());
	}

	// generate token for user once the user is validated we can create a token for the user
	public String generateToken(Authentication auth) {

		org.springframework.security.core.userdetails.User principal =
				(org.springframework.security.core.userdetails.User) auth.getPrincipal();
		final UserDetails userDetails = jwtUserDetailsService
				.loadUserByUsername(principal.getUsername());

		String[] claims = getClaimsFromUser(userDetails);

		// todo the claims should be the info of the user
//		claims.put("username",userDetails.getUsername());

		return JWT.create()
				.withIssuer(COMPANY_NAME)
				.withAudience(COMPANY_ADMINISTRATION)
				.withSubject(principal.getUsername())
				.withArrayClaim(AUTHORITIES, claims)
				.withIssuedAt(new Date())
				.withExpiresAt(new Date(System.currentTimeMillis() + 1000 * JWT_TOKEN_VALIDITY))
				.sign(Algorithm.HMAC512(secret.getBytes()));
	}

	private String[] getClaimsFromUser(UserDetails userDetails) {
		List<String> authorities = new ArrayList<>();
		for(GrantedAuthority grantedAuthority : userDetails.getAuthorities()){
			authorities.add(grantedAuthority.getAuthority());
		}
		return authorities.toArray(new String[0]);
	}

	//validate token
	public Boolean validateToken(String token, UserDetails userDetails) {
		final String username = getUsernameFromToken(token);
		return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}

	// set the user as authenticated user
	public void authentication(UserDetails userDetails, HttpServletRequest request){
			UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
					userDetails, null, userDetails.getAuthorities());
			usernamePasswordAuthenticationToken
					.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
			// After setting the Authentication in the context, we specify
			// that the current user is authenticated.
			SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
	}
}