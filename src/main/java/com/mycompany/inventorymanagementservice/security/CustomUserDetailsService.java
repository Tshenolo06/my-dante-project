package com.mycompany.inventorymanagementservice.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.mycompany.inventorymanagementservice.entity.User;
import com.mycompany.inventorymanagementservice.exception.NotFoundException;
import com.mycompany.inventorymanagementservice.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService{
	
	private final UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		User user = userRepository.findByEmail(username)
				.orElseThrow(()-> new NotFoundException("User email not found"));
		
		
		return AuthUser.builder()
				.user(user)
				.build();
	}

}
