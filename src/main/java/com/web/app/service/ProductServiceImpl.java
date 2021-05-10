package com.web.app.service;

import java.util.List;
import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.web.app.entity.Product;
import com.web.app.repository.ProductRepository;
@Service
@Transactional
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductRepository productRepository;
	@Override
	public void saveProductToDB(Product product) {
		this.productRepository.save(product);
	}

	@Override
	public List<Product> getAllActiveProducts() {
		return this.productRepository.findAll();
	}

	@Override
	public Optional<Product> getProductById(Long id) {
		return this.productRepository.findById(id);
	}

	@Override
	public List<Product> getProductByType(String type) {
		return this.productRepository.findByType(type);
	}

	@Override
	public Product getProductByName(String name) {
		String prdName = name;
		return this.productRepository.findByName(prdName);
	}

	@Override
	public void deleteProductById(Long id) {
		this.productRepository.deleteById(id);
		
	}


}
