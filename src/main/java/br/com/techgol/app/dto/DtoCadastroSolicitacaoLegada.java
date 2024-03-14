package br.com.techgol.app.dto;

import java.time.LocalDateTime;

public record DtoCadastroSolicitacaoLegada(
		
		String usuario,
        LocalDateTime dataAbertura,
        String solicitante,
        String resolucao,
        String descricaoProblema,
        String obs,
        String abriuCHamado
		
		) {

}
