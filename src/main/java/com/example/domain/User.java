package com.example.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User{
	private int id;
    private String userId;
    private String password;
    private String username;
    private String phone;
    private String address;
    private int money;
    private String roles;
    private String vip;
    private String kit;
    private String code;
}
