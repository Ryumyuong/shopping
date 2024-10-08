package com.example.controller;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.domain.Cart;
import com.example.domain.Orders;
import com.example.domain.User;
import com.example.mapper.LoginMapper;
import com.example.service.CartService;
import com.example.service.FCMNotificationSender;
import com.example.service.LoginService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/cart/")
public class CartController {
	private final CartService cartService;
	private final LoginMapper loginMapper;
	private final LoginService loginService;
	
	@Autowired
	FCMNotificationSender fcmsender;

	@GetMapping("main")
	public String cart(Model model, @RequestParam("userId") String userId) {
		List<Cart> cartList = cartService.product(userId);
		Cart total = cartService.total(userId);
		model.addAttribute("cartItems", cartList);
		model.addAttribute("total", total);
		return "cart";
	}

	@PostMapping("main")
	public String addCart(Model model, @RequestParam String category, String userId, String name, String description,
			int price, String fileName, int count) throws IOException {
		for (int i = 0; i < count; i++) {
			cartService.inCart(userId, name, description, price, fileName);
			if(name.equals("루나몰 웰컴키트")) {
				loginMapper.oneKit(userId);
			}
		}
		return "redirect:/runa/main?category=" + URLEncoder.encode(category, StandardCharsets.UTF_8);
	}

	@GetMapping("delete")
	public String cartDelete(Model model, @RequestParam("userId") String userId, @RequestParam("name") String name) {
		if(name.equals("루나몰 웰컴키트")) {
			loginMapper.noKit(userId);
		}
		cartService.deleteCart(userId, name);
		System.out.println("장바구니 삭제 " + userId + " " + name);
		return "redirect:main?userId=" + URLEncoder.encode(userId, StandardCharsets.UTF_8);
	}

	@GetMapping("deleteAll")
	public String cartDeleteAll(Model model, @RequestParam("userId") String userId) {
		List<Cart> cartList = cartService.product(userId);
		for(int i=0;i<cartList.size();i++) {
			if(cartList.get(i).getS_name().equals("루나몰 웰컴키트")) {
				loginMapper.noKit(userId);
			}
		}
		cartService.deleteCartAll(userId);
		System.out.println("장바구니 전체삭제 " + userId);
		return "redirect:/runa/main?category=";
	}

	@GetMapping("order")
	public String order(Model model, @RequestParam("userId") String userId) {
		List<Cart> cartList = cartService.product(userId);
		model.addAttribute("cartItems", cartList);
		Cart total = cartService.total(userId);
		if (total == null) {
			model.addAttribute("cartItems", cartList);
			model.addAttribute("total", total);
			model.addAttribute("loginErrorMsg", "물품을 정하세요");
			return "cart";
		}
		model.addAttribute("total", total);
		return "order";
	}

	@PostMapping("order")
	public String orderCom(Model model, Principal principal, String username, String phone, String address, String inquire, int total) {
		Cart totals = cartService.total(principal.getName());
		model.addAttribute("total", totals);
		List<Cart> cartList = cartService.product(principal.getName());
		model.addAttribute("cartItems", cartList);
		if (username == "") {
			model.addAttribute("loginErrorMsg", "받는사람을 입력하세요");
			return "order";
		} else if (phone == "") {
			model.addAttribute("loginErrorMsg", "전화번호를 입력하세요");
			return "order";
		} else if (address == "") {
			model.addAttribute("loginErrorMsg", "주소를 입력하세요");
			return "order";
		} else {
			String userId = principal.getName();
			User user = loginMapper.loginSearch(userId);
			int money = user.getMoney();
			money -= total;
			if (money < 0) {
				cartList = cartService.product(userId);
				model.addAttribute("cartItems", cartList);
				model.addAttribute("loginErrorMsg", "루나가 부족합니다.");
				return "order";
			} else {
				loginService.addRuna(userId, money);
				cartList = cartService.product(userId);
				String order_menu = "";
				for (Cart item : cartList) {
					String s_name = item.getS_name();
					int count = item.getCount();

					order_menu += s_name + " " + count + "개\n";
				}
				System.out.println("order_menu " + order_menu);
				cartService.order(username, userId, phone, address, inquire, order_menu,"", -total);
				cartService.orderCom(userId);
				return "orderComplete";
			}
		}
	}

	@GetMapping("runaList")
	public String runaList(Model model, @RequestParam String userId, @RequestParam int pageNumber,
			@RequestParam(defaultValue = "10") int pageSize) {
		if (userId.equals("admin")) {
			List<Orders> orderList = cartService.orderListAll(pageNumber, pageSize);
			System.out.println(orderList);
			for (Orders order : orderList) {
				String modifiedOrderMenu = order.getOrder_menu().replace("\n", "<br>");
				order.setOrder_menu(modifiedOrderMenu);
			}
			int total = cartService.runaTotalAll();
			model.addAttribute("total", -total);
			model.addAttribute("orderList", orderList);
			model.addAttribute("currentPage", pageNumber);
			for (int i = 1;; i++) {
				orderList = cartService.orderListAll(i, 10);
				if (orderList.size() < 10) {
					model.addAttribute("totalPages", i);
					break;
				}
			}
			return "runaList";
		} else {
			List<Orders> orderList = cartService.orderList(userId, pageNumber, pageSize);
			int total = cartService.runaTotal(userId);
			model.addAttribute("total", total);
			model.addAttribute("orderList", orderList);
			model.addAttribute("currentPage", pageNumber);
			for (int i = 1;; i++) {
				orderList = cartService.orderList(userId, i, 10);
				if (orderList.size() < 10) {
					model.addAttribute("totalPages", i);
					break;
				}
			}
			return "runaList";
		}
	}

	@PostMapping("runaListSearch")
	public String runaListSearch(String userId, @RequestParam int pageNumber) {

		if (userId == "") {
			return "redirect:runaList?userId=admin&pageNumber=" + pageNumber;
		} else {
			return "redirect:runaList?userId=" + URLEncoder.encode(userId, StandardCharsets.UTF_8) + "&pageNumber="
					+ pageNumber;
		}
	}

	@GetMapping("runaDeliver")
	public String runaDeliver(@RequestParam String id, @RequestParam int pageNumber) {
		cartService.orderDeliver(id);
		return "redirect:runaList?userId=admin&pageNumber=" + pageNumber;
	}

	@GetMapping("runaUnDeliver")
	public String runaUnDeliver(@RequestParam String id, @RequestParam int pageNumber, @RequestParam String userId,
			@RequestParam int money, @RequestParam String menu) {
		if(menu.contains("루나몰 웰컴키트")) {
			loginMapper.noKit(userId);
		}
		
		cartService.orderUnDeliver(id);
		User user = loginMapper.loginSearch(userId);
		System.out.println(money + " " + user.getMoney());
		money -= user.getMoney();
		loginService.addRuna(userId, -money);
		return "redirect:runaList?userId=admin&pageNumber=" + pageNumber;
	}

}
