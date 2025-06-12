package br.com.techgol.app.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("computador")
public class ComputadorController {
	
	@PreAuthorize("hasRole('ROLE_USER')")
	@GetMapping("/list")
	public String listar() {
		return "computadorList.html";
	}

}
