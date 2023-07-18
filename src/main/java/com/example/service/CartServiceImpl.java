package com.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.domain.Cart;
import com.example.domain.Orders;
import com.example.mapper.CartMapper;

@Service
public class CartServiceImpl implements CartService{
	@Autowired
	private CartMapper cartMapper;
	
	@Override
	public void inCart(String userId, String name, String description, int price, String fileName) {
		cartMapper.inCart(userId, name, description, price, fileName);
	}

	@Override
	public List<Cart> product(String userId) {
		return cartMapper.product(userId);
	}
	
	@Override
	public Cart total(String userId) {
		return cartMapper.total(userId);
	}
	
	@Override
	public void order(String username, String userId, String phone, String address, String order_menu, int total) {
		cartMapper.order(username, userId, phone, address, order_menu, total);
	}
	
	@Override
	public void orderCom(String userId) {
		cartMapper.orderCom(userId);	
	}
	
	@Override
	public List<Orders> orderListAll() {
		return cartMapper.orderListAll();
	}
	
	@Override
	public List<Orders> orderList(String username) {
		return cartMapper.orderList(username);
	}
	
	@Override
	public void deleteCartAll(String userId) {
		cartMapper.deleteCartAll(userId);
	}

	@Override
	public void deleteCart(String userId, String name) {
		cartMapper.deleteCart(userId, name);
		
	}

}
