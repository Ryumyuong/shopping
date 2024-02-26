package com.example.task;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.domain.Card;
import com.example.domain.User;
import com.example.mapper.LoginMapper;
import com.example.service.CartService;
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
	
	@Autowired
	CartService cartService;

    @Scheduled(cron = "0 0 0 15 * ?")
//    @Scheduled(cron = "0 * * * * ?")
    public void checkAndDeductAmount() {
    	List<Card> cardList = timeService.getList();
    	for(int i=0;i<cardList.size();i++) {
    		String time = cardList.get(i).getOrder_time();
    		String userId = cardList.get(i).getName();
    		String days = cardList.get(i).getDays();
    		String order_menu = cardList.get(i).getOrder_menu();
    		if(!days.equals("일시불")) {
    			int day = Integer.parseInt(days);
        		int luna = cardList.get(i).getLuna();
        		
        		System.out.println("루나 감소 : " + day);
        		System.out.println("루나 감소량 : " + luna);
        		
        		day -= 1;
        		User user = loginMapper.loginSearch(userId);
    			int money = user.getMoney();
    			money -= luna;
    			loginService.addRuna(userId, money);
    			cartService.order(userId, userId, "", "", "할부금", order_menu, days, -luna);
    			if(day == 0) {
    				timeService.deleteCard(time);
    			} else {
    				String dayss = String.valueOf(day);
    				timeService.updateCard(time, dayss, luna);
    			}		
    		} else {
    			System.out.println("오류");
    		}
    			
    	}
    }
}