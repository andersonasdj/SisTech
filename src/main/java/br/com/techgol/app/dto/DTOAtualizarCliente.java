package br.com.techgol.app.dto;

import br.com.techgol.app.model.Cliente;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DTOAtualizarCliente(
		@NotNull
		Long id,
		@NotBlank
		String nomeCliente,
		@NotBlank
		String usuario,
		@NotBlank
		String senha,
		String endereco,
		String telefone,
		String cnpj,
		boolean ativo) {

	public DTOAtualizarCliente(Cliente c) {
		
		this(c.getId(),c.getNomeCliente(),c.getUsuario(),c.getSenha(),c.getEndereco(),c.getTelefone(),c.getCnpj(),c.getAtivo());
	}

}
