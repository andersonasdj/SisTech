package br.com.techgol.app.restcontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.techgol.app.dto.DtoAdicionaPesoSolicitacao;
import br.com.techgol.app.dto.DtoPesoSolicitacao;
import br.com.techgol.app.model.PesoSolicitacao;
import br.com.techgol.app.repository.PesoSolicitacoes;
import jakarta.transaction.Transactional;

@RestController
@RequestMapping("api/v1/pesosolicitacoes")
public class PesoSolicitacoesRestController {
	
	@Autowired
	PesoSolicitacoes repository;
	
	
	@GetMapping
	public List<PesoSolicitacao> listarPesosSolicitacoes(){
		
		return repository.findAll();
		
	}
	
	@PutMapping
	@Transactional
	public void salvaPesosSolicitacoes(@RequestBody DtoPesoSolicitacao peso){
		
		PesoSolicitacao pesoSolicitacao =  repository.getReferenceById(peso.id());
		pesoSolicitacao.setPeso(peso.peso());
		pesoSolicitacao.setTipo( (peso.tipo())!= null ? peso.tipo() : pesoSolicitacao.getTipo() );
		
	}
	
	@PostMapping
	@Transactional
	public String adicionaPesosSolicitacoes(@RequestBody DtoAdicionaPesoSolicitacao peso){
		
		repository.save(new PesoSolicitacao(peso));
		return "Adicionado com sucesso";
	}

}
