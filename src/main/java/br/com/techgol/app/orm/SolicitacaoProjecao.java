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
	Long getDuracao();
	@JsonFormat(pattern = "dd/MM/yyyy HH:mm")
	LocalDateTime getDataAtualizacao();
	@JsonFormat(pattern = "dd/MM/yyyy HH:mm")
	LocalDateTime getDataAgendado();
	@JsonFormat(pattern = "dd/MM/yyyy HH:mm")
	LocalDateTime getDataFinalizado();
	String getVersao();
	boolean getVip();
	boolean getRedFlag();
	Long getLog_Id();
	Long getPeso();
	@JsonFormat(pattern = "dd/MM/yyyy HH:mm")
	LocalDateTime getDataAndamento();

}
