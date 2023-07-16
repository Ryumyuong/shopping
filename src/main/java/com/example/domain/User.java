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
    private String roles;
}
