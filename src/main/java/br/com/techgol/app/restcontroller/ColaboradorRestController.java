package br.com.techgol.app.restcontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
	ColaboradorService service;
	
	@GetMapping("/list/{id}") //RETORNA UMA PROJECAO DE COLABORADORES POR ID DE CLIENTE
	public ResponseEntity<List<ColaboradorProjecao>> listarPorIdCliente(@PathVariable Long id ) {
		return ResponseEntity.ok().body(service.listarPorIdCliente(id));
	}
	
	@GetMapping("/edit/{id}") //RETORNA UMA DTO DE UM COLABORADOR PARA EDIÇÃO
	public ResponseEntity<DtoColaboradorEdit> editar(@PathVariable Long id ) {
		return ResponseEntity.ok().body(service.editaPorIdColaborador(id));
	}
	
	@GetMapping //REVISAR !!!!!
	public ResponseEntity<List<DtoColaboradorListar>> listar() {
		return ResponseEntity.ok().body(service.listar());
	}
	
	@PostMapping //SALVA UM NOVO COLABORADOR POR ID DE UM CLIENTE
	public void cadastrar(@RequestBody DtoColaboradorCadastrar dados) {
		service.salvar(dados);
	}
	
	@PutMapping("/edit") //ATUALIZA UM COLABORADOR
	public ResponseEntity<String> editar(@RequestBody DtoColaboradorEdit dados) {
		return ResponseEntity.ok().body(service.editar(dados));
	}
	
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
