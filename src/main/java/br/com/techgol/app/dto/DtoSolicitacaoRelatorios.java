package br.com.techgol.app.dto;

import java.util.List;

public record DtoSolicitacaoRelatorios(
		Long abertosHoje,
		Long finalizadosHoje,
		Long agendadosHoje,
		Long agendadosAtrasados,
		Long atualizadosHoje,
		List<DtoSolicitacoesRelatorioFuncionario> relatorioFuncionario) {

}
