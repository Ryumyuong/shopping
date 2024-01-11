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
	
	public void insertCart(@Param("userId")String userId, @Param("name")String name, @Param("price")int price, @Param("description")String description, @Param("fileName")String fileName);
	
	public List<Cart> product(String userId);
	
	public Cart total(String userId);
	
	public void order(@Param("username")String username, @Param("userId")String userId, @Param("phone")String phone, @Param("address")String address, @Param("inquire")String inquire, @Param("order_menu")String order_menu, @Param("total")int total);
	
	public void orderCom(@Param("userId")String userId);
	
	public List<Orders> orderListAll(@Param("offset")int offset, @Param("limit") int pageSize);
	
	public List<Orders> orderList(@Param("userId")String userId, @Param("offset")int offset, @Param("limit") int pageSize);
	
	public void deleteCartAll(@Param("userId")String userId);
	
	public void deleteCart(@Param("userId")String userId, @Param("name")String name);
	
	public int runaTotal(@Param("userId")String userId);
	
	public int runaTotalAll();
	
	public void orderDeliver(@Param("id") String id);
	
	public void orderUnDeliver(@Param("id") String id);
}
