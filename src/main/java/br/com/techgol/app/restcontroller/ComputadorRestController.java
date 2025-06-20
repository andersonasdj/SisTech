package br.com.techgol.app.restcontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.techgol.app.dto.DtoComputadorResumido;
import br.com.techgol.app.dto.DtoComputadoresList;
import br.com.techgol.app.repository.ComputadorRepository;
import br.com.techgol.app.services.ComputadorService;

@RestController
@RequestMapping("/api/v1/computador")
public class ComputadorRestController {
	
	@Autowired ComputadorRepository repository;
	@Autowired ComputadorService service;
	
	@PreAuthorize("hasRole('ROLE_USER')")
	@GetMapping
	public List<DtoComputadoresList> listarComputadores(){
		
		return repository.listarTodos().stream().map(DtoComputadoresList::new).toList();
	}
	
	@PreAuthorize("hasRole('ROLE_USER')")
	@PutMapping
	public String atualizarComputador(@RequestBody DtoComputadorResumido dto) {
		return service.atualizarComputador(dto);
	}
	
	@PreAuthorize("hasRole('ROLE_USER')")
	@DeleteMapping("/{id}")
	public String deletarComputador(@PathVariable Long id){
		if(repository.existsById(id)) {
			try {
				repository.deleteById(id);
				return "Computador excluído!";
			} catch (Exception e) {
				return "Erro ao excluir o Computador!";
			}
		}else {
			return "Computador não encontrado";
		}
	}

	@PreAuthorize("hasRole('ROLE_USER')")
	@GetMapping("/cliente/{id}")
	public List<DtoComputadoresList> listarComputadoresCliente(@PathVariable Long id){
		
		return repository.listarTodosCliente(id).stream().map(DtoComputadoresList::new).toList();
	}
	
	@PreAuthorize("hasRole('ROLE_USER')")
	@GetMapping("/{id}")
	public DtoComputadorResumido buscarComputador(@PathVariable Long id){
		
		return new DtoComputadorResumido(repository.getReferenceById(id));
	}

}
