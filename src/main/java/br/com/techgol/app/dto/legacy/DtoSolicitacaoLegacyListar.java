package br.com.techgol.app.dto.legacy;

import java.util.Date;

import br.com.techgol.app.model.legacy.ClienteLecagy;
import br.com.techgol.app.model.legacy.FuncionarioLegacy;
import br.com.techgol.app.model.legacy.SolicitacaoLegacy;

public record DtoSolicitacaoLegacyListar(
		
		Long id,
		String abriuCHamado,
		Date agendado,
		Date dataAbertura,
		String descricaoProblema,
		String formaAbertura,
		String nivelDeIncidencia,
		String obs,
		String onsiteOffsite,
		String prioridade,
		String resolucao,
		String solicitante,
		String status,
		String usuario,
		ClienteLecagy cliente,
		FuncionarioLegacy funcionario) {
	
	public DtoSolicitacaoLegacyListar(SolicitacaoLegacy dado) {
		this(dado.getId(), dado.getAbriuChamado(), dado.getAgendado(), dado.getDataAbertura(), dado.getDescricaoProblema(),
				dado.getFormaAbertura(), dado.getNivelDeIncidencia(), dado.getObs(), dado.getOnsiteOffsite(), dado.getPrioridade(),
				dado.getResolucao(), dado.getSolicitante(), dado.getStatus(), dado.getUsuario(), dado.getCliente(), dado.getFuncionario());
	}


}
