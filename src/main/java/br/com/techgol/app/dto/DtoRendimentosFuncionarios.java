package br.com.techgol.app.dto;

public record DtoRendimentosFuncionarios(
		String nomeFuncionario,
		int qtdFinalizados,
		int qtdAtualizados,
		Long qtdHoras,
		Long qtdHorasReais,
		Long funcionarioId) {

}
