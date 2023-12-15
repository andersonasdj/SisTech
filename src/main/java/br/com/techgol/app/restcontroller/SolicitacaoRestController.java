package br.com.techgol.app.restcontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.techgol.app.dto.DTOCadastroSolicitacao;
import br.com.techgol.app.model.Solicitacao;
import br.com.techgol.app.repository.SolicitacaoRepository;

@RestController
@RequestMapping("solicitacao")
public class SolicitacaoRestController {
	
	@Autowired
	private SolicitacaoRepository repository;
	
	@GetMapping
	private List<Solicitacao> listar(){
		
		return repository.findAll();
		
	}
	
	@PostMapping
	public void cadastrar(@RequestBody List<DTOCadastroSolicitacao> dados) {
		
		dados.forEach(s -> repository.save(new Solicitacao(s)));
		
	}

	
}
