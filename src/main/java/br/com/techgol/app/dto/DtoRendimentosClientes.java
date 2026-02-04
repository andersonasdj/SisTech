package br.com.techgol.app.dto;

import java.math.BigDecimal;

public record DtoRendimentosClientes(
		String nomeCliente,
		long qtdFinalizados,
		long qtdAtualizados,
		long qtdHoras,
		Long tempoContratado,
		long qtdAbertos,
		Long clienteId,//,
		BigDecimal custoOperacional
		//List<DtoHistorico> historico
		) {


}
