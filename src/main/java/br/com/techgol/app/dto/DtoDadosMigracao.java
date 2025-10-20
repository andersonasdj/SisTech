package br.com.techgol.app.dto;

public record DtoDadosMigracao(
		Long idSolicitacao,
		Long idCliente,
		String solicitante,
		String afetado) {

}
