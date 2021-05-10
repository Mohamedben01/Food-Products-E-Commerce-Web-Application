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

import com.web.app.entity.CartItem;
import com.web.app.entity.Customer;
import com.web.app.security.CustomerUserDetails;
import com.web.app.service.ShoppingCartService;

@Controller
public class BillPaymentController {

	@Autowired
	private ShoppingCartService shoppingCartService;
	/* --------------------------------- */

	@GetMapping("/CashOnDelivery")
	public String cashOnDelivery(Model model, @AuthenticationPrincipal CustomerUserDetails customerDetails) {

		Customer customer = customerDetails.getCustomer();
		String pattern = "dd-MM-yyyy";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		String date = simpleDateFormat.format(new Date());
		List<CartItem> cartPrd = this.shoppingCartService.allCarts(customer, false, date);
		double subtotal = 0;
		for (CartItem l : cartPrd) {
			subtotal += l.getProduct().getPrice() * l.getQuantity();
		}
		model.addAttribute("subtotal", String.format("%.2f", subtotal));
		//model.addAttribute("total", subtotal + 10);
		return "cashOnDelivery";
	}

	@GetMapping("/OnlinePayment")
	public String payOnLine(Model model, @AuthenticationPrincipal CustomerUserDetails customerDetails) {

		Customer customer = customerDetails.getCustomer();
		String pattern = "dd-MM-yyyy";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		String date = simpleDateFormat.format(new Date());
		List<CartItem> cartPrd = this.shoppingCartService.allCarts(customer, false, date);
		double subtotal = 10;
		for (CartItem l : cartPrd) {
			subtotal += l.getProduct().getPrice() * l.getQuantity();
		}
		model.addAttribute("total", String.format("%.2f", subtotal));
		return "onLinePayment";
	}
	
	@GetMapping("/customer/histories")
	public String goHistoryPage(Model model,@AuthenticationPrincipal CustomerUserDetails customerDetails) {
		
		String pattern = "dd-MM-yyyy";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		String date = simpleDateFormat.format(new Date());
		int countItems = this.shoppingCartService.allCarts(customerDetails.getCustomer(),false,date).size();
		model.addAttribute("nbrItems", countItems);
		
		List<String> dateBill = this.shoppingCartService.allBills(customerDetails.getCustomer(), true).stream()
				                      .map(CartItem::getDate)
				                      .distinct()
				                      .collect(Collectors.toList());
		model.addAttribute("dateBills", dateBill);
		return "customerHistoryBill";
	}

}
