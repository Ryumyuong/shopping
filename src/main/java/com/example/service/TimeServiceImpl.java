package com.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.domain.Card;
import com.example.mapper.TimeMapper;

@Service
public class TimeServiceImpl implements TimeService {
	@Autowired
	TimeMapper timeMapper;

	@Override
	public List<Card> getList() {
		return timeMapper.getList();
	}

	@Override
	public void insertCard(String name, String days, int luna) {
		timeMapper.insertCard(name, days, luna);		
	}

	@Override
	public void deleteCard(String time) {
		timeMapper.deleteCard(time);
		
	}

	@Override
	public void updateCard(String time, String days, int luna) {
		timeMapper.updateCard(time, days, luna);
		
	}
	
}