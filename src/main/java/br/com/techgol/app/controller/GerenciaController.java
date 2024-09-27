package br.com.techgol.app.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("gerencia")
public class GerenciaController {

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/home")
	public String homeGerencia() {
		return "index.html";
	}
	
}
