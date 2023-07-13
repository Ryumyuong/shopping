package com.example.service;

import java.util.List;

import com.example.domain.Cart;
import com.example.domain.Product;

public interface ProductService {
	public List<Product> productAll();
	
	public void inCart(String userId, String name, String fileName);
	
	public List<Cart> product(String userId);
}
