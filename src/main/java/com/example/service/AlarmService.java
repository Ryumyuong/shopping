package com.example.service;

import java.util.List;

import com.example.domain.Alarm;

public interface AlarmService {
	
	public List<Alarm> getAlarm();
	
	public void insertAlarm(String name, String order_menu, String price);
}