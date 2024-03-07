package br.com.techgol.app.dto;

public record DtoFuncionarioHome(
		String saudacao,
		String nomeFuncionario,
		String dataHoje,
		DtoSolicitacoesFuncionario solicitacoes,
		DtoSolicitacoesGerais gerais
		) {
	
}
