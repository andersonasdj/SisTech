package br.com.techgol.app.model;

import br.com.techgol.app.dto.DtoAdicionaPesoSolicitacao;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "peso_solicitacoes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PesoSolicitacao {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String tipo;
	
	private String item;
	
	private Long peso;
	
	public PesoSolicitacao(DtoAdicionaPesoSolicitacao dado) {
		this.tipo = dado.tipo();
		this.item = dado.item().toUpperCase();
		this.peso = dado.peso();
	
	}

	public PesoSolicitacao(String item, long peso, String tipo) {
		this.item = item.toUpperCase();
		this.peso = peso;
		this.tipo = tipo;
	}
}
