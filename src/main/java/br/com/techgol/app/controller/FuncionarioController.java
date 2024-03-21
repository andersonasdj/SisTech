package br.com.techgol.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("funcionario")
public class FuncionarioController {

	@GetMapping("/list")
	public String listar() {
		return "funcionarioList.html";
		
	}
	
	@GetMapping("/form")
	public String formulario() {
		return "funcionarioForm.html";
		
	}
	
	@GetMapping("/perfil")
	public String perfil() {
		return "perfil.html";
		
	}
	
}
