package com.example.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.domain.User;
import com.example.mapper.LoginMapper;
import com.example.service.LoginService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class LoginController {
	private final LoginService loginService;

	private final BCryptPasswordEncoder passwordEncoder;
	
	private final LoginMapper loginMapper;

	@GetMapping("/newLogin")
	public String newLoginForm() {
		return "newLogin";
	}

	@PostMapping("/newLogin")
	public String newLogin(Model model, String userId, String password, String username, String phone, String address) {
		if(userId == "") {
			model.addAttribute("loginErrorMsg", "아이디를 입력하세요");
			return "newLogin";
		} else if(password == "") {
			model.addAttribute("loginErrorMsg", "비밀번호를 입력하세요");
			return "newLogin";
		} else if(phone == "") {
			model.addAttribute("loginErrorMsg", "전화번호를 입력하세요");
			return "newLogin";
		} else if(address == "") {
			model.addAttribute("loginErrorMsg", "주소를 입력하세요");
			return "newLogin";
		} else {
			User user = loginMapper.loginSearch(userId);
			if(user == null) {
				String encodePwd = passwordEncoder.encode(password);
				loginService.newLogin(userId, encodePwd, username, phone, address);
				
				return "redirect:/runa/main";
			} else {
			model.addAttribute("loginErrorMsg", "이미 존재하는 아이디 입니다.");
			return "newLogin";
			}
		}
	}
	
	@GetMapping("/userList")
	public String userList(Model model) {
		List<User> userList = loginService.userList();
		model.addAttribute("userList", userList);
		return "userList";
	}
	
	@GetMapping("/updateUser")
	public String updateUser() {
		return "updateUser";
	}

	@PostMapping("/updateUser")
	public String updateUser(Model model, Principal principal, String userId, String password, String username, String phone, String address) {
		if(userId == "") {
			model.addAttribute("loginErrorMsg", "아이디를 입력하세요");
			return "updateUser";
		} else if(password == "") {
			model.addAttribute("loginErrorMsg", "비밀번호를 입력하세요");
			return "updateUser";
		} else if(phone == "") {
			model.addAttribute("loginErrorMsg", "전화번호를 입력하세요");
			return "updateUser";
		} else if(address == "") {
			model.addAttribute("loginErrorMsg", "주소를 입력하세요");
			return "updateUser";
		} else {
			User user = loginMapper.loginSearch(userId);
			if(user == null || principal.getName().equals(userId)) {
				String encodePwd = passwordEncoder.encode(password);
				loginService.updateUser(userId, encodePwd, username, phone, address);
				
				return "redirect:/runa/main";
			} else {
			model.addAttribute("loginErrorMsg", "이미 존재하는 아이디 입니다.");
			return "updateUser";
			}
		}
	}
	
	@GetMapping("/delete")
	public String deleteUser(Model model, @RequestParam String userId) {
		loginService.deleteUser(userId);
		List<User> userList = loginService.userList();
		model.addAttribute("userList", userList);
		return "userList";
	}
	
	@GetMapping("/addRuna")
	public String addRuna(Model model) {
		List<User> userList = loginService.userList();
		model.addAttribute("userList", userList);
		return "addRuna";
	}
	
	@PostMapping("/addRuna")
	public String addRuna(Model model, String userId, int money) {
		User user = loginMapper.loginSearch(userId);
		money += user.getMoney();
		System.out.println("더해진 루나 " + money);
		loginService.addRuna(userId, money);
		List<User> userList = loginService.userList();
		model.addAttribute("userList", userList);
		return "redirect:/addRuna";
	}
}
