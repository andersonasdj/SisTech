package br.com.techgol.app.dto;

public record DtoFuncionarioHome(
		String saudacao,
		String nomeFuncionario,
		String dataHoje,
		String dataUltimoLogin,
		Long agendadosAtrasados,
		Long agendadosHoje,
		long avisos,
		Boolean trocaSenha,
		DtoSolicitacoesFuncionario solicitacoes,
		DtoSolicitacoesGerais gerais,
		Boolean refeicao
		) {
}
