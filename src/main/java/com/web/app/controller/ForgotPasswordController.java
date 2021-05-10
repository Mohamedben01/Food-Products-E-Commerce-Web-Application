package com.web.app.controller;

import java.io.UnsupportedEncodingException;

import java.nio.charset.StandardCharsets;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.repository.query.Param;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import com.web.app.Utility;
import com.web.app.entity.Customer;
import com.web.app.service.CustomerNotFoundException;
import com.web.app.service.CustomerService;

import net.bytebuddy.utility.RandomString;

@Controller
public class ForgotPasswordController {

	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private JavaMailSender mailSender;
	
	@Autowired
	private Environment env; // to get valuse from property file.
	
	@Autowired
    private SpringTemplateEngine templateEngine;
	
	@GetMapping("/forgot_password")
	public String showForgotPassword(Model model) {
		model.addAttribute("pageTitle", "Forgot Password");
		return "forgot_password_form";
	}
	
	@PostMapping("/forgot_password")
	public String processForgotPasswordForm(HttpServletRequest request,Model model) {
		
		String email = request.getParameter("email");
		//System.out.println("=========>Email: "+email);
		String token = RandomString.make(45);
		//System.out.println("=========>Token: "+token);
		try {
			customerService.updateResetPasswordToken(token, email);
			
			//generate reset password link
			String resetPasswordLink = Utility.getSiteUrl(request) + "/reset_password?token=" + token;
			System.out.println("=========>Password link: " +resetPasswordLink);
			model.addAttribute("resetPasswordLink", resetPasswordLink);
			//sending email
			sendEmail(email, resetPasswordLink);
			
			model.addAttribute("message", "We have sent a reset password link to your email.Please check.");
			
		} catch (CustomerNotFoundException e) {
			model.addAttribute("error", e.getMessage());
			e.printStackTrace();
		} catch (UnsupportedEncodingException | MessagingException e) {
			model.addAttribute("error", "Error while sending email.");
		}
		
		return "forgot_password_form";
	}

	private void sendEmail(String email, String resetPasswordLink) throws UnsupportedEncodingException, MessagingException {
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message,MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());
		
		helper.setFrom(env.getProperty("spring.mail.username"),"MedOne Shop Support");
		helper.setTo(email);
		
		String subject = "Here is the link to reset your password";
		
        Context context = new Context();
        context.setVariable("resetPasswordLink", resetPasswordLink);
        //System.out.println("This your link: "+context.getVariable(resetPasswordLink).toString());
		String content = templateEngine.process("forgot_password_email.html", context);
		
        
		helper.setSubject(subject);
		helper.setText(content, true);
		mailSender.send(message);
		
	}
	
	
	@GetMapping("/reset_password")
	public String showResetPasswordForm(@Param(value = "token") String token, Model model) {
		
		Customer customer = customerService.get(token);
		if(customer == null) {
			model.addAttribute("message", "Invalid Token");
			return "reset_password_form";
		}
		
		model.addAttribute("token", token);
		return"reset_password_form";
	}
	
	
	@PostMapping("reset_password")
	public String processResetPassword(HttpServletRequest request, Model model) {
		
		String token = request.getParameter("token");
		String password = request.getParameter("password");
		//String ConfirmPass = request.getParameter("confirmPassword");
		
			Customer customer = customerService.get(token);
			if(customer == null) {
				model.addAttribute("message", "Invalid Token");
			}else {
				customerService.updatePassword(customer, password);
				model.addAttribute("message", "You have successfuly changed your password.");
				
			}
			return "reset_password_form";		
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
