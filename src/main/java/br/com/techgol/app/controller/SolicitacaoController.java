package br.com.techgol.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("solicitacao")
public class SolicitacaoController {

	@GetMapping("/form")
	public String formulario() {
		
		return "solicitacaoForm.html";
	}
	
	@GetMapping("/list")
	public String listar() {
		
		return "solicitacaoList.html";
	}
	
	
	@GetMapping("/legacy")
	public String legadas() {
		
		return "solicitacaoLegacyList.html";
	}
	
	@GetMapping("/dashboard/geral")
	public String dashboardAtivos() {
		
		return "dashboard.html";
	}
	

}
