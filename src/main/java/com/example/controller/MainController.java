package com.example.controller;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.domain.Product;
import com.example.service.ProductService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MainController {
	private final ProductService productService;
	
	@Value("${upload.path}")
	private String uploadPath;
	
	@GetMapping("/")
	public String main(Model model) {
		List<Product> productList = productService.productAll();
		model.addAttribute("rs", productList);
		return "main";
	}
	
	@GetMapping("/update")
	public String update(Model model) {
		List<Product> productList = productService.productAll();
		model.addAttribute("rs", productList);
		return "update";
	}
	
	@GetMapping("/updateProduct")
	public String updateProduct(Model model, @RequestParam String name) {
			Product product = productService.productName(name);
			System.out.println("controller " + product);
			model.addAttribute("p", product);
		return "updateProduct";
	}
	
	@PostMapping("/updateProduct")
	public String updateComplete(Model model, @RequestParam String name, String sname, String description, String price, MultipartFile fileData) throws IOException{
		if(fileData != null) {
			String filePath = uploadPath + "/" + fileData.getOriginalFilename();
	        FileCopyUtils.copy(fileData.getBytes(), new FileOutputStream(filePath));
		}
		byte[] imageData = fileData.getBytes();
		String fileName = Base64.getEncoder().encodeToString(imageData);
		productService.updateProduct(sname, name, description, price, fileName);
		List<Product> productList = productService.productAll();
		model.addAttribute("rs", productList);
		return "main";
	}
	
	@GetMapping("/insertProduct")
	public String insert() {
		return "insertProduct";
	}
	
	@PostMapping("/insertProduct")
	public String insertComplete(Model model, String name, String description, String price, MultipartFile fileData) throws IOException{
		boolean isDuplicate = productService.checkDuplicateProductName(name);
		if(isDuplicate) {
			model.addAttribute("duplicateWarning", true);
			return "insertProduct";
		} else {
		String filePath = uploadPath + "/" + fileData.getOriginalFilename();
        FileCopyUtils.copy(fileData.getBytes(), new FileOutputStream(filePath));
        byte[] imageData = fileData.getBytes();
        String fileName = Base64.getEncoder().encodeToString(imageData);
        productService.insertProduct(name, description, price, fileName);
		List<Product> productList = productService.productAll();
		model.addAttribute("rs", productList);
		return "main";
		}
	}
	
	@GetMapping("/delete")
	public String delete(Model model) {
		List<Product> productList = productService.productAll();
		model.addAttribute("rs", productList);
		return "deleteProduct";
	}
	
	@PostMapping("/delete")
	public String deleteProduct(Model model, @RequestParam String name) {
		productService.deleteProduct(name);
		List<Product> productList = productService.productAll();
		model.addAttribute("rs", productList);
		return "main";
	}
}
