package br.com.techgol.app.restcontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.techgol.app.dto.DtoColaboradorCadastrar;
import br.com.techgol.app.dto.DtoColaboradorListar;
import br.com.techgol.app.orm.ColaboradorProjecao;
import br.com.techgol.app.services.ColaboradorService;

@RestController
@RequestMapping("colaborador")
public class ColaboradorRestController {

	@Autowired
	ColaboradorService service;
	
	@PostMapping
	public void cadastrar(@RequestBody DtoColaboradorCadastrar dados) {
		service.salvar(dados);
	}
	
	@GetMapping
	public List <DtoColaboradorListar> listar() {
		return service.listar();
		
	}
	
	@GetMapping("/list/{id}")
	public List <ColaboradorProjecao> listarPorIdCliente(@PathVariable Long id ) {
		return service.listarPorIdCliente(id);
		
	}
	
}
