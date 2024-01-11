package com.example.service;

import java.util.List;

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

	@Override
	public List<User> userList(int pageNumber, int pageSize) {
	        int offset = (pageNumber - 1) * pageSize;
	        return loginMapper.userList(offset, pageSize);
	}

	@Override
	public void updateUser(String userId, String encodePwd, String username, String phone, String address) {
		loginMapper.updateUser(userId, encodePwd, username, phone, address);
	}

	@Override
	public void deleteUser(String userId) {
		loginMapper.deleteUser(userId);
		
	}

	@Override
	public void addRuna(String userId, int money) {
		loginMapper.addRuna(userId, money);
		
	}

	@Override
	public List<User> getUser(String userId) {
		return loginMapper.getUser(userId);
	}
}
