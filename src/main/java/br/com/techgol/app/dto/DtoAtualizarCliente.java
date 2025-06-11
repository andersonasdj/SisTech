package br.com.techgol.app.dto;

import br.com.techgol.app.model.Cliente;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DtoAtualizarCliente(
		@NotNull
		Long id,
		@NotBlank
		String nomeCliente,
		@NotBlank
		String username,
		@NotBlank
		String password,
		String endereco,
		String telefone,
		String cnpj,
		Boolean ativo,
		Boolean vip,
		Boolean redFlag,
		String bairro,
		Long tempoContratado,
		String token) {

	public DtoAtualizarCliente(Cliente c) {
		
		this(c.getId(),c.getNomeCliente(),c.getUsername(),c.getPassword(),c.getEndereco(),c.getTelefone(),c.getCnpj(),c.getAtivo(), c.isVip(), c.isRedFlag(), c.getBairro(), c.getTempoContratado(), c.getToken());
	}

}
