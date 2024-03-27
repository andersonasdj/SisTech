package br.com.techgol.app.orm;

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
	String getDataAbertura();
	String getDuracao();
	String getDataAtualizacao();
	boolean getVip();

}
