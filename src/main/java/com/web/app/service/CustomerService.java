package com.web.app.service;





import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.web.app.entity.Customer;
import com.web.app.entity.CustomerDetail;
import com.web.app.repository.CustomerDetailsRepository;
import com.web.app.repository.CustomerRepository;

@Service
public class CustomerService {
	
	@Autowired
	private CustomerRepository customerRepository;
	
	@Autowired
	private CustomerDetailsRepository custDetailRepo;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	
	@Autowired
	public CustomerService(CustomerRepository customerRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
		this.customerRepository = customerRepository;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}


	public Customer saveCustomer(Customer customer) {
		customer.setPassword(this.bCryptPasswordEncoder.encode(customer.getPassword()));
		return this.customerRepository.save(customer);
	}
	
	public Customer findCustomerByEmail(String email) {
		return this.customerRepository.findByEmail(email);
	}

	
	public void updateCustomer(Customer newCustomer,Customer oldCustomer) {
		
		Customer customer = this.customerRepository.findCustomerById(oldCustomer.getId());
		if(customer != null) {
			customer.setFirstName(newCustomer.getFirstName());
			customer.setLastName(oldCustomer.getFirstName());
			customer.setEmail(oldCustomer.getEmail());
			
			this.customerRepository.save(customer);
		}
	}
	
	public void updateCustomerDetail(CustomerDetail details, Customer oldCustomer) {
		
		CustomerDetail custDetail = this.customerRepository.findCustomerById(oldCustomer.getId()).getCustomerDetail();
		if(custDetail != null) {
			custDetail.setAddress(details.getAddress());
			custDetail.setCity(details.getCity());
			custDetail.setState(details.getState());
			custDetail.setZipCode(details.getZipCode());
			custDetail.setPhone(details.getPhone());
			custDetail.setGender(details.getGender());
			
			this.custDetailRepo.save(custDetail);
		}
	}

	
	


	
	/*For Reset Password */
	public void updateResetPasswordToken(String token, String email) throws CustomerNotFoundException {
		Customer customer = this.customerRepository.findByEmail(email);
		if(customer != null) {
			customer.setResetPasswordToken(token);
			customerRepository.save(customer);
		}else {
			throw new CustomerNotFoundException("Could not find anu customer with email: "+email);
		}
		
	}
	
	public Customer get(String resetPasswordToekn) {
		return customerRepository.findByResetPasswordToken(resetPasswordToekn);
	}
	
	public void updatePassword(Customer customer, String newPassword) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String encoderPassword = passwordEncoder.encode(newPassword);
		
		customer.setPassword(encoderPassword);
		customer.setResetPasswordToken(null);
		
		customerRepository.save(customer);
	}
	
	
	
	
	
	
	
	
	
}
