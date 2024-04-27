package br.com.techgol.app.model;

import br.com.techgol.app.dto.DtoModeloSolicitacao;
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
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "modelo_solicitacoes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ModeloSolicitacao {
	

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Enumerated(EnumType.STRING)
	private FormaAbertura formaAbertura;
	
	@Column(name="descricao", length = 300)
	private String descricao;	
	@Column(name="observacao", length = 300)
	private String observacao;
	@Column(name="prioridade")
	@Enumerated(EnumType.STRING)
	private Prioridade prioridade;
	@Column(name="status")
	@Enumerated(EnumType.STRING)
	private Status status;
	@Column(name = "categoria")
	@Enumerated(EnumType.STRING)
	private Categoria categoria;
	@Column(name = "classificacao")
	@Enumerated(EnumType.STRING)
	private Classificacao classificacao;
	@Column(name = "local")
	@Enumerated(EnumType.STRING)
	private Local local;
	
	
	public ModeloSolicitacao(DtoModeloSolicitacao dados, ConjuntoModelos conjuntoModelos) {
		this.categoria = dados.categoria();
		this.classificacao = dados.classificacao();
		this.descricao = dados.descricao();
		this.formaAbertura = dados.formaAbertura();
		this.local = dados.local();
		this.observacao = dados.observacao();
		this.prioridade = dados.prioridade();
		this.status = dados.status();
		this.conjuntoModelos = conjuntoModelos;
	
	}
	
	
	@ManyToOne
	private ConjuntoModelos conjuntoModelos;
}
