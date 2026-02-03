package br.com.techgol.app.dto;

import java.math.BigDecimal;

import br.com.techgol.app.model.Funcionario;

public record DtoListarCustoFuncionarios(
		Long id,
		BigDecimal valorHora) {
	
	public DtoListarCustoFuncionarios(Funcionario f) {
		this(
				f.getId(), 
				f.getValorHora()
				);
	}

}
