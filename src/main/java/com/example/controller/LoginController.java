package com.example.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.domain.User;
import com.example.service.LoginService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class LoginController {
	private final LoginService loginService;
	@GetMapping("/login")
	public String login() {
		return "loginForm";
	}
	
	@PostMapping("/login")
	public String loginComplete(HttpServletRequest request,Model model, String userId, String password) {
		User user = loginService.loginCom(userId, password);
		if(user == null) {
			model.addAttribute("loginErrorMsg", "아이디/비밀번호가 잘못되었습니다.");
			return "loginForm";
		} else {
			HttpSession session = request.getSession();

	        session.setAttribute("userId", userId);
			return "redirect:/";
		}
	}
}
