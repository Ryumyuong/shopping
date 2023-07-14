package com.example.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.example.domain.Cart;
import com.example.domain.Product;

@Repository
@Mapper
public interface ProductMapper {
	public List<Product> productAll();
	
	public void inCart(@Param("userId")String userId, @Param("name")String name, @Param("price")int price, @Param("fileName")String fileName);
	
	public List<Cart> product(String userId);
	
	public Cart total(String userId);
	
	public void order(@Param("userId")String userId);
	
	public void deleteCartAll(@Param("userId")String userId);
	
	public void deleteCart(@Param("userId")String userId, @Param("name")String name);
}
