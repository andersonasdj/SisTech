package br.com.techgol.app.dto;

public record DtoSolicitacoesRelatorioFuncionario(
		String nome,
		Long aberto,
		Long andamento,
		Long agendado,
		Long aguardando,
		Long pausado,
		Long total
		) {

}
