package br.com.techgol.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
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

}
