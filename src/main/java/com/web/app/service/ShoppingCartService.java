package com.web.app.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.web.app.entity.CartItem;
import com.web.app.entity.Customer;
import com.web.app.entity.Product;


public interface ShoppingCartService {
	
	CartItem addProduct(Product product, Customer customer);
	
	List<CartItem> allCarts(Customer customer, Boolean active, String date);
	
	void deleteCartById(Long id);
	
	void deleteAllCarts(List<CartItem> carts);
	
	Optional<CartItem> getCartByProductAndCustomer(Customer customer,Product product);
	
	
	CartItem getCartById(Long cart_id);

	CartItem saveCart(CartItem cart);
	
	void updateQuantity(Integer quantity, Long cart_id);
	
	void confirmOrder(CartItem cart);
	
	List<CartItem> allBills(Customer customer, Boolean state);
}
