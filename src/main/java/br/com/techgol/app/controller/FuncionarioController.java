package br.com.techgol.app.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("funcionario")
public class FuncionarioController {

	@PreAuthorize("hasRole('ROLE_USER')")
	@GetMapping("/list")
	public String listar() {
		return "funcionarioList.html";
		
	}
	
	@PreAuthorize("hasRole('ROLE_USER')")
	@GetMapping("/form")
	public String formulario() {
		return "funcionarioForm.html";
		
	}
	
	@PreAuthorize("hasRole('ROLE_USER')")
	@GetMapping("/perfil")
	public String perfil() {
		return "perfil.html";
		
	}
	
}
