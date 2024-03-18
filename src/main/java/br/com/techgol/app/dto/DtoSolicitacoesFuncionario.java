package br.com.techgol.app.dto;

public record DtoSolicitacoesFuncionario(
		int abertas,
		int andamento,
		int agendados,
		int aguardando,
		int pausado,
		int total) {

}
