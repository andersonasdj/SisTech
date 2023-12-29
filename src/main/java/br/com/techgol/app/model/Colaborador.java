package br.com.techgol.app.model;

import br.com.techgol.app.dto.DtoColaboradorCadastrar;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Entity
@Table(name = "colaboradores")
public class Colaborador {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String nomeColaborador;
	
	private String celular;
	
	private boolean vip;
	
	@ManyToOne
	private Cliente cliente;
	
	
	public Colaborador(DtoColaboradorCadastrar dados, Cliente cliente) {
		this.nomeColaborador = dados.nomeColaborador();
		this.celular = dados.celular();
		this.vip = dados.vip();
		this.cliente = cliente;
	}

}
