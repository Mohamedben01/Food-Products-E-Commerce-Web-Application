package com.web.app.repository;

import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.web.app.entity.Product;
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
	
	@Query("SELECT p FROM Product p WHERE p.product_type = ?1")
	List<Product> findByType(String type);

	@Query("SELECT p FROM Product p WHERE p.name = ?1")
	Product findByName(String name);
	
	

}
