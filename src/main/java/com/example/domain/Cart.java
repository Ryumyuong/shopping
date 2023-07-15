package com.example.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cart {
	private int c_id;
	private String userId;
	private String s_name;
	private int s_price;
	private String s_description;
	private String fileName;
	private String c_condition;
	private int count;
	private int cost;
	private int total;
}
