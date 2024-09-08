package br.com.techgol.app.restcontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.techgol.app.dto.DtoColaboradorCadastrar;
import br.com.techgol.app.dto.DtoColaboradorEdit;
import br.com.techgol.app.dto.DtoColaboradorListar;
import br.com.techgol.app.orm.ColaboradorProjecao;
import br.com.techgol.app.services.ColaboradorService;

@RestController
@RequestMapping("colaborador")
public class ColaboradorRestController {

	@Autowired
	private ColaboradorService service;
	
	@PreAuthorize("hasRole('ROLE_USER')")
	@GetMapping("/list/{id}") //RETORNA UMA PROJECAO DE COLABORADORES POR ID DE CLIENTE
	public ResponseEntity<List<ColaboradorProjecao>> listarPorIdCliente(@PathVariable Long id ) {
		return ResponseEntity.ok().body(service.listarPorIdCliente(id));
	}
	
	@PreAuthorize("hasRole('ROLE_USER')")
	@GetMapping("/list/cliente/{id}") //RETORNA UMA PROJECAO DE COLABORADORES POR ID DE CLIENTE
	public ResponseEntity<List<String>> listarNomesPorIdCliente(@PathVariable Long id ) {
		return ResponseEntity.ok().body(service.listarNomesIdCliente(id));
	}
	
	@PreAuthorize("hasRole('ROLE_USER')")
	@GetMapping("/list/cliente/{id}/{nomeColaborador}") //RETORNA UMA PROJECAO DE COLABORADORES POR ID DE CLIENTE
	public ResponseEntity<String> listarNomesCelularPorIdCliente(@PathVariable Long id, @PathVariable String nomeColaborador ) {
		return ResponseEntity.ok().body(service.listarCelularColaborador(id, nomeColaborador));
	}
	
	@PreAuthorize("hasRole('ROLE_USER')")
	@GetMapping("/edit/{id}") //RETORNA UMA DTO DE UM COLABORADOR PARA EDIÇÃO
	public ResponseEntity<DtoColaboradorEdit> editar(@PathVariable Long id ) {
		return ResponseEntity.ok().body(service.editaPorIdColaborador(id));
	}
	
	@GetMapping //REVISAR !!!!!
	public ResponseEntity<List<DtoColaboradorListar>> listar() {
		return ResponseEntity.ok().body(service.listar());
	}
	
	@PreAuthorize("hasRole('ROLE_USER')")
	@PostMapping //SALVA UM NOVO COLABORADOR POR ID DE UM CLIENTE
	public String cadastrar(@RequestBody DtoColaboradorCadastrar dados) {
		return service.salvar(dados);
	}
	
	@PreAuthorize("hasRole('ROLE_USER')")
	@PutMapping("/edit") //ATUALIZA UM COLABORADOR
	public ResponseEntity<String> editar(@RequestBody DtoColaboradorEdit dados) {
		return ResponseEntity.ok().body(service.editar(dados));
	}
	
	@PreAuthorize("hasRole('ROLE_EDITOR')")
	@DeleteMapping("/delete/{id}")
	public String excluirColaborador(@PathVariable Long id) {
		
		if(service.existeColaborador(id)) {
			try {
				service.excluirColaborador(id);
				return "Colaborador excluído!";
			} catch (Exception e) {
				return "Erro ao excluir o colaborador!";
			}
		}else {
			return "Colaborador não encontrado";
		}
	}
	
}
