package com.example.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.example.domain.Alarm;

@Repository
@Mapper
public interface AlarmMapper {
	public List<Alarm> getAlarm();
	
	public void insertAlarm(@Param("username")String name, @Param("menu")String order_menu, @Param("price")String price);
}