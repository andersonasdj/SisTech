package br.com.techgol.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("cliente")
public class ClienteController {
	
	
	@GetMapping
	public String login() {
		return "templates/cliente.html";
	}

}
