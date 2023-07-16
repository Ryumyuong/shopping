package com.example.service;

import com.example.domain.User;

public interface LoginService {
	public User loginCom(String userId, String password);
	
	public void newLogin(String userId, String encodePwd, String username, String phone, String address);
}
