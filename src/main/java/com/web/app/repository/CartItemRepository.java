package com.web.app.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.web.app.entity.CartItem;
import com.web.app.entity.Customer;
import com.web.app.entity.Product;
@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
	
	List<CartItem> findByCustomerAndStateAndDate(Customer customer, Boolean active, String date);
	
	List<CartItem> findByCustomerAndState(Customer customer, Boolean state);
	
	CartItem findCartItemById(Long id);
	
	//void deleteByCustomerAndProduct(Long customer_id, Long product_id);
	
	@Modifying
	@Query("UPDATE CartItem c SET c.quantity = ?1 WHERE c.id = ?2")
	public void updateQuantity(Integer quantity, Long cart_id);
	
	CartItem findCartItemByCustomerAndProduct(Customer customer ,Product product);
	
	CartItem getCartItemByCustomerAndProduct(Customer customer ,Product product);
	

}
