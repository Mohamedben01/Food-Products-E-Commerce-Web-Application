package com.web.app.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@Table(name = "customer_detail")
public class CustomerDetail {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "customerdetail_id", nullable = false, unique = true)
	private Long customerdetailid;
	
	@Column(name = "phone")
	private String phone;
	@Column(name = "address")
	private String address;
	@Column(name = "city")
	private String city;
	@Column(name = "state")
	private String state;
	@Column(name = "zip_code")
	private String zipCode;
	@Column(name = "gender")
	private String gender;
	
	@Lob
	@Column(name = "customer_img", length = Integer.MAX_VALUE, nullable = true)
	private byte[] customer_image;
	
	public CustomerDetail() {
		super();
	}

	public Long getCustomerdetailid() {
		return customerdetailid;
	}

	public void setCustomerdetailid(Long customerdetailid) {
		this.customerdetailid = customerdetailid;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public byte[] getCustomer_image() {
		return customer_image;
	}

	public void setCustomer_image(byte[] customer_image) {
		this.customer_image = customer_image;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
	
	
	

}
