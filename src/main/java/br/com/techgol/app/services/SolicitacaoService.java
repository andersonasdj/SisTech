package br.com.techgol.app.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.techgol.app.dto.DtoDadosEdicaoRapida;
import br.com.techgol.app.model.Funcionario;
import br.com.techgol.app.model.Solicitacao;
import br.com.techgol.app.repository.SolicitacaoRepository;
import jakarta.transaction.Transactional;

@Service
public class SolicitacaoService {
	
	@Autowired
	private SolicitacaoRepository repository;
	
	@Autowired
	private FuncionarioService funcionarioService;
	
	@Transactional
	public String exclusaoLogigaSolicitacao(Long id) {
		
		if(repository.existsById(id)) {
			Solicitacao solicitacao = repository.getReferenceById(id);
			solicitacao.setExcluido(true);
			return "Excluido com sucesso!";
		}else {
			return "Não foi possível excluir";
		}
	}
	
	public Solicitacao edicaoRapida(DtoDadosEdicaoRapida dados) {
		
		Solicitacao solicitacao = repository.getReferenceById(dados.id());
		if(dados.funcionario() != null && dados.funcionario() != "") {
			if(funcionarioService.existePorNomeFuncionario(dados.funcionario())) {
				Funcionario funcionario = funcionarioService.buscaPorNome(dados.funcionario());
				solicitacao.setFuncionario(funcionario);
				System.out.println(funcionario);
			}
		}
		solicitacao.setDescricao(dados.descricao());
		solicitacao.setResolucao(dados.resolucao());
		solicitacao.setObservacao(dados.observacao());
		solicitacao.setStatus(dados.status());
		return repository.save(solicitacao);
	}

}
