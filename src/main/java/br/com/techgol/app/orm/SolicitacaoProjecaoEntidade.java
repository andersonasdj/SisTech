package br.com.techgol.app.orm;

public interface SolicitacaoProjecaoEntidade {
	Long getId();
	String getDataAbertura();
	String getDataAndamento();
	String getDataFinalizado();
	String getDataAgendado();
	String getFormaAbertura();
	String getSolicitante();
	String getAfetado();
	String getDescricao();
	String getResolucao();
	String getObservacao();
	String getAbertoPor();
	String getPrioridade();
	String getStatus();
	String getCategoria();
	String getClassificacao();
	String getlocal();
	Long getClienteId();
	Long getFuncionarioId();
	Long duracao();

}
