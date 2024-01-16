package com.example.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.domain.Cart;
import com.example.domain.InCart;
import com.example.domain.InOrder;
import com.example.domain.Orders;
import com.example.domain.Product;
import com.example.domain.User;
import com.example.mapper.LoginMapper;
import com.example.service.CartService;
import com.example.service.FCMNotificationSender;
import com.example.service.LoginService;
import com.example.service.ProductService;

@RestController
@RequestMapping("/luna/main/")
public class MyJsonController {
	private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	
	@Autowired
	ProductService productService;
	
	@Autowired
	CartService cartService;
	
	@Autowired
	LoginService loginService;
	
	@Autowired
	FCMNotificationSender fcmsender;
	
	@Autowired
	LoginMapper loginMapper;

//	@GetMapping("/getAll")
//	public List<Product> getAll() {
//		System.out.println("getAll : " + productService.MproductAll());
//			return productService.MproductAll();
//	}
	
	@GetMapping("getCategory")
	public Map<String,List<Product>> productCategory(String category) {
		Map<String,List<Product>> product = new HashMap<String,List<Product>>();
		product.put("items", productService.productCategory(category));
		System.out.println("Waiting=====getCategory==============="+productService.productCategory(category));
		return product;
	}
	
	@PostMapping("insert")
	public void insertCart(@RequestBody InCart inCart) {
		String userId = inCart.getUserId();
	    String name = inCart.getS_name();
	    Integer price = inCart.getS_price();
	    String description = inCart.getS_description();
	    String fileName = inCart.getFileName();
		System.out.println("insert=====Cartuser===============" + userId);
		System.out.println("insert=====Cartname===============" + name);
		System.out.println("insert=====Cartprice===============" + price);
		cartService.insertCart(userId, name, price, description, fileName);
		if(name.equals("루나몰 웰컴키트")) {
			loginMapper.oneKit(userId);
		}
	}
	
	@GetMapping("/csrf")
    public CsrfToken getCsrfToken(CsrfToken csrfToken) {
		System.out.println("토큰 " + csrfToken);
        return csrfToken;
    }
	
	@GetMapping("/user")
	public Map<String,List<User>> getUser(String userId) {
		Map<String,List<User>> user = new HashMap<String,List<User>>();
		user.put("items", loginService.getUser(userId));
		System.out.println("User=====getUser==============="+loginService.getUser(userId));
		return user;
	}

	@GetMapping("/getCart")
	public Map<String,List<Cart>> getCart(@RequestParam("userId") String userId) {
		Map<String,List<Cart>> cart = new HashMap<String,List<Cart>>();
		cart.put("items", cartService.product(userId));
		System.out.println("Cart=====getCart==============="+cartService.product(userId));
		return cart;
	}
	
	@GetMapping("/total")
	public void total(@RequestParam("userId") String userId) {
		cartService.total(userId);
	}
	
	@GetMapping("orderList")
	public Map<String,List<Orders>> orderList(@RequestParam String userId, @RequestParam int pageNumber,
		@RequestParam(defaultValue = "10") int pageSize) {
		Map<String,List<Orders>> orderList= new HashMap<String,List<Orders>>();
		if (userId.equals("admin")) {
			orderList.put("items", cartService.orderListAll(pageNumber, pageSize));
			System.out.println(orderList);
			for (Orders order : cartService.orderListAll(pageNumber, pageSize)) {
				String modifiedOrderMenu = order.getOrder_menu().replace("\n", "<br>");
				order.setOrder_menu(modifiedOrderMenu);
			}
			cartService.runaTotalAll();
			for (int i = 1;; i++) {
				List<Orders> orderListAll = cartService.orderListAll(i, 10);
				if (orderListAll.size() < 10) {
					break;
				}
			}
			return orderList;
		} else {
			orderList.put("items", cartService.orderList(userId, pageNumber, pageSize));
			cartService.runaTotal(userId);
			for (int i = 1;; i++) {
				List<Orders> orderListAll = cartService.orderList(userId, i, 10);
				if (orderListAll.size() < 10) {
					break;
				}
			}
			return orderList;
		}
	}
	
	@GetMapping("runaList")
	public Map<String,List<Orders>> runaList(@RequestParam String userId, @RequestParam int pageNumber,
		@RequestParam(defaultValue = "10000") int pageSize) {
		Map<String,List<Orders>> orderList= new HashMap<String,List<Orders>>();
		if (userId.equals("admin")) {
			orderList.put("items", cartService.orderListAll(pageNumber, pageSize));
			System.out.println(orderList);
			for (Orders order : cartService.orderListAll(pageNumber, pageSize)) {
				String modifiedOrderMenu = order.getOrder_menu().replace("\n", "<br>");
				order.setOrder_menu(modifiedOrderMenu);
			}
			cartService.runaTotalAll();
			for (int i = 1;; i++) {
				List<Orders> orderListAll = cartService.orderListAll(i, 10000);
				if (orderListAll.size() < 10000) {
					break;
				}
			}
			return orderList;
		} else {
			orderList.put("items", cartService.orderList(userId, pageNumber, pageSize));
			cartService.runaTotal(userId);
			for (int i = 1;; i++) {
				List<Orders> orderListAll = cartService.orderList(userId, i, 10000);
				if (orderListAll.size() < 10000) {
					break;
				}
			}
			return orderList;
		}
	}
	
	@PostMapping("notificationToken")
	public void notiToken(@RequestParam("notiToken")String notiToken) {
		fcmsender.sendPushNotification(notiToken,"루나몰", "주문이 접수되었습니다.");
		System.out.println("===notiToken===" + notiToken);
	}
	
	@PostMapping("order")
	public void order(@RequestBody InOrder inorder) {
		String userId = inorder.getUserId();
	    String phone = inorder.getPhone();
	    String address = inorder.getAddress();
	    String inquire = inorder.getInquire();
	    Integer total = inorder.getTotal();
		
		List<Cart> cartList = cartService.product(userId);
			User user = loginMapper.loginSearch(userId);
			int money = user.getMoney();
			money -= total;
				loginService.addRuna(userId, money);
				cartList = cartService.product(userId);
				String order_menu = "";
				for (Cart item : cartList) {
					String s_name = item.getS_name();
					int count = item.getCount();

					order_menu += s_name + " " + count + "개\n";
				}
				System.out.println("order_menu " + order_menu);
				cartService.order(userId, userId, phone, address, inquire, order_menu, -total);
				cartService.orderCom(userId);
	}

}

