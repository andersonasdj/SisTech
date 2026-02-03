package br.com.techgol.app.dto;

import java.math.BigDecimal;

public record DtoRendimentosClientes(
		String nomeCliente,
		int qtdFinalizados,
		int qtdAtualizados,
		Long qtdHoras,
		Long tempoContratado,
		int qtdAbertos,
		Long clienteId,//,
		BigDecimal custoOperacional
		//List<DtoHistorico> historico
		) {

}
