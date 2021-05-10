package com.web.app.controller;

import java.io.BufferedOutputStream;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;

import java.util.List;
import java.util.Optional;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;


import com.web.app.entity.Product;
import com.web.app.service.ProductService;

@Controller
public class ProductController {

	@Value("${uploadDir}")
	private String uploadFolder;
	
	@Autowired
	private ProductService productSrvice;
	
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	@GetMapping(value = { "/", "/adminHome"})
	public String addProductPage() {
		return "index";
	}
	
	@PostMapping("/product/saveProduct")
	public @ResponseBody  ResponseEntity<?> createProduct(@RequestParam("name") String name, 
			@RequestParam("price") double price, @RequestParam String product_type,@RequestParam String product_desc, Model model, HttpServletRequest request,
			final @RequestParam("product_image") MultipartFile file)
	{
		try {
			//String uploadDirectory = System.getProperty("user.dir") + uploadFolder;
			String uploadDirectory = request.getServletContext().getRealPath(uploadFolder);
			log.info("uploadDirectory:: " + uploadDirectory);
			String fileName = file.getOriginalFilename();
			String filePath = Paths.get(uploadDirectory, fileName).toString();
			log.info("FileName: " + file.getOriginalFilename());
			if (fileName == null || fileName.contains("..")) {
				model.addAttribute("invalid", "Sorry! Filename contains invalid path sequence \" + fileName");
				return new ResponseEntity<>("Sorry! Filename contains invalid path sequence " + fileName, HttpStatus.BAD_REQUEST);
			}
			String[] names = name.split(",");
			
			log.info("Name: " + names[0]+" "+filePath);
			
			try {
				File dir = new File(uploadDirectory);
				if (!dir.exists()) {
					log.info("Folder Created");
					dir.mkdirs();
				}
				// Save the file locally
				BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(filePath)));
				stream.write(file.getBytes());
				stream.close();
			} catch (Exception e) {
				log.info("in catch");
				e.printStackTrace();
			}
			byte[] productImage = file.getBytes();
			Product product = new Product();
			product.setName(names[0]);
			product.setProduct_image(productImage);
			product.setPrice(price);
			product.setProduct_type(product_type);
			product.setProduct_desc(product_desc);
			this.productSrvice.saveProductToDB(product);
			log.info("HttpStatus===" + new ResponseEntity<>(HttpStatus.OK));
			return new ResponseEntity<>("Product Saved With File - " + fileName, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			log.info("Exception: " + e);
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
			
	}
	
	@GetMapping("/product/display/{id}")
	@ResponseBody
	void showImage(@PathVariable("id") Long id, HttpServletResponse response, Optional<Product> product) throws ServletException, IOException {
		log.info("Id :: " + id);
		product = this.productSrvice.getProductById(id);
		response.setContentType("image/jpeg, image/jpg, image/png, image/gif");
		response.getOutputStream().write(product.get().getProduct_image());
		response.getOutputStream().close();
	}

	@GetMapping("/product/show")
	String show(Model map) {
		List<Product> products = this.productSrvice.getAllActiveProducts();
		map.addAttribute("products", products);
		return "products";
	}
	
	
	
	
	
	
}
