package com.example.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InOrder {
	private String userId;
	private String phone;
	private String address;
	private String inquire;
	private int total;
}
