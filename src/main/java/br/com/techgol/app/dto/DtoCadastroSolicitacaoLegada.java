package br.com.techgol.app.dto;

import java.util.Date;

public record DtoCadastroSolicitacaoLegada(
		
		String usuario,
        Date dataAbertura,
        String solicitante,
        String resolucao,
        String descricaoProblema,
        String obs,
        String abriuCHamado
		
		) {

}
