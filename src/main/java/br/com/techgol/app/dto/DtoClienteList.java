package br.com.techgol.app.dto;

import br.com.techgol.app.model.Cliente;

public record DtoClienteList(
		Long id,
		boolean ativo,
		String nomeCliente
		
		) {
	
	public DtoClienteList(Cliente c){
		this(c.getId(), c.getAtivo(), c.getNomeCliente());
		
		
	}

}
