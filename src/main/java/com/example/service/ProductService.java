package com.example.service;

import java.util.List;

import com.example.domain.Cart;
import com.example.domain.Product;

public interface ProductService {
	
	public List<Product> productAll();
	
	public Product productName(String name);
	
	public void inCart(String userId, String name, int price);
	
	public List<Cart> product(String userId);
	
	public Cart total(String userId);
	
	public void order(String userId);
	
	public void deleteCartAll(String userId);
	
	public void deleteCart(String userId, String name);
	
	public void updateProduct(String name, String s_name, String price, String fileName, byte[] imageData);
}
