package com.web.app.security;







import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;


import com.web.app.entity.Customer;
import com.web.app.repository.CustomerRepository;

public class CustomerUserDetailsService implements UserDetailsService {

	@Autowired
	private CustomerRepository customerRepository;
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Customer customer = customerRepository.findByEmail(email);
		if(customer == null) {
			throw new UsernameNotFoundException("Customer not found !!!");
		}
		return new CustomerUserDetails(customer);
	}
	
	
	
}
