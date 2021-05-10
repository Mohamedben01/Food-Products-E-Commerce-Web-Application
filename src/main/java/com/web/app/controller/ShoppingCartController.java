package com.web.app.controller;



import java.text.SimpleDateFormat;



import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.web.app.entity.CartItem;
import com.web.app.entity.Customer;

import com.web.app.security.CustomerUserDetails;
import com.web.app.service.ShoppingCartService;

@Controller
public class ShoppingCartController {
	
	@Autowired
	private ShoppingCartService shoppingCartService;
	
	
	
	@RequestMapping(value = "/shoppingCart", method = RequestMethod.GET)
	public String shoppingCartPage(Model model,@AuthenticationPrincipal CustomerUserDetails customerDetails) {
		
		String pattern = "dd-MM-yyyy";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		String date = simpleDateFormat.format(new Date());
		
		Customer customer = customerDetails.getCustomer();
		List<CartItem> cartPrd = this.shoppingCartService.allCarts(customer,false, date);
		
		
	
		/* ---- */
		double subtotal = 0;
		for(CartItem l : cartPrd) {
			subtotal += l.getQuantity() * l.getProduct().getPrice();
		}
		model.addAttribute("Subtotal", subtotal);
		model.addAttribute("cartPrds", cartPrd);
		
		int countItems = this.shoppingCartService.allCarts(customer,false, date).size();
		model.addAttribute("nbrItems", countItems);
		return "customerCartProducts";
	}
	
	@GetMapping("/deleteAllProducts")
	public String deleteAllCarts(@AuthenticationPrincipal CustomerUserDetails customerDetails) {
		Customer customer = customerDetails.getCustomer();
		String pattern = "dd-MM-yyyy";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		String date = simpleDateFormat.format(new Date());
		List<CartItem> carts = this.shoppingCartService.allCarts(customer,false, date);
		this.shoppingCartService.deleteAllCarts(carts);
		return "redirect:/shoppingCart";
	}
	

}
