package com.example.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.domain.Cart;
import com.example.domain.Product;
import com.example.service.ProductService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MainController {
	private final ProductService productService;
	
	@GetMapping("/")
	  public String main(Model model){
		List<Product> productList = productService.productAll();
		model.addAttribute("rs", productList);
		return "main";
	}
	
	@PostMapping("/")
	public String addCart(Model model, String userId, String name, String fileName) {
		productService.inCart(userId, name, fileName);
		List<Product> productList = productService.productAll();
		model.addAttribute("rs", productList);
		return "main";
	}
	
	@GetMapping("/cart")
	public String cart(Model model, @RequestParam("userId") String userId) {
		List<Cart> cartList = productService.product(userId);
		model.addAttribute("cartItems", cartList);
		return "cart";
	}
}
