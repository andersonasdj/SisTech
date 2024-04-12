package br.com.techgol.app.orm;

import java.time.LocalDateTime;

public interface SolicitacaoProjecaoEntidadeComAtributos {
	Long getId();
	String getAbertoPor();
	String getAfetado();
	String getCategoria();
	String getClassificacao();
	String getDescricao();
	String getFormaAbertura();
	String getLocal();
	String getObservacao();
	String getPrioridade();
	String getResolucao();
	String getSolicitante();
	String getStatus();
	String getNomeCliente();
	String getNomeFuncionario();
	String getDuracao();
	LocalDateTime getDataAbertura();
	LocalDateTime getDataAtualizacao();
	LocalDateTime getDataAgendado();
	boolean getVip();
	boolean getRedFlag();
}

