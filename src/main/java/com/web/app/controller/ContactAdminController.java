package com.web.app.controller;

import java.io.UnsupportedEncodingException;

import java.nio.charset.StandardCharsets;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;



@Controller
public class ContactAdminController {

	@Autowired
	private JavaMailSender mailSender;
	
	@Autowired
	private SpringTemplateEngine tempkateEngine;
	
	@Autowired
	private Environment env;
	
	
	@RequestMapping(value="/sending_message", method=RequestMethod.POST)
	public String sendMessageToAdminUser(Model model,HttpServletRequest request) {
		
		String name = request.getParameter("name");
		String subject = request.getParameter("subject");
		String email = request.getParameter("email");
		System.out.println("======>This is email user: "+email);
		String message = request.getParameter("message");
		try {
			sendMessage(name,email,subject,message);
			System.out.println("===========>Message has sent successflly");
			model.addAttribute("message", "Your message was successflly sent.");
		
	    } catch (UnsupportedEncodingException | MessagingException e) {
		    model.addAttribute("message", "Error while sending email.");
		    e.printStackTrace();
	    }
	   return "redirect:/homehome";
	}
	
	
	public void sendMessage(String name, String subject, String email, String message ) throws UnsupportedEncodingException , MessagingException {
		
		MimeMessage msg = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(msg,MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());
		helper.setFrom(email);
		helper.setTo(env.getProperty("spring.mail.username"));
		helper.setSubject(subject);
		
		Context context = new Context();
		context.setVariable("customerName", name);
		context.setVariable("customerMessage", message);
		
		String content = tempkateEngine.process("sending_message.html", context);
		
		helper.setText(content, true);
		mailSender.send(msg);
	}
}
