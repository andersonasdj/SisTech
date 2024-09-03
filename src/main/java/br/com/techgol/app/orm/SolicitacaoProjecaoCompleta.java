package br.com.techgol.app.orm;

import java.time.LocalDateTime;

public interface SolicitacaoProjecaoCompleta {
	
	Long getId();
	Long getCliente_id();
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
	String getDuracao();
	String getNomeCliente();
	String getNomeFuncionario();
	LocalDateTime getDataAbertura();
	LocalDateTime getDataAndamento();
	LocalDateTime getDataFinalizado();
	LocalDateTime getDataAtualizacao();

}
