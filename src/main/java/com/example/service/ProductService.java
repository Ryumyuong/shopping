package com.example.service;

import java.util.List;

import com.example.domain.Cart;
import com.example.domain.Product;

public interface ProductService {
	public List<Product> productAll();
	
	public void inCart(String userId, String name, int price, String fileName);
	
	public List<Cart> product(String userId);
	
	public Cart total(String userId);
	
	public void order(String userId);
	
	public void deleteCartAll(String userId);
	
	public void deleteCart(String userId, String name);
}
