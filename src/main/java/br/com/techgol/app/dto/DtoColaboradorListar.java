package br.com.techgol.app.dto;

import br.com.techgol.app.model.Colaborador;

public record DtoColaboradorListar(
		Long id,
		String nomeColaborador,
		String celular,
		boolean vip,
		Long clienteId
		
		) {
	
	public DtoColaboradorListar(Colaborador c) {
		this(c.getId(), c.getNomeColaborador(), c.getCelular(), c.isVip(), c.getId());
	}

}
