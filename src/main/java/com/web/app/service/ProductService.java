package com.web.app.service;

import java.util.List;
import java.util.Optional;

import com.web.app.entity.Product;

public interface ProductService {

	void saveProductToDB(Product product);
	
	List<Product> getAllActiveProducts();
	
	Optional<Product> getProductById(Long id);
	
	Product getProductByName(String name);
	
	List<Product> getProductByType(String type);
	
	void deleteProductById(Long id);
	
	
}
