package com.web.app.repository;





import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.web.app.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
	
	@Query("SELECT c FROM Customer c WHERE c.email = ?1")
	Customer findByEmail(String email);
	
	Customer findCustomerById(Long id);

	Customer findUserByEmailAndPassword(String email, String password);
	
	Customer findByResetPasswordToken(String token);

}
