package com.example.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.domain.Cart;
import com.example.domain.Orders;
import com.example.domain.Product;
import com.example.service.CartService;
import com.example.service.ProductService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/cart/")
public class CartController {
	private final CartService cartService;
	private final ProductService productService;
	
	@GetMapping("main")
	public String cart(Model model, @RequestParam("userId") String userId) {
		List<Cart> cartList = cartService.product(userId);
		Cart total = cartService.total(userId);
		model.addAttribute("cartItems", cartList);
		model.addAttribute("total",total);
		return "cart";
	}
	
	@PostMapping("main")
	public String addCart(Model model, String userId, String name, String description, int price, String fileName) throws IOException{
		cartService.inCart(userId, name, description, price, fileName);
		System.out.println("장바구니 등록 " + userId + " " + name);
		List<Product> productList = productService.productAll();
		model.addAttribute("rs", productList);
		return "redirect:/runa/main";
	}
	
	@GetMapping("delete")
	public String cartDelete(Model model, @RequestParam("userId") String userId, @RequestParam("name") String name) {
		cartService.deleteCart(userId, name);
		System.out.println("장바구니 삭제 " + userId + " " + name);
		List<Cart> cartList = cartService.product(userId);
		Cart total = cartService.total(userId);
		model.addAttribute("cartItems", cartList);
		model.addAttribute("total",total);
		return "cart";
	}
	
	@GetMapping("deleteAll")
	public String cartDeleteAll(Model model, @RequestParam("userId") String userId) {
		cartService.deleteCartAll(userId);
		System.out.println("장바구니 전체삭제 " + userId);
		List<Product> productList = productService.productAll();
		model.addAttribute("rs", productList);
		return "main";
	}
	
	@GetMapping("order")
	  public String order(Model model, @RequestParam("userId") String userId){
		List<Cart> cartList = cartService.product(userId);
		model.addAttribute("cartItems", cartList);
		Cart total = cartService.total(userId);
		model.addAttribute("total",total);
		return "order";
	}
	
	@PostMapping("order")
	  public String orderCom(Principal principal, String username, String phone, String address, int total){
		String userId = principal.getName();
		List<Cart> cartList = cartService.product(userId);
		String order_menu = "";
		for (Cart item : cartList) {
	        String s_name = item.getS_name();
	        int count = item.getCount();
	        
	        order_menu += s_name + " " + count + "개 ";
	    }
		System.out.println("order_menu " + order_menu);
		cartService.order(username, userId, phone, address, order_menu, total);
		cartService.orderCom(userId);
		return "orderComplete";
	}
	
	@GetMapping("orderList")
	public String orderList(Model model, @RequestParam String username) {

		if(username.equals("관리자")) {
			List<Orders> orderList = cartService.orderListAll();
			model.addAttribute("orderList", orderList);
			return "orderList";
		} else {
			List<Orders> orderList = cartService.orderList(username);
			model.addAttribute("orderList", orderList);
			return "orderList";
		}
	}
	
	@GetMapping("orderListSearch")
	public String orderListSearch(Model model, @RequestParam String name) {

		if(name == null) {
			List<Orders> orderList = cartService.orderListAll();
			model.addAttribute("orderList", orderList);
			return "orderList";
		} else {
			List<Orders> orderList = cartService.orderList(name);
			model.addAttribute("orderList", orderList);
			return "orderList";
		}
	}

}
