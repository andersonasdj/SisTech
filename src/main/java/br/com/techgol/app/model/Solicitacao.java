package br.com.techgol.app.model;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import br.com.techgol.app.dto.DTOCadastroSolicitacao;
import br.com.techgol.app.model.enums.FormaAbeertura;
import br.com.techgol.app.model.enums.Prioridade;
import br.com.techgol.app.model.enums.Status;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "solicitacoes")
@Getter
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
	private FormaAbeertura formaAbertura;
	
	@Column(length = 20)
	private String solicitante;
	@Column(length = 20)
	private String afetado;
	@Column(length = 300)
	private String descricao;
	@Column(length = 300)
	private String resolucao;
	@Column(length = 300)
	private String observacao;
	@Column(length = 20)
	private String abertoPor;
	@Enumerated(EnumType.STRING)
	private Prioridade prioridade;
	@Enumerated(EnumType.STRING)
	private Status status;
	@OneToOne
	private Cliente cliente;
	@OneToOne
	private Funcionario fucnionario;
	
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

}
