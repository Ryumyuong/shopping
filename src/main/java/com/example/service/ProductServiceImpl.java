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
	
	@Override
	public List<Product> productCategory(String category) {
		return productMapper.productCategory(category);
	}
	
	@Override
	public void insertProduct(String category, String name, int price, String description,  String fileName) {
		productMapper.insertProduct(category, name, description, price, fileName);	
	}

	@Override
	public void updateProduct(String category, String name, String sname, String description, int price, String fileName) {
		productMapper.updateProduct(category, name, sname, description, price, fileName);	
	}
	
	@Override
	public void updateProduct2(String category, String name, String sname, String description, int price) {
		productMapper.updateProduct2(category, name, sname, description, price);	
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

	@Override
	public List<Product> productname(String name) {
		return productMapper.productname(name);
	}
	
}
