package br.com.techgol.app.dto;

import java.io.Serializable;

import br.com.techgol.app.model.TimeSheet;

public record DtoTimesheetPorIdSolicitacao(
		
		Long id,
		String inicio,
		String fim,
		Long duracao,
		String status,
		Long solicitacaoId
		) implements Serializable{
	
	public DtoTimesheetPorIdSolicitacao(TimeSheet t) {
		this(t.getId(),t.getInicio().toString(), t.getFim().toString(), t.getDuracao(), t.getStatus().toString(), t.getSolicitacao().getId());
	}

}
