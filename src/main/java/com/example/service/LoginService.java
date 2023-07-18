package com.example.service;

import java.util.List;

import com.example.domain.User;

public interface LoginService {
	public User loginCom(String userId, String password);
	
	public void newLogin(String userId, String encodePwd, String username, String phone, String address);
	
	public List<User> userList();
	
	public void updateUser(String userId, String encodePwd, String username, String phone, String address);
	
	public void deleteUser(String userId);
	
	public void addRuna(String userId, int money);
}
