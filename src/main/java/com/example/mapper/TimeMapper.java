package com.example.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.example.domain.Card;

@Repository
@Mapper
public interface TimeMapper {
	public List<Card> getList();
	
	public void insertCard(@Param("name")String name, @Param("order_menu") String order_menu, @Param("days")String days, @Param("luna")int luna);
	
	public void deleteCard(@Param("time")String time);
	
	public void updateCard(@Param("time")String time, @Param("days")String days, @Param("luna")int luna);
}