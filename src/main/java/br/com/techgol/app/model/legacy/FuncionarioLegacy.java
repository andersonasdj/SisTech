package br.com.techgol.app.model.legacy;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

//@Entity
//@Table(name = "Funcionario")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FuncionarioLegacy extends UsuarioLegacy {

	private String funcao;
	private String caminhoFoto;
	private String celular;
	
	
}