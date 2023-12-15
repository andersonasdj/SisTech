package br.com.techgol.app.restcontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.techgol.app.dto.DTOCadastroCliente;
import br.com.techgol.app.model.Cliente;
import br.com.techgol.app.repository.ClienteRepository;

@RestController
@RequestMapping("cliente")
public class ClienteRestController {
	
	@Autowired
	private ClienteRepository repository;
	
	@PostMapping
	public void cadastrar(@RequestBody DTOCadastroCliente dados) {
		
		repository.save(new Cliente(dados));
		
	}

	@GetMapping
	public Page<Cliente> listar(Pageable page){
	
		return repository.findAll(page);
	}
	
	
	
}
