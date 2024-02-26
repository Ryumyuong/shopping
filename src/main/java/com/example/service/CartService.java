package com.example.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.example.domain.Cart;
import com.example.domain.Orders;

public interface CartService {
	public void inCart(String userId, String name, String description, int price, String fileName);
	
	public void insertCart(String userId, String name, int price, String description, String fileName);
	
	public List<Cart> product(String userId);
	
	public Cart total(String userId);
	
	public void order(String username, String userId, String phone, String address, String inquire, String order_menu, String time, int total);
	
	public List<Orders> orderListAll(int pageNumber, int pageSize);
	
	public List<Orders> orderList(String userId, int pageNumber, int pageSize);
	
	public void orderCom(@Param("userId")String userId);
	
	public void deleteCartAll(String userId);
	
	public void deleteCart(String userId, String name);
	
	public int runaTotal(String userId);
	
	public int runaTotalAll();
	
	public void orderDeliver(String id);
	
	public void orderUnDeliver(String id);
	
	public void delivering(int id);

}
