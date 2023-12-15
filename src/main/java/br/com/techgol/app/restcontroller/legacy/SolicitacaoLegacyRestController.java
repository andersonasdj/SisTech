package br.com.techgol.app.restcontroller.legacy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.techgol.app.dto.legacy.DtoSolicitacaoLegacyListar;
import br.com.techgol.app.repository.legacy.SolicitacaoLegacyRepository;

@RestController
@RequestMapping("solicitacao/legacy")
public class SolicitacaoLegacyRestController {

	@Autowired
	SolicitacaoLegacyRepository repository;
	
	@GetMapping
	public Page<DtoSolicitacaoLegacyListar> listar(Pageable page) {
		
		return repository.findAll(page).map(DtoSolicitacaoLegacyListar::new);
		
	
	}
	
}
