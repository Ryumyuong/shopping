package com.example.service;

import java.util.List;

import com.example.domain.Card;

public interface TimeService {
	public List<Card> getList();
	
	public void insertCard(String name, String days, int luna);
	
	public void deleteCard(String time);
	
	public void updateCard(String time, String days, int luna);
}