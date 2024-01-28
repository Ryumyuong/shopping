package com.example.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.example.domain.User;

@Repository
@Mapper
public interface LoginMapper {
	public User loginCom(@Param("userId")String userId, @Param("password")String password);
	
	public User loginSearch(@Param("userId")String userId);
	
	public void newLogin(@Param("userId")String userId, @Param("password")String encodePwd, @Param("username")String username, @Param("phone")String phone ,@Param("address")String address, @Param("vip")String vip);
	
	public List<User> userList(@Param("offset")int offset, @Param("limit")int pageSize);
	
	public void updateUser(@Param("userId")String userId, @Param("password")String encodePwd, @Param("username")String username, @Param("phone")String phone ,@Param("address")String address, @Param("vip")String vip);
	
	public void deleteUser(@Param("userId")String userId);
	
	public void addRuna(@Param("userId")String userId, @Param("money")int money);
	
	public List<User> getUser(@Param("userId")String userId);
	
	public void oneKit(@Param("userId")String userId);
	
	public void noKit(@Param("userId")String userId);
	
	public void updateCode(@Param("userId")String userId, @Param("code") String code);
}
