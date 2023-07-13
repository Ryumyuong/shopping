package com.example.domain;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {
	private int s_id;
	private String s_name;
	private String s_description;
	private String s_condition;
	private String s_fileName;
}
