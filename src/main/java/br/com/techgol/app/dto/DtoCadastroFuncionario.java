package br.com.techgol.app.dto;

import br.com.techgol.app.model.enums.Funcao;
import jakarta.validation.constraints.NotBlank;

public record DtoCadastroFuncionario(
		
		@NotBlank
		String nomeFuncionario,
		@NotBlank
		String usuario,
		@NotBlank
		String senha,
		Funcao funcao) {

}
