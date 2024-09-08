package br.com.techgol.app.model;

import java.io.Serializable;

import br.com.techgol.app.dto.DtoConjuntoModelo;
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
@Table(name = "conjunto_modelos")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ConjuntoModelos implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String nomeModelo;
	
	public ConjuntoModelos(DtoConjuntoModelo dado) {
		this.nomeModelo = dado.nomeModelo();

	}
}
