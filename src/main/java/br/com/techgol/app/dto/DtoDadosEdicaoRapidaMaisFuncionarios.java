package br.com.techgol.app.dto;

import java.util.List;

import br.com.techgol.app.model.Solicitacao;
import br.com.techgol.app.model.enums.Status;

public record DtoDadosEdicaoRapidaMaisFuncionarios(
		
		
		Long id,
		String descricao,
		String resolucao,
		String observacao,
		Status status,
		String funcionario,
		List<String> funcionarios) {

	public DtoDadosEdicaoRapidaMaisFuncionarios(Solicitacao dados, List<String> s) {
		this(dados.getId(), dados.getDescricao(), dados.getResolucao(), dados.getObservacao(), dados.getStatus(), dados.getFuncionario().getNomeFuncionario(), s);
	}
		
		
		
		
		
		
		
		
		

}
