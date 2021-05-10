package com.web.app.security;




import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.web.app.entity.Customer;


public class CustomerUserDetails implements UserDetails {


	
	@Autowired
	private Customer customer;
	
	
	public CustomerUserDetails(Customer customer) {
		super();
		this.customer = customer;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return customer.getRoles()
				.stream()
				.map(customer -> new SimpleGrantedAuthority(customer.getRole().toString()))
				.collect(Collectors.toList());
	}

	@Override
	public String getPassword() {
		return customer.getPassword();
	}

	

	@Override
	public String getUsername() {
		return customer.getEmail();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}
