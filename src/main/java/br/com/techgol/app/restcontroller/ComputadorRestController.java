package br.com.techgol.app.restcontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.techgol.app.dto.DtoComputadoresList;
import br.com.techgol.app.repository.ComputadorRepository;

@RestController
@RequestMapping("/api/v1/computador")
public class ComputadorRestController {
	
	@Autowired ComputadorRepository repository;
	
	@GetMapping
	public List<DtoComputadoresList> listarComputadores(){
		
		return repository.listarTodos().stream().map(DtoComputadoresList::new).toList();
	}
	
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

}
