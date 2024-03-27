package br.com.techgol.app.dto;

import br.com.techgol.app.model.Cliente;

public record DadosClienteEditDTO(
		Long id,
		Boolean ativo,
		Boolean vip,
		String nomeCliente,
		String endereco,
		String telefone,
		String username,
		String password,
		String cnpj) {
	
	public DadosClienteEditDTO(Cliente c) {
		this(c.getId(),c.getAtivo(), c.isVip(), c.getNomeCliente(),c.getEndereco(),c.getTelefone(),c.getUsername(),c.getPassword(),c.getCnpj());
		
	}
	

}
