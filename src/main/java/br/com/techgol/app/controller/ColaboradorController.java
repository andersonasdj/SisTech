package br.com.techgol.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("colaborador")
public class ColaboradorController {
	
	@GetMapping("/form")
	private String formulario() {
		return "colaboradorForm.html";
	}

}
