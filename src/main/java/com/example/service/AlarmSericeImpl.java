package com.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.domain.Alarm;
import com.example.mapper.AlarmMapper;

@Service
public class AlarmSericeImpl implements AlarmService {
	@Autowired
	AlarmMapper alarmMapper;

	@Override
	public void insertAlarm(String name, String order_menu, String price) {
	            alarmMapper.insertAlarm(name, order_menu, price);	
	}

	@Override
	public List<Alarm> getAlarm() {
		return alarmMapper.getAlarm();		
	}
	
}