package br.com.techgol.app.dto;

import jakarta.validation.constraints.NotBlank;

public record DTOCadastroCliente(
		@NotBlank
		String nomeCliente,
		@NotBlank
		String usuario,
		@NotBlank
		String senha,
		String endereco,
		String telefone,
		String cnpj
		) {}

