package com.example.service;

import java.util.List;

import com.example.domain.Cart;

public interface CartService {
	public void inCart(String userId, String name, String description, int price, String fileName);
	
	public List<Cart> product(String userId);
	
	public Cart total(String userId);
	
	public void order(String userId);
	
	public void deleteCartAll(String userId);
	
	public void deleteCart(String userId, String name);
}
