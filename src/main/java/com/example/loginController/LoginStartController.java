package com.example.loginController;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class LoginStartController {
	
	@GetMapping("/")
	public String login() {
		return "loginForm";
	}

	@GetMapping("/logout")
	public String logout() {
		return "redirect:/";
	}

}
