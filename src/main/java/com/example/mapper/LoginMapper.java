package com.example.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.example.domain.User;

@Repository
@Mapper
public interface LoginMapper {
	public User loginCom(@Param("userId")String userId, @Param("password")String password);
	
	public User loginSearch(@Param("userId")String userId);
	
	public void newLogin(@Param("userId")String userId, @Param("password")String encodePwd, @Param("username")String username, @Param("phone")String phone ,@Param("address")String address);
}
