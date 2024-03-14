package br.com.techgol.app.dto;

import java.time.LocalDateTime;

import br.com.techgol.app.model.Solicitacao;

public record DtoListarSolicitacao(
		Long id,
        LocalDateTime dataAbertura,
        String solicitante,
        String resolucao,
        String descricaoProblema,
        String obs,
        String abriuCHamado
		
		){
	
	DtoListarSolicitacao(Solicitacao s){
		this(s.getId(), s.getDataAbertura(), s.getSolicitante(), s.getResolucao(), s.getDescricao(), s.getObservacao(), s.getAbertoPor());
		
	}

}
