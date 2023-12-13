package br.com.techgol.app.model;

import br.com.techgol.app.model.enums.Funcao;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "funcionarios")
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Funcionario extends Usuario {
	
	@NotBlank
	@Column(length = 50)
	private String nomeFuncionario;
	@NotBlank
	@Column(length = 20)
	private String usuarioFuncionario;
	@NotBlank
	@Column(length = 50)
	private String senhaFuncionario;
	@Enumerated(EnumType.STRING)
	private Funcao funcao;
	private String caminhoFoto;
	

}
