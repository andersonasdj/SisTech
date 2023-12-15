package br.com.techgol.app.orm;

import java.util.Date;

public interface SolicitacaoLegacyProjecao {
	
	Long getId();
	String getAbriuCHamado();
	Date getAgendado();
	Date getDataAbertura();
	String getDescricaoProblema();
	String getFormaAbertura();
	String getNivelDeIncidencia();
	String getObs();
	String getOnsiteOffsite();
	String getPrioridade();
	String getResolucao();
	String getSolicitante();
	String getStatus();
	String getUsuario();
	String getNome();
	
}
