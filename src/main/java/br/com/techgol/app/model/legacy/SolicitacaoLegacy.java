package br.com.techgol.app.model.legacy;


import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "Solicitacao")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString
public class SolicitacaoLegacy {

	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	private Date dataAbertura;
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	private Date dataAndamento;
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	private Date dataFechamento;

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	private Date agendado;

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "HH:mm")
	private Date agendadoHora;
	
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	private Date dataAtualizacao;

	@OneToOne
	private ClienteLecagy cliente;
	@OneToOne
	private FuncionarioLegacy funcionario;
	private String formaAbertura;
	private String solicitante;
	private String usuario;
	private String prioridade;
	private String descricaoProblema;
	private String resolucao;
	private String status;
	private String obs;
	private String nivelDeIncidencia;
	private String onsiteOffsite;
	private String classificacao;
	private String statusEmail;
	private String abriuChamado;
	private String tempoDeAndamento;
	
	@Column(length = 1000)
	private String andamentoDoChamado;
	private Long estrela;
	private String senha;
	private boolean excluido;
	private String caminhoAnexo;
	private String comentario;
	private Long idChamadoLigacao;
	private Long minutos;
	private boolean play;
	
	//@OneToOne(fetch = FetchType.LAZY) //IGNORA O RELACIONAMENTO
	//@JoinColumn(name = "historico_id")
	//@OneToOne (fetch = FetchType.EAGER)
	//private Historico historico;
	
	//	public Solicitacao(DadosSolicitacaoCriar dados) {
	//		
	//		this.dataAbertura = new Date();
	//		this.dataAtualizacao = new Date();
	//		this.abriuChamado = dados.abriuChamado();
	//		this.cliente = dados.cliente();
	//		this.funcionario = dados.funcionario();
	//		this.solicitante = dados.solicitante();
	//		this.usuario = dados.usuario();
	//		this.descricaoProblema = dados.descricaoProblema();
	//		this.formaAbertura = dados.formaAbertura();
	//		this.prioridade = dados.prioridade();
	//		this.obs = dados.obs();
	//		this.nivelDeIncidencia = dados.nivelDeIncidencia();
	//		this.onsiteOffsite = dados.onsiteOffsite();
	//		this.classificacao = dados.classificacao();
	//		this.play = false;
	//	}

	public void excluir() {
		this.excluido = true;
	}
	
}