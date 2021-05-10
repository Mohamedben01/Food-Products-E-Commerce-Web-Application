package com.web.app.restController;





import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.web.app.entity.CartItem;
import com.web.app.entity.Customer;
import com.web.app.entity.Product;
import com.web.app.repository.CartItemRepository;
import com.web.app.security.CustomerUserDetails;

import com.web.app.service.ProductService;
import com.web.app.service.ShoppingCartService;

@RestController
@RequestMapping("/api")
public class CustomerRestController {

	@Autowired
	private ShoppingCartService shoppingCartService;
	@Autowired
	private ProductService productService;
	@Autowired
	private CartItemRepository CartRepo;

	 
	@RequestMapping(value = "/updateCustomer", method = RequestMethod.GET, produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
	public ResponseEntity<Customer> findCustomer(@AuthenticationPrincipal CustomerUserDetails customerDetails){
		try {
			Customer customer = customerDetails.getCustomer();
	
			return new ResponseEntity<Customer>(customer, HttpStatus.OK);
		}catch(Exception e) {
			return new ResponseEntity<Customer>(HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(value = "/searchProduct/{productName}", method = RequestMethod.GET, produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
	public ResponseEntity<Product> findProductByName(@PathVariable(name="productName") String name ){
		try {
			Product product = this.productService.getProductByName(name);
			return new ResponseEntity<Product>(product, HttpStatus.OK);
		}catch(Exception e) {
			return new ResponseEntity<Product>(HttpStatus.BAD_REQUEST);
		}
	}
	


	@RequestMapping(value = "/addProductToCart/{id}", method = RequestMethod.GET)
	public int addProductToCart(@AuthenticationPrincipal CustomerUserDetails customerDetails,
			@PathVariable("id") Long product_id) {
		
			Customer customer = customerDetails.getCustomer();
			Product product = this.productService.getProductById(product_id).get();
			this.shoppingCartService.addProduct(product, customer);	
			String pattern = "dd-MM-yyyy";
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
			String date = simpleDateFormat.format(new Date());
			int countItems = this.shoppingCartService.allCarts(customer, false, date).size();
			return countItems;
	}
	
	@GetMapping("/deleteProduct/{id}")
	public void deleteProductById(@PathVariable Long id) {
		
		this.shoppingCartService.deleteCartById(id);
	
	}
	

	@GetMapping(value = "/updateQuantity/{cart_id}/quantity/{quantity}")
	public void updateCart(@PathVariable Long cart_id, @PathVariable Integer quantity) {
		try {
			shoppingCartService.updateQuantity(quantity,cart_id);
			//System.out.println("=======> Good Job you updated successfully.");
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println("=======>" +e.getMessage());
		}
		
	}

	@GetMapping(value="/confirmorder")
	public String confirmation(@AuthenticationPrincipal CustomerUserDetails customerDetails) {
		String pattern = "dd-MM-yyyy";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		String date = simpleDateFormat.format(new Date());
		List<CartItem> carts = this.shoppingCartService.allCarts(customerDetails.getCustomer(), false, date);
		
		for(CartItem crt : carts) {
			this.shoppingCartService.confirmOrder(crt);
		}
		return "your order has confirmed succussfully";
	}
	
	
	@GetMapping(value="/findBill/date/{date}")
	public ResponseEntity<List<CartItem>> getBillWithDate(@AuthenticationPrincipal CustomerUserDetails customerDetails
			, @PathVariable("date") String billDate){
		
		try {
			List<CartItem> excitingBill = this.shoppingCartService.allCarts(customerDetails.getCustomer(), true, billDate);
			return new ResponseEntity<List<CartItem>>(excitingBill,HttpStatus.OK);
		}catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<List<CartItem>>(HttpStatus.BAD_REQUEST);
		}
	}
	
}
