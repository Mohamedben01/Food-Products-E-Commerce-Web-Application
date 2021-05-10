package com.web.app.controller;


import org.slf4j.Logger;

import org.slf4j.LoggerFactory;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import com.web.app.entity.Customer;
import com.web.app.entity.CustomerDetail;
import com.web.app.entity.Product;
import com.web.app.security.CustomerUserDetails;
import com.web.app.service.CustomerService;
import com.web.app.service.ProductService;
import com.web.app.service.ShoppingCartService;




@Controller
public class CustomerController {
	
	
	@Autowired
	private JavaMailSender mailSender;
	
	@Autowired
	private Environment env;
	

	@Autowired
    private SpringTemplateEngine templateEngine;
	
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Value("${uploadDir}")
	private String uploadFolder;

	@Autowired
	private CustomerService customerService;
	
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private ShoppingCartService shoppingCartService;
	
	
	/*---------------------------------------------*/

	
	@GetMapping("/homehome")
	public String homepage() {
		return "globalHome";
	} 
	
	
	@RequestMapping(value={"/login"}, method = RequestMethod.GET)
    public String login(){
		
		return "login";
    }
	
	@RequestMapping(value="/registration", method = RequestMethod.GET)
    public ModelAndView registration(){
        ModelAndView modelAndView = new ModelAndView();
        Customer customer = new Customer();
        modelAndView.addObject("customer", customer);
        modelAndView.setViewName("registration");
        return modelAndView;
    }
	
	
	@RequestMapping(value = "/registration", method = RequestMethod.POST)
	public ModelAndView saveCustomer(@Valid Customer customer,BindingResult bindingResult,Model model) {
		ModelAndView modelAndView = new ModelAndView();
		Customer customerExists = this.customerService.findCustomerByEmail(customer.getEmail());
		if(customerExists != null) {
			bindingResult.rejectValue("email", "error.customer", "There is already a customer registered with the email provided");
			model.addAttribute("exist", true);
		}
		if(bindingResult.hasErrors()) {
			modelAndView.addObject("registration");
		}
		else {
			CustomerDetail custDetail = new CustomerDetail();
			customer.setCustomerDetail(custDetail);
			this.customerService.saveCustomer(customer);
			modelAndView.addObject("successMessage", "Customer has been registered successfully");
			modelAndView.addObject("customer", new Customer());
            modelAndView.setViewName("registration");
		}
	
		return modelAndView;
	}
	
	
	@GetMapping("/customerHome")
	public String cstpage(Model model,@AuthenticationPrincipal CustomerUserDetails customerDetails,
			@RequestParam(name = "product_type",defaultValue="Vegetable") String type) {
		
		    Customer customer = customerDetails.getCustomer();
		    model.addAttribute("customer", customer);
			List<Product> prdList = this.productService.getProductByType(type);
			model.addAttribute("products", prdList);
			String pattern = "dd-MM-yyyy";
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
			String date = simpleDateFormat.format(new Date());
			int countItems = this.shoppingCartService.allCarts(customer,false,date).size();
			model.addAttribute("nbrItems", countItems);
			
					
			return "/customerHome";
		
		
		
	}
	
	

	
/*-----------------------------------------------------------------------------------------------------*/	
	//Customer's Profile Details
	
	@RequestMapping(value="/profile", method=RequestMethod.GET)
	public String customerProfilePage(Model model, @AuthenticationPrincipal CustomerUserDetails customerDetails) {
		Customer customer = customerDetails.getCustomer();
		model.addAttribute("customer", customer);
		char a = customerDetails.getCustomer().getFirstName().toUpperCase().charAt(0);
		char b = customerDetails.getCustomer().getLastName().toUpperCase().charAt(0);
		model.addAttribute("fisrtChar", a+"."+b);
		
		String pattern = "dd-MM-yyyy";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		String date = simpleDateFormat.format(new Date());
		int countItems = this.shoppingCartService.allCarts(customer,false, date).size();
		model.addAttribute("nbrItems", countItems);
		return "customerProfile";
	}
	
	@RequestMapping(value ="/updateCustomer", method=RequestMethod.POST)
	public String saveCustomers (Model model,HttpServletRequest request,@AuthenticationPrincipal CustomerUserDetails customerDetails) {
		try {
			Customer oldcustomer  = customerDetails.getCustomer();
			oldcustomer.setFirstName(request.getParameter("firstName").trim());
			oldcustomer.setLastName(request.getParameter("lastName").trim());
			oldcustomer.setEmail(request.getParameter("email").trim());
			
			oldcustomer.getCustomerDetail().setGender(request.getParameter("gender").trim());
			oldcustomer.getCustomerDetail().setPhone(request.getParameter("phone").trim());
			oldcustomer.getCustomerDetail().setAddress(request.getParameter("address").trim());
			oldcustomer.getCustomerDetail().setCity(request.getParameter("city").trim());
			oldcustomer.getCustomerDetail().setState(request.getParameter("state").trim());
			oldcustomer.getCustomerDetail().setZipCode(request.getParameter("zipCode").trim());
			
			this.customerService.saveCustomer(oldcustomer);
			model.addAttribute("message", "Customer has successflly updated.");
			
		} catch(Exception e) {
			e.printStackTrace();
			model.addAttribute("message", "Customer has not updated.");
		}
	
		return "redirect:/profile";
		
	}

	
//Update customer's image
@RequestMapping(value = "/updateImage", method = RequestMethod.POST)
public String updateImage(HttpServletRequest request,@AuthenticationPrincipal CustomerUserDetails customerDetails,
		final @RequestParam("customer_image") MultipartFile file) {
	
	try {
	
		String uploadDirectory = request.getServletContext().getRealPath(uploadFolder);
		log.info("uploadDirectory:: " + uploadDirectory);
		String fileName = file.getOriginalFilename();
		String filePath = Paths.get(uploadDirectory, fileName).toString();
		log.info("FileName: " + file.getOriginalFilename());
		if (fileName == null || fileName.contains("..")) {
			System.out.println("Sorry! Filename contains invalid path sequence \" + fileName");
			//return new ResponseEntity<>("Sorry! Filename contains invalid path sequence " + fileName, HttpStatus.BAD_REQUEST);
		}
		
		try {
			File dir = new File(uploadDirectory);
			if (!dir.exists()) {
				log.info("Folder Created");
				dir.mkdirs();
			}
			// Save the file locally
			BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(filePath)));
			stream.write(file.getBytes());
			stream.close();
		} catch (Exception e) {
			log.info("in catch");
			e.printStackTrace();
		}
		 byte[] customerImage = file.getBytes();
	     Customer customer  = customerDetails.getCustomer();
	     customer.getCustomerDetail().setCustomer_image(customerImage);
	     this.customerService.saveCustomer(customer);
	}catch(Exception e) {
		e.printStackTrace();
		log.info("Exception: " + e);
	}
	
	return "redirect:/profile";
	
}


@GetMapping("/customerImage/display")
@ResponseBody
void showCustomerImage(HttpServletResponse response, @AuthenticationPrincipal CustomerUserDetails customerDetails ) throws ServletException, IOException {
	Customer customer = customerDetails.getCustomer();
	response.setContentType("image/jpeg, image/jpg, image/png, image/gif");
	response.getOutputStream().write(customer.getCustomerDetail().getCustomer_image());
	response.getOutputStream().close();
}


//Sending Message to Admin

@PostMapping("/sendMessage")
public String getInTouch(HttpServletRequest request, Model model) {

	String email = request.getParameter("email");
	String name = request.getParameter("name");
	String subject = request.getParameter("subject");
	String msg = request.getParameter("message");
	try {
		System.out.println("=================>Start");
		this.sendEmail(email, name, subject, msg);
		System.out.println("=================>Finish");
	}catch(Exception e) {
		e.printStackTrace();
		System.out.println("=================>Error");
	}
	
	return "redirect:/customerHome";
}
private void sendEmail(String email, String name, String subject, String msg) throws UnsupportedEncodingException, MessagingException {
	MimeMessage message = mailSender.createMimeMessage();
	MimeMessageHelper helper = new MimeMessageHelper(message,MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());
	
	helper.setFrom(email,"Customer "+name);
	helper.setTo("mohamedbendahui7@gmail.com");
	
    Context context = new Context();
    context.setVariable("msg", msg);
    context.setVariable("name", name);
	String content = templateEngine.process("send_email.html", context);
	
 
	helper.setSubject(subject);
	helper.setText(content, true);
	mailSender.send(message);
	
}


}


