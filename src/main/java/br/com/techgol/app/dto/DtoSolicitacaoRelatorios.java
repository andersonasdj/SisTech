package br.com.techgol.app.dto;

import java.util.List;

public record DtoSolicitacaoRelatorios(
		Long abertosHoje,
		Long finalizadosHoje,
		List<DtoSolicitacoesRelatorioFuncionario> relatorioFuncionario) {

}
