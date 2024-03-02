package br.com.techgol.app.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AppController {
	
	@GetMapping("/login")
	public String login() {
		return "templates/login.html";
	}
	
	@GetMapping("/home")
	public String home() {
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		System.out.println(authentication);
		
		return "templates/home.html";
	}

}
