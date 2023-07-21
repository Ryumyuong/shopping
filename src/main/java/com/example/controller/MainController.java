package com.example.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.domain.Product;
import com.example.service.ProductService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/runa/")
public class MainController {
	private final ProductService productService;

	@Value("${upload.path}")
	private String uploadPath;

	@GetMapping("main")
	public String main(Model model, @RequestParam String category) {
		if (category == "") {
			category = "전자";
			List<Product> productListCategory = productService.productCategory(category);
			model.addAttribute("rs", productListCategory);
			return "main";
		} else {
			List<Product> productListCategory = productService.productCategory(category);
			model.addAttribute("rs", productListCategory);
			return "main";
		}
	}

	@GetMapping("update")
	public String update(Model model, @RequestParam String category) {
		List<Product> productListCategory = productService.productCategory(category);
		model.addAttribute("rs", productListCategory);
		return "update";
	}

	@GetMapping("updateProduct")
	public String updateProduct(Model model, @RequestParam String name) {
		Product product = productService.productName(name);
		model.addAttribute("p", product);
		return "updateProduct";
	}

	@PostMapping("updateProduct")
	public String updateComplete(Model model, String category, String name, String description, int price,
			MultipartFile fileData) throws IOException {
		if (price == 0) {
			model.addAttribute("loginErrorMsg", "가격을 입력하세요");
			Product product = productService.productName(name);
			model.addAttribute("p", product);
			return "updateProduct";
		} else if(fileData.getOriginalFilename().equals(" ")){
			System.out.println("file1 " + fileData.getOriginalFilename());
			String filePath = uploadPath + "/" + fileData.getOriginalFilename();
			File dir = new File(uploadPath);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			FileCopyUtils.copy(fileData.getBytes(), new FileOutputStream(filePath));
			byte[] imageData = fileData.getBytes();
			String fileName = Base64.getEncoder().encodeToString(imageData);
			productService.updateProduct(category, name, description, price, fileName);
			List<Product> productList = productService.productAll();
			model.addAttribute("rs", productList);
			return "redirect:update?category=" + URLEncoder.encode(category, StandardCharsets.UTF_8);

		} else {
			System.out.println("file2 " + fileData.getOriginalFilename());
			productService.updateProduct2(category, name, description, price);
			List<Product> productList = productService.productAll();
			model.addAttribute("rs", productList);
			return "redirect:update?category=" + URLEncoder.encode(category, StandardCharsets.UTF_8);			
		}
	}

	@GetMapping("insertProduct")
	public String insert() {
		return "insertProduct";
	}

	@PostMapping("insertProduct")
	public String insertComplete(Model model, @RequestParam String category, String name, String description, int price,
			MultipartFile fileData) throws IOException {
		if (name == "") {
			model.addAttribute("loginErrorMsg", "제품명을 입력하세요");
			return "insertProduct";
		} else if (price == 0) {
			model.addAttribute("loginErrorMsg", "가격을 입력하세요");
			return "insertProduct";
		} else if (category == "") {
			model.addAttribute("loginErrorMsg", "종류를 입력하세요");
			return "insertProduct";
		} else {
			boolean isDuplicate = productService.checkDuplicateProductName(name);
			if (isDuplicate) {
				model.addAttribute("duplicateWarning", true);
				return "insertProduct";
			} else {
				String filePath = uploadPath + "/" + fileData.getOriginalFilename();
				FileCopyUtils.copy(fileData.getBytes(), new FileOutputStream(filePath));
				byte[] imageData = fileData.getBytes();
				String fileName = Base64.getEncoder().encodeToString(imageData);
				productService.insertProduct(category, name, price, description, fileName);
				List<Product> productList = productService.productAll();
				model.addAttribute("rs", productList);
				return "redirect:main?category=" + URLEncoder.encode(category, StandardCharsets.UTF_8);
			}
		}
	}

	@GetMapping("delete")
	public String delete(Model model, @RequestParam String category) {
		List<Product> productListCategory = productService.productCategory(category);
		model.addAttribute("rs", productListCategory);
		return "deleteProduct";
	}

	@PostMapping("delete")
	public String deleteProduct(Model model, @RequestParam String name, @RequestParam String category) {
		productService.deleteProduct(name);
		List<Product> productList = productService.productAll();
		model.addAttribute("rs", productList);
		return "redirect:delete?category=" + URLEncoder.encode(category, StandardCharsets.UTF_8);
	}
}
