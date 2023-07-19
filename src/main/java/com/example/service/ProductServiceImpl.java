package com.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.domain.Cart;
import com.example.domain.Product;
import com.example.mapper.ProductMapper;

@Service
public class ProductServiceImpl implements ProductService{
	
	@Autowired
	private ProductMapper productMapper;
	
	@Override
	public List<Product> productAll() {
		return productMapper.productAll();
	}
	
	public List<Product> productCategory(String category) {
		return productMapper.productCategory(category);
	}
	
	@Override
	public void insertProduct(String category, String name, int price, String description,  String fileName) {
		productMapper.insertProduct(category, name, description, price, fileName);	
	}

	@Override
	public void updateProduct(String category, String name, String description, int price, String fileName) {
		productMapper.updateProduct(category, name, description, price, fileName);	
	}
	
	@Override
	public void deleteProduct(String name) {
		productMapper.deleteProduct(name);	
	}

	@Override
	public Product productName(String name) {
		
		return productMapper.productName(name);
	}

	@Override
	public boolean checkDuplicateProductName(String name) {
		int count = productMapper.checkDuplicateProductName(name);
		return count > 0;
		
	}
	
}
