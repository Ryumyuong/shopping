package com.example.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {
	private Integer s_id;
	private String s_category;
	private String s_name;
	private Integer s_price;
	private String s_description;
	private String s_fileName;
}
