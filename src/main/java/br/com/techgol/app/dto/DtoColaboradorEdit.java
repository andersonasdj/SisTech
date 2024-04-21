package br.com.techgol.app.dto;

import br.com.techgol.app.model.Colaborador;

public record DtoColaboradorEdit(

		Long id, 
		String nomeColaborador, 
		String celular, 
		Boolean vip,
		String email,
		Long idCliente
		) {
	
	public DtoColaboradorEdit(Colaborador c) {
		this(c.getId(), c.getNomeColaborador(), c.getCelular(), c.isVip(), c.getEmail(), c.getCliente().getId());
		
	}
	
	

}
