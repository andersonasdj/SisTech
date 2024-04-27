package br.com.techgol.app.restcontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.techgol.app.model.LogSolicitacao;
import br.com.techgol.app.repository.LogSolicitacaoRepository;

@RestController
@RequestMapping("log")
public class LogSolicitacaoRestController {

	@Autowired
	LogSolicitacaoRepository repository;
	
	@GetMapping("{id}")
	public String buscarLog(@PathVariable Long id) {
		
		LogSolicitacao log = repository.getReferenceById(id);
		return log.getLog();
	}
	
}
