package com.example.service;

import java.util.List;

import com.example.domain.Cart;
import com.example.domain.Product;

public interface ProductService {
	
	public List<Product> productAll();
	
	public Product productName(String name);
	
	public List<Product> productCategory(String category);
	
	public void insertProduct(String category, String name, int price, String description, String fileName); 
	
	public void updateProduct(String category, String name, String sname, String description, int price, String fileName);
	
	public void updateProduct2(String category, String name, String sname, String description, int price);
	
	public void deleteProduct(String name);
	
	public boolean checkDuplicateProductName(String name);
}
