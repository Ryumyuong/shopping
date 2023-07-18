package com.example.service;

import java.util.List;

import com.example.domain.Cart;
import com.example.domain.Product;

public interface ProductService {
	
	public List<Product> productAll();
	
	public Product productName(String name);
	
	public void insertProduct(String name, int price, String description, String fileName); 
	
	public void updateProduct(String name, String s_name, String description, int price, String fileName);
	
	public void deleteProduct(String name);
	
	public boolean checkDuplicateProductName(String name);
}
