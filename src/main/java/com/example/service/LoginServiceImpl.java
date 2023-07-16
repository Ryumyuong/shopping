package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.domain.User;
import com.example.mapper.LoginMapper;

@Service
public class LoginServiceImpl implements LoginService{
	@Autowired
	private LoginMapper loginMapper;

	@Override
	public User loginCom(String userId, String password) {
		
		return loginMapper.loginCom(userId, password);
	}

	@Override
	public void newLogin(String userId, String encodePwd, String username, String phone, String address) {
		loginMapper.newLogin(userId, encodePwd, username, phone, address);
		
	}
}
