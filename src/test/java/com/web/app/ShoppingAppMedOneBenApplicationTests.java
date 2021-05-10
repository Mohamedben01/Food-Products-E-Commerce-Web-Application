package com.web.app;

import static org.junit.jupiter.api.Assertions.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;



import com.web.app.service.ShoppingCartService;

@SpringBootTest
@AutoConfigureMockMvc
class ShoppingAppMedOneBenApplicationTests {

	@Autowired
	private ShoppingCartService cartSer;


	@Autowired
	private MockMvc mockMvc;
	

	@Test
	public void updateQuantityTest() {
		this.cartSer.updateQuantity(3, (long) 2);
		assertNotNull(cartSer.getCartById((long) 2));
	}
	
	
	@Test
	public void testUpdateQuantity() throws Exception {
		Long cart_id = (long) 2;
		Integer quantity = 1;
		String url = "/api/updateQuantity/" + cart_id + "/quantity/" + quantity;
		
		this.mockMvc.perform(get(url)).andExpect(status().isOk());
	}
	
	
	
}
