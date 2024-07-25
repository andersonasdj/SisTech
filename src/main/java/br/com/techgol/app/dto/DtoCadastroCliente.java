package br.com.techgol.app.dto;

import jakarta.validation.constraints.NotBlank;

public record DtoCadastroCliente(
		@NotBlank
		String nomeCliente,
		@NotBlank
		String username,
		@NotBlank
		String password,
		String endereco,
		String telefone,
		String cnpj,
		String bairro
		) {}

