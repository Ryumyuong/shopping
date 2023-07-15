package com.example.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {
	private int s_id;
	private String s_name;
	private int s_price;
	private String s_fileName;
	private byte[] s_fileData;
}
