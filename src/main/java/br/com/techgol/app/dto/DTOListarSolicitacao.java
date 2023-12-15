package br.com.techgol.app.dto;

import java.util.Date;

import br.com.techgol.app.model.Solicitacao;

public record DTOListarSolicitacao(
		Long id,
        Date dataAbertura,
        String solicitante,
        String resolucao,
        String descricaoProblema,
        String obs,
        String abriuCHamado
		
		){
	
	DTOListarSolicitacao(Solicitacao s){
		this(s.getId(), s.getDataAbertura(), s.getSolicitante(), s.getResolucao(), s.getDescricao(), s.getObservacao(), s.getAbertoPor());
		
	}

}
