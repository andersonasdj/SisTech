package br.com.techgol.app.dto;

public record DtoFuncionarioHome(
		String saudacao,
		String nomeFuncionario,
		String dataHoje,
		String dataUltimoLogin,
		Long agendadosAtrasados,
		Long agendadosHoje,
		DtoSolicitacoesFuncionario solicitacoes,
		DtoSolicitacoesGerais gerais
		) {
	
}
