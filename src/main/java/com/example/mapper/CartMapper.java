package com.example.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.example.domain.Cart;
import com.example.domain.Orders;

@Repository
@Mapper
public interface CartMapper {
	public void inCart(@Param("userId")String userId, @Param("name")String name, @Param("description")String description, @Param("price")int price, @Param("fileName")String fileName);
	
	public List<Cart> product(String userId);
	
	public Cart total(String userId);
	
	public void order(@Param("username")String username, @Param("userId")String userId, @Param("phone")String phone, @Param("address")String address, @Param("order_menu")String order_menu, @Param("total")int total);
	
	public void orderCom(@Param("userId")String userId);
	
	public List<Orders> orderListAll();
	
	public List<Orders> orderList(@Param("userId")String userId);
	
	public void deleteCartAll(@Param("userId")String userId);
	
	public void deleteCart(@Param("userId")String userId, @Param("name")String name);
	
	public int runaTotal(@Param("userId")String userId);
	
	public int runaTotalAll();
	
	public void orderDeliver(@Param("date") String date);
}
