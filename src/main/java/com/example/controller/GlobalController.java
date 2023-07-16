package com.example.controller;

import java.security.Principal;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.example.domain.User;
import com.example.mapper.LoginMapper;

@ControllerAdvice(basePackages = {"com.example.controller"})
public class GlobalController {
	@Autowired
	private LoginMapper loginMapper;

    @ModelAttribute
    public void addSessionAttributes(Model model, Principal principal, HttpSession session) {
    	User user = loginMapper.loginSearch(principal.getName());

        if (user != null) {
        	session.setAttribute("user", user);
            model.addAttribute("user", user);
        }
    }
}
