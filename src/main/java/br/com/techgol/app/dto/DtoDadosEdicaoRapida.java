package br.com.techgol.app.dto;

import br.com.techgol.app.model.Solicitacao;
import br.com.techgol.app.model.enums.Status;

public record DtoDadosEdicaoRapida(
		
		Long id,
		String descricao,
		String resolucao,
		String observacao,
		Status status) {

	public DtoDadosEdicaoRapida(Solicitacao dados) {
		this(dados.getId(), dados.getDescricao(), dados.getResolucao(), dados.getObservacao(), dados.getStatus());
	}

}
