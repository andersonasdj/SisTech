package br.com.techgol.app.dto;

import br.com.techgol.app.model.Colaborador;

public record DtoColaboradorEdit(

		Long id, 
		String nomeColaborador, 
		String celular, 
		Boolean vip
		) {
	
	public DtoColaboradorEdit(Colaborador c) {
		this(c.getId(), c.getNomeColaborador(), c.getCelular(), c.isVip());
		
	}
	
	

}
