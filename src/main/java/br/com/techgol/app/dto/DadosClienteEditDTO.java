package br.com.techgol.app.dto;

import br.com.techgol.app.model.Cliente;

public record DadosClienteEditDTO(
		Long id,
		Boolean ativo,
		String nomeCliente) {
	
	public DadosClienteEditDTO(Cliente c) {
		this(c.getId(),c.getAtivo(), c.getNomeCliente());
		
	}
	

}
