package com.web.app.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.web.app.entity.CartItem;
import com.web.app.entity.Customer;
import com.web.app.entity.Product;
import com.web.app.repository.CartItemRepository;
import com.web.app.repository.ProductRepository;
@Service
@Transactional
public class ShoppingCartServiceImpl implements ShoppingCartService {

	@Autowired
	private CartItemRepository cartItemRepo;
	
	
	
	@Override
	public CartItem addProduct(Product product, Customer customer) {
		CartItem cart = new CartItem();
		cart.setCustomer(customer);
		cart.setProduct(product);
		cart.setQuantity(1);
		cart.setState(false);
		String pattern = "dd-MM-yyyy";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		String date = simpleDateFormat.format(new Date());
		cart.setDate(date);
		
		return this.saveCart(cart);
	}


	@Override
	public List<CartItem> allCarts(Customer customer, Boolean active, String date) {
		if(active) {
			return this.cartItemRepo.findByCustomerAndStateAndDate(customer, active, date);
		}
		else {
			return this.cartItemRepo.findByCustomerAndStateAndDate(customer, active, date);
		}
		
	}
	

	@Override
	public void deleteCartById(Long id) {
		this.cartItemRepo.deleteById(id);
		
	}

	@Override
	public void deleteAllCarts(List<CartItem> carts) {
		this.cartItemRepo.deleteAll(carts);;
		
	}


	@Override
	public Optional<CartItem> getCartByProductAndCustomer(Customer customer, Product product) {
		return this.getCartByProductAndCustomer(customer, product);
	}


	@Override
	public CartItem getCartById(Long cart_id) {
		return this.cartItemRepo.findCartItemById(cart_id);
	}


	@Override
	public CartItem saveCart(CartItem cart) {
		return this.cartItemRepo.save(cart);
		
	}


	@Override
	public void updateQuantity(Integer quantity, Long cart_id) {
		this.cartItemRepo.updateQuantity(quantity, cart_id);
	}


	@Override
	public void confirmOrder(CartItem cart) {
		cart.setState(true);
		this.saveCart(cart);
	}


	@Override
	public List<CartItem> allBills(Customer customer, Boolean state) {
		return this.cartItemRepo.findByCustomerAndState(customer, state);
	}





	





	

		

	

		
		
	

	

}
