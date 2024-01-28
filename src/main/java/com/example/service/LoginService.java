package com.example.service;

import java.util.List;

import com.example.domain.User;

public interface LoginService {
	public User loginCom(String userId, String password);
	
	public void newLogin(String userId, String encodePwd, String username, String phone, String address, String vip);
	
	public List<User> userList(int pageNumber, int PageSize);
	
	public void updateUser(String userId, String encodePwd, String username, String phone, String address, String vip);
	
	public void deleteUser(String userId);
	
	public void addRuna(String userId, int money);
	
	public List<User> getUser(String userId);
	
	public void updateCode(String userId, String code);
}
