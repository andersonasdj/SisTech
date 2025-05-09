package br.com.techgol.app.dto;

import java.util.List;

public record DtoRendimentosClientes(
		String nomeCliente,
		int qtdFinalizados,
		int qtdAtualizados,
		Long qtdHoras,
		Long tempoContratado,
		int qtdAbertos,
		Long clienteId,
		List<DtoHistorico> historico) {

}
