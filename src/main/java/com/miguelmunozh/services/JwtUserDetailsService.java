package com.miguelmunozh.services;

import java.util.ArrayList;
import java.util.Collection;

import com.miguelmunozh.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.miguelmunozh.models.User;

import static java.util.Collections.singletonList;

@Service
public class JwtUserDetailsService implements UserDetailsService {

	private UserRepository userRepository;

	@Autowired
	public void setUserRepository(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByUsername(username)
				.orElseThrow(()->new UsernameNotFoundException("User not found with username: " + username));

		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
				new ArrayList<>());
	}

	private Collection<? extends GrantedAuthority> getAuthorities(String role) {
		return singletonList(new SimpleGrantedAuthority(role));
	}
}