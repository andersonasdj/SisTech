package br.com.techgol.app.dto;

public record DtoRendimentosClientes(
		String nomeCliente,
		int qtdFinalizados,
		int qtdAtualizados,
		Long qtdHoras,
		Long tempoContratado,
		int qtdAbertos,
		Long clienteId//,
		//List<DtoHistorico> historico
		) {

}
