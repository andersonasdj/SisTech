package br.com.techgol.app.dto;

import java.math.BigDecimal;

import br.com.techgol.app.model.Funcionario;
import br.com.techgol.app.model.UserRole;

public record DtoFuncionarioAdvancedEdit(
		Long id,
		UserRole role,
		BigDecimal valorHora
		) {
	
	public DtoFuncionarioAdvancedEdit(Funcionario f) {
		this(
				f.getId(), 
				f.getRole(),
				f.getValorHora()
				);
	}

}
