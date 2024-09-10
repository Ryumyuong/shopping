package com.example.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Alarm {
	private int a_id;
	private String a_time;
	private String a_username;
	private String a_menu;
	private String a_price;
}
