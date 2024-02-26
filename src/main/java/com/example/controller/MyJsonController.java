package com.example.controller;

import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.domain.Card;
import com.example.domain.Cart;
import com.example.domain.InCard;
import com.example.domain.InCart;
import com.example.domain.InOrder;
import com.example.domain.Orders;
import com.example.domain.Product;
import com.example.domain.User;
import com.example.domain.Version;
import com.example.mapper.LoginMapper;
import com.example.service.CartService;
import com.example.service.FCMNotificationSender;
import com.example.service.LoginService;
import com.example.service.ProductService;
import com.example.service.TimeService;

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
	
	@Autowired
	TimeService timeService;
	
	
	@GetMapping("api/time")
    public Map<String, String> getCurrentTime() {
		Map<String, String> time = new HashMap<String, String>();
		Instant now = Instant.now();
		ZonedDateTime koreaTime = ZonedDateTime.ofInstant(now, ZoneId.of("Asia/Seoul"));
		time.put("time", koreaTime.toString());
		ZonedDateTime plustime = koreaTime.plusMinutes(1);
		
        return time;
    }
	
	@GetMapping("getCardList")
	public Map<String, List<Card>> getCardList() {
		Map<String, List<Card>> card = new HashMap<String, List<Card>>();
		card.put("card", timeService.getList());
		return card;
	}
	
	@PostMapping("insertCard")
	public ResponseEntity<InCard> insertCard(@RequestHeader("X-CSRF-TOKEN") String csrfToken, @RequestBody InCard inCard) {
		try {
			String name = inCard.getName();
			String days = inCard.getDays();
			int luna = inCard.getLuna();
			
			timeService.insertCard(name, days, luna);
			
			return new ResponseEntity<>(inCard, HttpStatus.OK);
		} catch(Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("getCategory")
	public Map<String, List<Product>> productCategory(String category) {
		Map<String, List<Product>> product = new HashMap<String, List<Product>>();
		product.put("items", productService.productCategory(category));
		System.out.println("Waiting=====getCategory===============" + productService.productCategory(category));
		return product;
	}

	@PostMapping("insert")
	public ResponseEntity<InCart> insertCart(@RequestHeader("X-CSRF-TOKEN") String csrfToken,
			@RequestBody InCart inCart) {
		try {
			String userId = inCart.getUserId();
			String name = inCart.getS_name();
			Integer price = inCart.getS_price();
			String description = inCart.getS_description();
			String fileName = inCart.getFileName();
			System.out.println("insert=====Cartuser===============" + userId);
			System.out.println("insert=====Cartname===============" + name);
			System.out.println("insert=====Cartprice===============" + price);
			cartService.insertCart(userId, name, price, description, fileName);
			if (name.equals("루나몰 웰컴키트")) {
				loginMapper.oneKit(userId);
			}
			return new ResponseEntity<>(inCart, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/csrf")
	public CsrfToken getCsrfToken(CsrfToken csrfToken) {
		System.out.println("토큰 " + csrfToken);
		return csrfToken;
	}

	@GetMapping("/user")
	public Map<String, List<User>> getUser(String userId) {
		Map<String, List<User>> user = new HashMap<String, List<User>>();
		user.put("items", loginService.getUser(userId));
		System.out.println("User=====getUser===============" + loginService.getUser(userId));
		return user;
	}

	@GetMapping("/getCart")
	public Map<String, List<Cart>> getCart(@RequestParam("userId") String userId) {
		Map<String, List<Cart>> cart = new HashMap<String, List<Cart>>();
		cart.put("items", cartService.product(userId));
		System.out.println("Cart=====getCart===============" + cartService.product(userId));
		return cart;
	}

	@GetMapping("/total")
	public Cart total(@RequestParam("userId") String userId) {
		Cart cart = cartService.total(userId);
		System.out.println("total in");
		return cart;
	}

	@GetMapping("orderList")
	public Map<String, List<Orders>> orderList(@RequestParam String userId, @RequestParam int pageNumber,
			@RequestParam(defaultValue = "10") int pageSize) {
		Map<String, List<Orders>> orderList = new HashMap<String, List<Orders>>();
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
	public Map<String, List<Orders>> runaList(@RequestParam String userId, @RequestParam int pageNumber,
			@RequestParam(defaultValue = "10") int pageSize) {
		Map<String, List<Orders>> orderList = new HashMap<String, List<Orders>>();
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

	@PostMapping("notificationToken")
	public ResponseEntity<String> notiToken(@RequestHeader("X-CSRF-TOKEN") String csrfToken,
			@RequestParam("username") String username) {
		try {
			User user = loginMapper.loginSearch(username);
			String code = user.getCode();
			fcmsender.sendPushNotification(code, "루나몰", "주문이 접수되었습니다.");
			User user2 = loginMapper.loginSearch("류지희");
			User user3 = loginMapper.loginSearch("고혜영");
			User user4 = loginMapper.loginSearch("정진경");
			String code2 = user2.getCode();
			String code3 = user3.getCode();
			String code4 = user4.getCode();
			fcmsender.sendPushNotification(code2, "루나몰", username + "님이 물품을 주문하였습니다.");
			fcmsender.sendPushNotification(code3, "루나몰", username + "님이 물품을 주문하였습니다.");
			fcmsender.sendPushNotification(code4, "루나몰", username + "님이 물품을 주문하였습니다.");
			return new ResponseEntity<>(username, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@PostMapping("notificationLunaToken")
	public ResponseEntity<String> notiLunaToken(@RequestHeader("X-CSRF-TOKEN") String csrfToken,
			@RequestParam("username") String username, @RequestParam("luna") String luna) {
		try {
			User user = loginMapper.loginSearch(username);
			String code = user.getCode();
			fcmsender.sendPushNotification(code, "루나몰", luna + " 루나 추가가 신청되었습니다.");
			User user2 = loginMapper.loginSearch("류지희");
			User user3 = loginMapper.loginSearch("고혜영");
			User user4 = loginMapper.loginSearch("정진경");
			String code2 = user2.getCode();
			String code3 = user3.getCode();
			String code4 = user4.getCode();
			fcmsender.sendPushNotification(code2, "루나몰", username + "님이 " + luna + " 루나 추가를 신청하였습니다.");
			fcmsender.sendPushNotification(code3, "루나몰", username + "님이 " + luna + " 루나 추가를 신청하였습니다.");
			fcmsender.sendPushNotification(code4, "루나몰", username + "님이 " + luna + " 루나 추가를 신청하였습니다.");
			return new ResponseEntity<>(username, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@PostMapping("order")
	public ResponseEntity<InOrder> order(@RequestHeader("X-CSRF-TOKEN") String csrfToken,
			@RequestBody InOrder inorder, @RequestParam("days") String days, @RequestParam("luna") int luna) {
		try {
			String userId = inorder.getUserId();
			String phone = inorder.getPhone();
			String address = inorder.getAddress();
			String inquire = inorder.getInquire();
			String time = inorder.getTime();
			Integer total = inorder.getTotal();
			
			List<Cart> cartList = cartService.product(userId);
			
			String order_menu = "";
			
			for (Cart item : cartList) {
				String s_name = item.getS_name();
				int count = item.getCount();

				order_menu += s_name + " " + count + "개\n";
			}
			
			if(days == "일시불") {
				User user = loginMapper.loginSearch(userId);
				int money = user.getMoney();
				money -= total;
				loginService.addRuna(userId, money);				
			} else {
				timeService.insertCard(userId, days, luna);
			}
			cartService.order(userId, userId, phone, address, inquire, order_menu, time, -total);
			cartService.orderCom(userId);
			
			
			
			return new ResponseEntity<>(inorder, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/addLuna")
	public ResponseEntity<User> addRuna(@RequestHeader("X-CSRF-TOKEN") String csrfToken,
			@RequestParam("username") String username, @RequestParam("luna") int luna, @RequestBody User user) {
		try {
			cartService.order(username, username, user.getPhone(), user.getAddress(), "", "루나 추가", "", luna);
			System.out.println("더해진 루나 " + luna);
			int money = user.getMoney() + luna;
			System.out.println("총 루나 " + money);
			loginService.addRuna(username, money);

			String token = user.getCode();
			System.out.println("=" + token + "=");
			fcmsender.sendPushNotification(token, "루나몰", luna + " 루나가 추가되었습니다.");
			return new ResponseEntity<>(user, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/minLuna")
	public ResponseEntity<User> minRuna(@RequestHeader("X-CSRF-TOKEN") String csrfToken,
			@RequestParam("username") String userId, @RequestParam("luna") int luna, @RequestBody User user) {
		try {
			cartService.order(userId, userId, user.getPhone(), user.getAddress(), "", "루나 제거","", -luna);
			System.out.println("줄어든 루나 " + luna);
			int money = user.getMoney() - luna;
			System.out.println("총 루나 " + money);
			loginService.addRuna(userId, money);
			return new ResponseEntity<>(user, HttpStatus.OK);

		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/userList")
	public Map<String, List<User>> userList(@RequestParam int pageNumber,
			@RequestParam(defaultValue = "10") int pageSize) {
		Map<String, List<User>> userList = new HashMap<String, List<User>>();
		userList.put("items", loginService.userList(pageNumber, pageSize));
		for (int i = 1;; i++) {
			List<User> userListAll = loginService.userList(i, 10);
			if (userListAll.size() < 10) {
				break;
			}
		}
		System.out.println("userList : " + userList);
		return userList;
	}

	@PostMapping("/newLogin")
	public ResponseEntity<User> newLogin(@RequestHeader("X-CSRF-TOKEN") String csrfToken, @RequestBody User user) {
		try {
			String userId = user.getUserId();
			String password = user.getPassword();
			String username = user.getUsername();
			String phone = user.getPhone();
			String address = user.getAddress();
			String vip = user.getVip();
			User myuser = loginMapper.loginSearch(userId);
			if (myuser == null) {
				String encodePwd = passwordEncoder.encode(password);
				loginService.newLogin(userId, encodePwd, username, phone, address, vip);

				return new ResponseEntity<>(user, HttpStatus.OK);
			} else {

				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}

		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@PostMapping("insertProduct")
	public ResponseEntity<Product> insertComplete(@RequestHeader("X-CSRF-TOKEN") String csrfToken,
			@RequestBody Product product) throws IOException {
		String category = product.getS_category();
		String name = product.getS_name();
		int price = product.getS_price();
		String description = product.getS_description();
		String fileName = product.getS_fileName();
		boolean isDuplicate = productService.checkDuplicateProductName(name);
		if (isDuplicate) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		} else {
			productService.insertProduct(category, name, price, description, fileName);
			return new ResponseEntity<>(product, HttpStatus.OK);
		}

	}

	@PostMapping("updateProduct")
	public ResponseEntity<Product> updateProduct(@RequestHeader("X-CSRF-TOKEN") String csrfToken, @RequestParam("sname") String name,
			@RequestBody Product product) throws IOException {
		try {
			String category = product.getS_category();
			String sname = product.getS_name();
			int price = product.getS_price();
			String description = product.getS_description();
			String fileName = product.getS_fileName();
			
			productService.updateProduct(category, name, sname, description, price, fileName);
			return new ResponseEntity<>(product, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("deleteProduct")
	public ResponseEntity<String> deleteProduct(@RequestHeader("X-CSRF-TOKEN") String csrfToken,
			@RequestParam("productName") String product) {
		try {
			productService.deleteProduct(product);
			return new ResponseEntity<>(product, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@PostMapping("deliver")
	public ResponseEntity<String> deliver(@RequestHeader("X-CSRF-TOKEN") String csrfToken,
			@RequestParam("username") String username, @RequestParam("deliver") String deliver) {
		try {
			List<User> user = loginMapper.getUser(username);
			String token = user.get(0).getCode();
			cartService.orderCom(username);

			fcmsender.sendPushNotification(token, "루나몰", "배송이 시작되었습니다.");
			return new ResponseEntity<>(username, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@PostMapping("deleteCart")
	public ResponseEntity<String> deleteCart(@RequestHeader("X-CSRF-TOKEN") String csrfToken,
			@RequestParam("username") String username, @RequestParam("itemName") String product) {
		try {
			if (product.equals("루나몰 웰컴키트")) {
				loginMapper.noKit(username);
			}
			cartService.deleteCart(username, product);
			return new ResponseEntity<>(username, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@PostMapping("insertCode")
	public ResponseEntity<String> insertCode(@RequestHeader("X-CSRF-TOKEN") String csrfToken,
			@RequestParam("userId") String username, @RequestParam("code") String code) {
		try {
			loginService.updateCode(username, code);
			return new ResponseEntity<>(username, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
	
	@PostMapping("updateUser")
	public ResponseEntity<String> updateUser(@RequestHeader("X-CSRF-TOKEN") String csrfToken, @RequestBody User user, @RequestParam("userId") String userId) {
		try {
			String username = user.getUserId();
			String password = user.getPassword();
			String phone = user.getPhone();
			String address = user.getAddress();
			String vip = user.getVip();
			
			String encodePwd = passwordEncoder.encode(password);
			
			loginService.updateUser(userId, encodePwd, username, phone, address, vip);
			return new ResponseEntity<>(userId, HttpStatus.OK);
		} catch(Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping("deleteUser")
	public ResponseEntity<String> deleteUser(@RequestHeader("X-CSRF-TOKEN") String csrfToken, @RequestParam("userId") String userId) {
		try {
			loginService.deleteUser(userId);
			return new ResponseEntity<>(userId, HttpStatus.OK);
		} catch(Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping("delivering")
	public ResponseEntity<String> delivering(@RequestHeader("X-CSRF-TOKEN") String csrfToken, @RequestParam("id") int id, @RequestParam("username") String username) {
		try {
			cartService.delivering(id);
			return new ResponseEntity<>(csrfToken, HttpStatus.OK);
		} catch(Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping("deliverCom")
	public ResponseEntity<String> deliverCom(@RequestHeader("X-CSRF-TOKEN") String csrfToken, @RequestParam("id") String id, @RequestParam("username") String username) {
		try {
			List<User> user = loginMapper.getUser(username);
			String token = user.get(0).getCode();
			cartService.orderDeliver(id);
			fcmsender.sendPushNotification(token, "루나몰", "배송이 도착하였습니다.");
			return new ResponseEntity<>(csrfToken, HttpStatus.OK);
		} catch(Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("version")
	public Map<String, String> version() {
		Map<String, String> version = new HashMap<String, String>();
		Version ver = loginMapper.version();
		version.put("version", ver.getVersion());
		
		return version;
	}
}
