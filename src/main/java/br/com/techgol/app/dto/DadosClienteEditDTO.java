package br.com.techgol.app.dto;

import br.com.techgol.app.model.Cliente;

public record DadosClienteEditDTO(
		Long id,
		Boolean ativo,
		String nomeCliente,
		String endereco,
		String telefone,
		String usuario,
		String senha,
		String cnpj) {
	
	public DadosClienteEditDTO(Cliente c) {
		this(c.getId(),c.getAtivo(), c.getNomeCliente(),c.getEndereco(),c.getTelefone(),c.getUsuario(),c.getSenha(),c.getCnpj());
		
	}
	

}
