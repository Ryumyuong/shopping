package com.example.task;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.domain.Card;
import com.example.domain.User;
import com.example.mapper.LoginMapper;
import com.example.service.LoginService;
import com.example.service.TimeService;

@Component
public class ScheduledTask {
	
	@Autowired
	TimeService timeService;
	
	@Autowired
	LoginService loginService;
	
	@Autowired
	LoginMapper loginMapper;
	

//    @Scheduled(cron = "0 0 0 15 * ?")
    @Scheduled(cron = "0 * * * * ?")
    public void checkAndDeductAmount() {
    	List<Card> cardList = timeService.getList();
    	for(int i=0;i<cardList.size();i++) {
    		String time = cardList.get(i).getOrder_time();
    		String userId = cardList.get(i).getName();
    		String days = cardList.get(i).getDays();
    		int day = Integer.parseInt(days);
    		int luna = cardList.get(i).getLuna();
    		
    		day -= 1;
    		User user = loginMapper.loginSearch(userId);
			int money = user.getMoney();
			money -= luna;
			loginService.addRuna(userId, money);
			if(day == 0) {
				timeService.deleteCard(time);
			} else {
				String dayss = String.valueOf(day);
				timeService.updateCard(time, dayss, luna);
			}			
    	}
    }
}