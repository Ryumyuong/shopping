package com.example.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.domain.Cart;
import com.example.domain.Product;
import com.example.service.CartService;
import com.example.service.ProductService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class CartController {
	private final CartService cartService;
	private final ProductService productService;
	
	@GetMapping("/cart")
	public String cart(Model model, @RequestParam("userId") String userId) {
		List<Cart> cartList = cartService.product(userId);
		Cart total = cartService.total(userId);
		model.addAttribute("cartItems", cartList);
		model.addAttribute("total",total);
		return "cart";
	}
	
	@PostMapping("/")
	public String addCart(Model model, String userId, String name, String description, int price, String fileName) throws IOException{
        System.out.println("fileName " + fileName);
		cartService.inCart(userId, name, description, price, fileName);
		List<Product> productList = productService.productAll();
		model.addAttribute("rs", productList);
		return "main";
	}
	
	@GetMapping("/cart/delete")
	public String cartDelete(Model model, @RequestParam("userId") String userId, @RequestParam("name") String name) {
		cartService.deleteCart(userId, name);
		List<Cart> cartList = cartService.product(userId);
		Cart total = cartService.total(userId);
		model.addAttribute("cartItems", cartList);
		model.addAttribute("total",total);
		return "cart";
	}
	
	@GetMapping("/cart/deleteAll")
	public String cartDeleteAll(Model model, @RequestParam("userId") String userId) {
		cartService.deleteCartAll(userId);
		List<Product> productList = productService.productAll();
		model.addAttribute("rs", productList);
		return "main";
	}
	
	@GetMapping("/order")
	  public String main(@RequestParam("userId") String userId){
		cartService.order(userId);
		return "orderComplete";
	}

}
