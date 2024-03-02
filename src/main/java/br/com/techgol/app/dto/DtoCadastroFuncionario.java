package br.com.techgol.app.dto;

import br.com.techgol.app.model.UserRole;
import jakarta.validation.constraints.NotBlank;

public record DtoCadastroFuncionario(
		
		@NotBlank
		String nomeFuncionario,
		@NotBlank
		String username,
		@NotBlank
		String password,
		UserRole role) {

}
