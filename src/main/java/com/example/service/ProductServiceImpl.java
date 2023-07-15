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
		System.out.println("ServiceImpl List " + productMapper.productAll());
		return productMapper.productAll();
	}

	@Override
	public void inCart(String userId, String name, int price) {
		productMapper.inCart(userId, name, price);
	}

	@Override
	public List<Cart> product(String userId) {
		return productMapper.product(userId);
	}
	
	@Override
	public Cart total(String userId) {
		return productMapper.total(userId);
	}
	
	@Override
	public void order(String userId) {
		productMapper.order(userId);
	}
	
	@Override
	public void deleteCartAll(String userId) {
		productMapper.deleteCartAll(userId);
	}

	@Override
	public void deleteCart(String userId, String name) {
		productMapper.deleteCart(userId, name);
		
	}

	@Override
	public void updateProduct(String name, String s_name, String price, String fileName, byte[] imageData) {
		System.out.print("ServiceImpl update " + imageData);
		productMapper.updateProduct(name, s_name, price, fileName, imageData);
		
	}

	@Override
	public Product productName(String name) {
		
		return productMapper.productName(name);
	}
	
}
