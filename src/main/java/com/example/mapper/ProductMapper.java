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
	
	public Product productName(@Param("name")String name);
	
	public List<Product> productCategory(@Param("category") String category);
	
	public void insertProduct(@Param("category")String category, @Param("name")String name, @Param("description")String description, @Param("price")int price, @Param("fileName")String fileName);
	
	public void updateProduct(@Param("category")String category, @Param("name")String name, @Param("s_name")String s_name, @Param("description")String description, @Param("price")int price, @Param("fileName")String fileName);
	
	public void deleteProduct(@Param("name")String name);
	
	public int checkDuplicateProductName(@Param("name")String name);
}
