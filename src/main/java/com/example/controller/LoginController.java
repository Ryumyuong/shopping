package com.example.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.domain.User;
import com.example.mapper.LoginMapper;
import com.example.service.CartService;
import com.example.service.LoginService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class LoginController {
	private final LoginService loginService;
	private final CartService cartService;
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
				
				return "redirect:/runa/main?category=";
			} else {
			model.addAttribute("loginErrorMsg", "이미 존재하는 아이디 입니다.");
			return "newLogin";
			}
		}
	}
	
	@GetMapping("/userList")
	public String userList(Model model, @RequestParam int pageNumber, @RequestParam(defaultValue = "10") int pageSize) {
		List<User> userList = loginService.userList(pageNumber, pageSize);
		model.addAttribute("userList", userList);
		model.addAttribute("currentPage", pageNumber);
		for(int i = 1;;i++) {
			userList = loginService.userList(i, 10);
			if(userList.size() < 10) {
				model.addAttribute("totalPages", i);
				break;
			}
		}
		return "userList";
	}
	
	@GetMapping("/updateUser")
	public String updateUser(Model model, @RequestParam String userId) {
		User user = loginMapper.loginSearch(userId);
		model.addAttribute("update", user);
		return "updateUser";
	}

	@PostMapping("/updateUser")
	public String updateUser(Model model, Principal principal, @RequestParam String updateUser, String userId, String password, String username, String phone, String address) {
		User user = loginMapper.loginSearch(updateUser);
		model.addAttribute("update", user);
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
			user = loginMapper.loginSearch(userId);
			if(user == null || updateUser.equals(userId)) {
				String encodePwd = passwordEncoder.encode(password);
				loginService.updateUser(userId, encodePwd, username, phone, address);
				
				return "redirect:/runa/main?category=";
			} else {
			model.addAttribute("loginErrorMsg", "이미 존재하는 아이디 입니다.");
			return "updateUser";
			}
		}
	}
	
	@GetMapping("/delete")
	public String deleteUser(Model model, @RequestParam String userId , @RequestParam int pageNumber) {
		loginService.deleteUser(userId);
		return "redirect:userList?pageNumber=" + pageNumber;
	}
	
	@GetMapping("/addRuna")
	public String addRuna(Model model, @RequestParam int pageNumber, @RequestParam(defaultValue = "10") int pageSize) {
		List<User> userList = loginService.userList(pageNumber, pageSize);
		model.addAttribute("userList", userList);
		model.addAttribute("currentPage", pageNumber);
		for(int i = 1;;i++) {
			userList = loginService.userList(i, 10);
			if(userList.size() < 10) {
				model.addAttribute("totalPages", i);
				break;
			}
		}
		return "addRuna";
	}
	
	@PostMapping("/addRuna")
	public String addRuna(Model model, String userId, String phone, String address, int money, @RequestParam int pageNumber) {
		User user = loginMapper.loginSearch(userId);
		cartService.order(userId, userId, phone, address,"", "루나 추가", money);
		money += user.getMoney();
		System.out.println("더해진 루나 " + money);
		loginService.addRuna(userId, money);
		return "redirect:/addRuna?pageNumber=" + pageNumber;
	}
	
	@PostMapping("/minRuna")
	public String minRuna(Model model, String userId, String phone, String address, int money, @RequestParam int pageNumber) {
		User user = loginMapper.loginSearch(userId);
		cartService.order(userId, userId, phone, address,"", "루나 제거", -money);
		money = user.getMoney() - money;
		if(money < 0) {
			model.addAttribute("ErrorMsg","루나가 부족합니다.");
			return "redirect:/addRuna?pageNumber=" + pageNumber;
		} else {
			System.out.println("줄어든 루나 " + money);
			loginService.addRuna(userId, money);
			return "redirect:/addRuna?pageNumber=" + pageNumber;
		}
	}
}
