package br.com.techgol.app.model;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import br.com.techgol.app.dto.DTOCadastroSolicitacao;
import br.com.techgol.app.dto.DtoCadastroSolicitacao;
import br.com.techgol.app.model.enums.Categoria;
import br.com.techgol.app.model.enums.Classificacao;
import br.com.techgol.app.model.enums.FormaAbertura;
import br.com.techgol.app.model.enums.Local;
import br.com.techgol.app.model.enums.Prioridade;
import br.com.techgol.app.model.enums.Status;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "solicitacoes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Solicitacao {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd-MM-yyyy HH:MM")
	private Date dataAbertura;
	
	@Enumerated(EnumType.STRING)
	private FormaAbertura formaAbertura;
	
	@Column(length = 60)
	private String solicitante;
	@Column(length = 60)
	private String afetado;
	@Column(length = 300)
	private String descricao;
	@Column(length = 300)
	private String resolucao;
	@Column(length = 300)
	private String observacao;
	@Column(length = 60)
	private String abertoPor;
	@Enumerated(EnumType.STRING)
	private Prioridade prioridade;
	@Enumerated(EnumType.STRING)
	private Status status;
	@Enumerated(EnumType.STRING)
	private Categoria categoria;
	@Enumerated(EnumType.STRING)
	private Classificacao classificacao;
	@Enumerated(EnumType.STRING)
	private Local local;
	@ManyToOne
	private Cliente cliente;
	@ManyToOne
	private Funcionario funcionario;
	
	private Boolean excluido;
	
	
	public Solicitacao(DTOCadastroSolicitacao dados) {
		
		this.afetado = dados.usuario();
        this.dataAbertura = dados.dataAbertura();
        this.solicitante = dados.solicitante();
        this.resolucao = dados.resolucao();
        this.descricao = dados.descricaoProblema();
        this.observacao = dados.obs();
        this.abertoPor = dados.abriuCHamado();
		
	}


	public Solicitacao(DtoCadastroSolicitacao dados, Cliente cliente, Funcionario funcionario) {
		this.cliente = cliente;
		this.formaAbertura = dados.formaAbertura();
		this.solicitante = dados.solicitante();
		this.afetado = dados.afetado();
		this.descricao = dados.descricao();
		this.observacao = dados.observacao();
		this.categoria = dados.categoria();
		this.classificacao = dados.classificacao();
		this.prioridade = dados.prioridade();
		this.local = dados.local();
		this.funcionario = funcionario;
		this.status = dados.status();
		this.excluido = false;
		this.dataAbertura = new Date();
	}
	
	public Solicitacao(DtoCadastroSolicitacao dados, Cliente cliente) {
		this.cliente = cliente;
		this.formaAbertura = dados.formaAbertura();
		this.solicitante = dados.solicitante();
		this.afetado = dados.afetado();
		this.descricao = dados.descricao();
		this.observacao = dados.observacao();
		this.categoria = dados.categoria();
		this.classificacao = dados.classificacao();
		this.prioridade = dados.prioridade();
		this.local = dados.local();
		this.status = dados.status();
		this.excluido = false;
		this.dataAbertura = new Date();
	}

}
