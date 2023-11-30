package com.example.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Orders {
	private int id;
	private String order_time;
	private String username;
	private String userId;
	private String phone;
	private String address;
	private String inquire;
	private String order_menu;
	private int order_price;
	private int total;
	private String deliver;
}
