package br.com.techgol.app.restcontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.techgol.app.dto.DtoCadastroFuncionario;
import br.com.techgol.app.dto.DtoFuncionarioEdit;
import br.com.techgol.app.dto.DtoListarFuncionarios;
import br.com.techgol.app.services.FuncionarioService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("funcionarios")
public class FuncionarioRestController {
	
	@Autowired
	FuncionarioService service;
	
	@PostMapping
	public void cadastrar(@RequestBody @Valid DtoCadastroFuncionario dados ) {
		service.salvar(dados);
	}
	

	@GetMapping
	public List<DtoListarFuncionarios> listar(){
		return service.listar();
	}
	
	
	@GetMapping("/{id}")
	public DtoFuncionarioEdit editar(@PathVariable Long id ) {
		
		return service.editar(id);
		
	}
	
	@DeleteMapping("/{id}")
	public boolean deletar(@PathVariable Long id ) {
		System.out.println("passei aqui");
		return service.deletar(id);
		
	}
	
	
}
