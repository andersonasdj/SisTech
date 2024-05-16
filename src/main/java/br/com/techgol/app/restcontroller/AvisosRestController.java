package br.com.techgol.app.restcontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.techgol.app.dto.DtoAvisoCadastro;
import br.com.techgol.app.dto.DtoAvisoEdicao;
import br.com.techgol.app.dto.DtoAvisoListar;
import br.com.techgol.app.services.AvisosService;

@RestController
@RequestMapping("api/v1/avisos")
public class AvisosRestController {
	
	
	@Autowired
	AvisosService service;
	
	@PostMapping
	public DtoAvisoListar cadastrar(@RequestBody DtoAvisoCadastro dados) {
		if(dados.aviso() != " " || dados.aviso() != null) {
			return service.cadastrar(dados);
		}else {
			throw new RuntimeException("Erro ao cadastrar");
		}
	}
	
	@PutMapping
	public DtoAvisoListar atualizar(@RequestBody DtoAvisoEdicao dados) {
		if(dados.aviso() != " " || dados.aviso() != null) {
			return service.atualizar(dados);
		}else {
			throw new RuntimeException("Erro ao editar o aviso");
		}
	}
	
	@GetMapping
	public List<DtoAvisoListar> listar() {
		return service.listar();
	}
	
	@GetMapping("/{id}")
	public DtoAvisoListar buscaPorId(@PathVariable Long id) {
		return service.buscarPorId(id);
	}
	
	@DeleteMapping("/{id}")
	public String deletar(@PathVariable Long id) {
		return service.deletar(id);
	}

}
