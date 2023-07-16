package com.example.loginController;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.service.LoginService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class LoginController {
	private final LoginService loginService;

	private final BCryptPasswordEncoder passwordEncoder;

	@GetMapping("/")
	public String login() {
		return "loginForm";
	}

	@GetMapping("/logout")
	public String logout() {
		return "redirect:/";
	}

	@GetMapping("/newLogin")
	public String newLoginForm() {
		return "newLogin";
	}

	@PostMapping("/newLogin")
	public String newLogin(String userId, String password, String username, String phone, String address) {
		String encodePwd = passwordEncoder.encode(password);
		loginService.newLogin(userId, encodePwd, username, phone, address);
		
		return "redirect:/";
	}
}
