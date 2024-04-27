package br.com.techgol.app.orm;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

public interface SolicitacaoProjecao {
	
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
	@JsonFormat(pattern = "dd/MM/yyyy HH:mm")
	LocalDateTime getDataAbertura();
	String getDuracao();
	@JsonFormat(pattern = "dd/MM/yyyy HH:mm")
	LocalDateTime getDataAtualizacao();
	@JsonFormat(pattern = "dd/MM/yyyy HH:mm")
	LocalDateTime getDataAgendado();
	String getVersao();
	boolean getVip();
	boolean getRedFlag();
	Long getLog_Id();

}
