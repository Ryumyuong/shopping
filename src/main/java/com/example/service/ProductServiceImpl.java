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
	public void inCart(String userId, String name, String fileName) {
		productMapper.inCart(userId, name, fileName);
	}

	@Override
	public List<Cart> product(String userId) {
		return productMapper.product(userId);
	}
	
}
