package br.com.techgol.app.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "log_solicitacoes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LogSolicitacao {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Lob
	@Column(name = "log", columnDefinition="BLOB")
	private String log;
	
	public LogSolicitacao(Solicitacao solicitacao) {
		String log = " * Data Abertura: " + solicitacao.getDataAbertura() + "\n "
				+ " * Aberto por: " + solicitacao.getAbertoPor() + "\n "
				+ " * Forma de abertura: " + solicitacao.getFormaAbertura() + "\n "
				+ " * Solicitante: " + solicitacao.getSolicitante() + "\n "
				+ " * Afetado: " + solicitacao.getAfetado() + "\n "
				+ " * Descrição: " + solicitacao.getDescricao() + "\n "
				+ " * Observação: " + solicitacao.getObservacao() + "\n "
				+ " * Status: " + solicitacao.getStatus() + "\n "
				+ " * Funcionario atribuído: " + solicitacao.getFuncionario().getNomeFuncionario() + "\n"
				+ "########################################\n\n";
		
		this.log = log;
	}
	
}
