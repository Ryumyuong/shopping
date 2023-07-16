package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.auth.CustomUserDetails;
import com.example.domain.User;
import com.example.mapper.LoginMapper;

@Service
public class CustomUserDetailsService implements UserDetailsService{
	
	@Autowired
	private LoginMapper loginmapper;

	@Override
	public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
		System.out.println(userId);
		User user = loginmapper.loginSearch(userId);
		System.out.println(user);
		if(user != null) {
			return new CustomUserDetails(user);
		}
		return null;
	}

}

