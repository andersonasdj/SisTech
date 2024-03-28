package br.com.techgol.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import br.com.techgol.app.services.FuncionarioService;

@Controller
public class AppController {
	
	@Autowired
	FuncionarioService service;
	
	@GetMapping("/login")
	public String login() {
		return "templates/login.html";
	}
	
	@GetMapping("/mfa")
	public String mfa() {
		return "templates/mfa.html";
	}
	
	@GetMapping("/create")	//ENDPOINT PARA CRIACAO DO PRIMEIRO USUARIO DO SISTEMA
	public String create() {
		if(service.existeFuncionarios() == 0) {
			return "templates/create.html";
		}else {
			return "/logout";
		}
	}
	
	@GetMapping("/home")
	public String home() {
		return "templates/home.html";
	}
	
	@GetMapping("/sobre")
	public String sobre() {
		return "templates/sobre.html";
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/logAcesso")
	public String logsAcesso() {
		return "templates/logsAcesso.html";
	}

}
